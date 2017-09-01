package com.example.yymessage.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.yymessage.R;
import com.example.yymessage.adapter.ConversationListAdaptor;
import com.example.yymessage.base.BaseFragment;
import com.example.yymessage.bean.Conversation;
import com.example.yymessage.bean.Group;
import com.example.yymessage.dao.GroupDao;
import com.example.yymessage.dao.SimpleQueryHandler;
import com.example.yymessage.dao.ThreadGroupDao;
import com.example.yymessage.dialog.ConfirmDialog;
import com.example.yymessage.dialog.ConfirmDialog.OnConfirmListener;
import com.example.yymessage.dialog.DeleteMsgDialog;
import com.example.yymessage.dialog.DeleteMsgDialog.OnDeleteListener;
import com.example.yymessage.dialog.ListDialog;
import com.example.yymessage.dialog.ListDialog.OnListDialogListener;
import com.example.yymessage.globle.Constant;
import com.example.yymessage.ui.activity.ConversationDetailActivity;
import com.example.yymessage.ui.activity.NewMsgActivity;
import com.example.yymessage.utils.CursorUtils;
import com.example.yymessage.utils.LogUtils;
import com.example.yymessage.utils.ToastUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class ConversationFragment extends BaseFragment {

	private Button bt_conversation_edit;
	private Button bt_conversation_new_msg;
	private Button bt_conversation_select_all;
	private Button bt_conversation_cancel_select;
	private Button bt_conversation_delete;
	private LinearLayout ll_conversation_edit_menu;
	private LinearLayout ll_conversation_select_menu;
	private ListView lv_conversation_list;
	private ConversationListAdaptor adapter;
	private List<Integer> selectedConversationIds;
	static final int WAHT_DELETE_COMPLETE = 0;
	static final int WAHT_UPDATE_DELETE_PROGRESS = 1;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WAHT_DELETE_COMPLETE:
				adapter.setIsCheck(false);
				adapter.notifyDataSetChanged();
				showEditMenu();
				dialog.dismiss();
				break;
			case WAHT_UPDATE_DELETE_PROGRESS:
				dialog.onUpdateProgress(msg.arg1 + 1);
				break;
			}
		};
	};

	private DeleteMsgDialog dialog;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ��䲼���ļ�����View����
		View view = inflater.inflate(R.layout.fragment_conversation, null);
		bt_conversation_edit = (Button) view
				.findViewById(R.id.bt_conversation_edit);
		bt_conversation_new_msg = (Button) view
				.findViewById(R.id.bt_conversation_new_msg);
		bt_conversation_select_all = (Button) view
				.findViewById(R.id.bt_conversation_select_all);
		bt_conversation_cancel_select = (Button) view
				.findViewById(R.id.bt_conversation_cancel_select);
		bt_conversation_delete = (Button) view
				.findViewById(R.id.bt_conversation_delete);

		ll_conversation_edit_menu = (LinearLayout) view
				.findViewById(R.id.ll_conversation_edit_menu);
		ll_conversation_select_menu = (LinearLayout) view
				.findViewById(R.id.ll_conversation_select_menu);

		lv_conversation_list = (ListView) view
				.findViewById(R.id.lv_conversation_list);
		return view;
	}

	@Override
	public void initListener() {
		bt_conversation_edit.setOnClickListener(this);
		bt_conversation_new_msg.setOnClickListener(this);
		bt_conversation_select_all.setOnClickListener(this);
		bt_conversation_cancel_select.setOnClickListener(this);
		bt_conversation_delete.setOnClickListener(this);

		lv_conversation_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (adapter.getIsCheck()) {
					// ѡ��ģʽ
					adapter.selectSingle(position);
				} else {
					// ��ʾ��ϸ�Ự
					Intent intent = new Intent(getActivity(),
							ConversationDetailActivity.class);
					// Я�����ݣ�address,thread_id
					Cursor cursor = (Cursor) adapter.getItem(position);
					Conversation conversation = Conversation
							.createFromCursor(cursor);
					intent.putExtra("address", conversation.getAddress());
					intent.putExtra("thread_id", conversation.getThread_id());
					startActivity(intent);
				}

			}
		});
		lv_conversation_list
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						Cursor cursor = (Cursor) adapter.getItem(position);
						Conversation conversation = Conversation
								.createFromCursor(cursor);
						// �ж�ѡ�еĻỰ�Ƿ���������Ⱥ��
						if (ThreadGroupDao.hasGroup(getActivity()
								.getContentResolver(), conversation
								.getThread_id())) {
							// ��������Ⱥ�飬��ʾconfirmdialog
							showExitGroupDialog(conversation.getThread_id());
						} else {
							// ��������Ⱥ�飬��ʾlistdialog
							selectGroupListDialog(conversation.getThread_id());
						}
						// ���ѵ�����¼�������ᴫ��OnItemClickListener
						return true;
					}
				});

	}

	@Override
	public void initData() {
		// ����cursorAdaptor����
		adapter = new ConversationListAdaptor(getActivity(), null);
		lv_conversation_list.setAdapter(adapter);
		// ͬ����ѯ
		// Cursor
		// cursor=getActivity().getContentResolver().query(Constant.URI.URI_SMS_CONVERSATION,
		// null, null, null, null);
		// CursorUtils.printCursor(cursor);
		// �첽��ѯ
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity()
				.getContentResolver());
		String[] projection = { "sms.body AS snippet", "sms.thread_id AS _id",
				"groups.msg_count AS msg_count", "address AS address",
				"date AS date" };
		// arg0,arg1:���ò�ѯʱ�����ֵ
		queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION,
				projection, null, null, "date desc");
	}

	@Override
	public void progressClick(View v) {
		switch (v.getId()) {
		case R.id.bt_conversation_edit:
			showSelectMenu();
			// ����ѡ��ģʽ
			adapter.setIsCheck(true);
			adapter.notifyDataSetChanged();
			break;
		case R.id.bt_conversation_cancel_select:
			showEditMenu();
			// �˳�ѡ��ģʽ
			adapter.setIsCheck(false);
			adapter.cancelSelect();
			break;
		case R.id.bt_conversation_select_all:
			adapter.selectAll();
			break;
		case R.id.bt_conversation_delete:
			selectedConversationIds = adapter.getSelectedConversationIds();
			if (selectedConversationIds.size() == 0)
				return;
			else
				// deleteSms();
				showDeleteDialog();
			break;
		case R.id.bt_conversation_new_msg:
			Intent intent = new Intent(getActivity(), NewMsgActivity.class);
			startActivity(intent);

		}

	}

	/**
	 * �༭�˵������ƶ���ѡ��˵������ƶ�
	 */
	private void showSelectMenu() {
		ViewPropertyAnimator.animate(ll_conversation_edit_menu)
				.translationY(ll_conversation_edit_menu.getHeight())
				.setDuration(200);
		// �ӳ�200�����ִ��run��������Ĵ���
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ViewPropertyAnimator.animate(ll_conversation_select_menu)
						.translationY(0).setDuration(200);
			}
		}, 200);

	}

	private void showEditMenu() {
		ViewPropertyAnimator.animate(ll_conversation_select_menu)
				.translationY(ll_conversation_select_menu.getHeight())
				.setDuration(200);
		// �ӳ�200�����ִ��run��������Ĵ���
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ViewPropertyAnimator.animate(ll_conversation_edit_menu)
						.translationY(0).setDuration(200);
			}
		}, 200);
	}

	boolean isStopDelete = false;

	private void deleteSms() {
		dialog = DeleteMsgDialog.showDeleteMsgDialog(getActivity(),
				selectedConversationIds.size(), new OnDeleteListener() {

					@Override
					public void onDeleteCancel() {
						isStopDelete = true;

					}
				});
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < selectedConversationIds.size(); i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (isStopDelete) {
						isStopDelete = false;
						break;
					}
					// ȡ�������еĻỰId����id��Ϊwhere����ɾ�����з��������Ķ���
					String where = "thread_id="
							+ selectedConversationIds.get(i);
					getActivity().getContentResolver().delete(
							Constant.URI.URI_SMS, where, null);
					// ������Ϣ��ɾ��������ˢ�£����ɵ�ǰɾ�����ȴ���������
					Message msg = handler.obtainMessage();
					msg.what = WAHT_UPDATE_DELETE_PROGRESS;
					msg.arg1 = i;
					handler.sendMessage(msg);
				}
				handler.sendEmptyMessage(WAHT_DELETE_COMPLETE);
			}
		}).start();
	}

	private void showDeleteDialog() {
		ConfirmDialog.showDialog(getActivity(), "��ʾ", "���Ҫɾ���Ự��",
				new OnConfirmListener() {

					@Override
					public void onConfirm() {
						// TODO Auto-generated method stub
						deleteSms();
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub

					}
				});
	}

	private void showExitGroupDialog(final int thread_id) {
		// ͨ���Ựid��ȡȺ��id
		final int group_id = ThreadGroupDao.getGroupIdByThreadId(getActivity()
				.getContentResolver(), thread_id);
		// ͨ��Ⱥ��id��ȡȺ����
		String name = GroupDao.getGroupNameFromGroupId(getActivity()
				.getContentResolver(), group_id);
		String message = "�ûỰ�ѱ������[" + name + "]Ⱥ���У��Ƿ�Ҫ�˳���Ⱥ��";
		ConfirmDialog.showDialog(getActivity(), "��ʾ", message,
				new OnConfirmListener() {

					@Override
					public void onConfirm() {
						//��ѡ�еĻỰ��Ⱥ����ɾ��
						boolean isSucceed=ThreadGroupDao.deleteThreadFromThreadId(getActivity().getContentResolver(), thread_id,group_id);
						ToastUtils.showToast(getActivity(), isSucceed? "�˳��ɹ�":"�˳�ʧ��");
					}

					@Override
					public void onCancel() {
					}
				});
	}

	private void selectGroupListDialog(final int thread_id) {
		//����cursor��ȡȺ�����ƣ�����items������
		final Cursor cursor=getActivity().getContentResolver().query(Constant.URI.URI_GROUP_QUERY, null, null, null, null);
		String[] items=new String[cursor.getCount()];
		if(cursor.getCount()==0){
			ToastUtils.showToast(getActivity(), "Ŀǰû��Ⱥ�飬�봴��Ⱥ��");
			return;
		}
		while(cursor.moveToNext()){
			Group group=Group.createFromCursor(cursor);
			items[cursor.getPosition()]=group.getName();
		}
		ListDialog.showDialog(getActivity(), "ѡ��Ⱥ��", items, new OnListDialogListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//��cursor����group�е������ֶ�����
				cursor.moveToPosition(position);
				Group group=Group.createFromCursor(cursor);
				boolean isSucceed=ThreadGroupDao.insertThreadGroup(getActivity().getContentResolver(), thread_id, group.get_id());
				ToastUtils.showToast(getActivity(), isSucceed? "����ɹ�":"����ʧ��");
			}
		});

	}
}
