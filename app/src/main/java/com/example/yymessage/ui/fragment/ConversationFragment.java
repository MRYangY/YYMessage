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
		// 填充布局文件返回View对象
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
					// 选择模式
					adapter.selectSingle(position);
				} else {
					// 显示详细会话
					Intent intent = new Intent(getActivity(),
							ConversationDetailActivity.class);
					// 携带数据：address,thread_id
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
						// 判断选中的会话是否有所属的群组
						if (ThreadGroupDao.hasGroup(getActivity()
								.getContentResolver(), conversation
								.getThread_id())) {
							// 有所属的群组，显示confirmdialog
							showExitGroupDialog(conversation.getThread_id());
						} else {
							// 无所属的群组，显示listdialog
							selectGroupListDialog(conversation.getThread_id());
						}
						// 消费掉这个事件，否则会传给OnItemClickListener
						return true;
					}
				});

	}

	@Override
	public void initData() {
		// 创建cursorAdaptor对象
		adapter = new ConversationListAdaptor(getActivity(), null);
		lv_conversation_list.setAdapter(adapter);
		// 同步查询
		// Cursor
		// cursor=getActivity().getContentResolver().query(Constant.URI.URI_SMS_CONVERSATION,
		// null, null, null, null);
		// CursorUtils.printCursor(cursor);
		// 异步查询
		SimpleQueryHandler queryHandler = new SimpleQueryHandler(getActivity()
				.getContentResolver());
		String[] projection = { "sms.body AS snippet", "sms.thread_id AS _id",
				"groups.msg_count AS msg_count", "address AS address",
				"date AS date" };
		// arg0,arg1:调用查询时传入的值
		queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION,
				projection, null, null, "date desc");
	}

	@Override
	public void progressClick(View v) {
		switch (v.getId()) {
		case R.id.bt_conversation_edit:
			showSelectMenu();
			// 进入选择模式
			adapter.setIsCheck(true);
			adapter.notifyDataSetChanged();
			break;
		case R.id.bt_conversation_cancel_select:
			showEditMenu();
			// 退出选择模式
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
	 * 编辑菜单往下移动，选择菜单往上移动
	 */
	private void showSelectMenu() {
		ViewPropertyAnimator.animate(ll_conversation_edit_menu)
				.translationY(ll_conversation_edit_menu.getHeight())
				.setDuration(200);
		// 延迟200毫秒后执行run方法里面的代码
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
		// 延迟200毫秒后执行run方法里面的代码
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
					// 取出集合中的会话Id，以id作为where条件删除所有符合条件的短信
					String where = "thread_id="
							+ selectedConversationIds.get(i);
					getActivity().getContentResolver().delete(
							Constant.URI.URI_SMS, where, null);
					// 发送消息让删除进度条刷新，并吧当前删除进度传给进度条
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
		ConfirmDialog.showDialog(getActivity(), "提示", "真的要删除会话吗？",
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
		// 通过会话id获取群组id
		final int group_id = ThreadGroupDao.getGroupIdByThreadId(getActivity()
				.getContentResolver(), thread_id);
		// 通过群组id获取群组名
		String name = GroupDao.getGroupNameFromGroupId(getActivity()
				.getContentResolver(), group_id);
		String message = "该会话已被添加至[" + name + "]群组中，是否要退出该群组";
		ConfirmDialog.showDialog(getActivity(), "提示", message,
				new OnConfirmListener() {

					@Override
					public void onConfirm() {
						//把选中的会话从群组中删除
						boolean isSucceed=ThreadGroupDao.deleteThreadFromThreadId(getActivity().getContentResolver(), thread_id,group_id);
						ToastUtils.showToast(getActivity(), isSucceed? "退出成功":"退出失败");
					}

					@Override
					public void onCancel() {
					}
				});
	}

	private void selectGroupListDialog(final int thread_id) {
		//遍历cursor获取群组名称，存在items数组中
		final Cursor cursor=getActivity().getContentResolver().query(Constant.URI.URI_GROUP_QUERY, null, null, null, null);
		String[] items=new String[cursor.getCount()];
		if(cursor.getCount()==0){
			ToastUtils.showToast(getActivity(), "目前没有群组，请创建群组");
			return;
		}
		while(cursor.moveToNext()){
			Group group=Group.createFromCursor(cursor);
			items[cursor.getPosition()]=group.getName();
		}
		ListDialog.showDialog(getActivity(), "选择群组", items, new OnListDialogListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//此cursor包含group中的所有字段数据
				cursor.moveToPosition(position);
				Group group=Group.createFromCursor(cursor);
				boolean isSucceed=ThreadGroupDao.insertThreadGroup(getActivity().getContentResolver(), thread_id, group.get_id());
				ToastUtils.showToast(getActivity(), isSucceed? "插入成功":"插入失败");
			}
		});

	}
}
