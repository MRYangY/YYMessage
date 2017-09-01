package com.example.yymessage.ui.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.yymessage.R;
import com.example.yymessage.adapter.GroupListAdapter;
import com.example.yymessage.base.BaseFragment;
import com.example.yymessage.bean.Group;
import com.example.yymessage.dao.GroupDao;
import com.example.yymessage.dao.SimpleQueryHandler;
import com.example.yymessage.dialog.InputDialog;
import com.example.yymessage.dialog.InputDialog.OnInputDialogListener;
import com.example.yymessage.dialog.ListDialog;
import com.example.yymessage.dialog.ListDialog.OnListDialogListener;
import com.example.yymessage.globle.Constant;
import com.example.yymessage.ui.activity.GroupDetailActivity;
import com.example.yymessage.utils.ToastUtils;

public class GroupFragment extends BaseFragment {

	private Button bt_group_newgroup;
	private ListView lv_group_list;
	private GroupListAdapter adapter;
	private SimpleQueryHandler queryHandler;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_group, null);
		bt_group_newgroup = (Button) view.findViewById(R.id.bt_group_newgroup);
		lv_group_list = (ListView) view.findViewById(R.id.lv_group_list);
		return view;
	}

	@Override
	public void initListener() {
		bt_group_newgroup.setOnClickListener(this);
		lv_group_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor cursor=(Cursor) adapter.getItem(position);
				final Group group=Group.createFromCursor(cursor);
				ListDialog.showDialog(getActivity(), "选择操作", new String[]{"修改","删除"}, new OnListDialogListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						switch (position) {
						case 0:
							//弹出输入对话框
							InputDialog.showInputDialog(getActivity(), "修改群组", new OnInputDialogListener() {
								
								@Override
								public void onComfirm(String text) {
									GroupDao.updateGroupName(getActivity().getContentResolver(), text, group.get_id());
								}
								
								@Override
								public void onCancel() {
								}
							});
							break;
						case 1:
							GroupDao.deleteGroup(getActivity().getContentResolver(), group.get_id());
							break;
						}
					}
				});
				return false;
			}
		});
		lv_group_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Cursor cursor=(Cursor)adapter.getItem(position);
				Group group=Group.createFromCursor(cursor);
				if(group.getThread_count()>0){
					Intent intent=new Intent(getActivity(), GroupDetailActivity.class);
					intent.putExtra("name", group.getName());
					intent.putExtra("group_id", group.get_id());
					startActivity(intent);
				}else{
					ToastUtils.showToast(getActivity(), "当前群组中没有会话");
				}
			}
		});
	}

	@Override
	public void initData() {
		adapter = new GroupListAdapter(getActivity(), null);
		lv_group_list.setAdapter(adapter);
		
		queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());
		queryHandler.startQuery(0, adapter, Constant.URI.URI_GROUP_QUERY, null, null, null, "create_date desc");
	}

	@Override
	public void progressClick(View v) {
		switch (v.getId()) {
		case R.id.bt_group_newgroup:
			InputDialog.showInputDialog(getActivity(), "创建群组", new OnInputDialogListener() {				
				@Override
				public void onComfirm(String text) {
					if(!TextUtils.isEmpty(text)){
						GroupDao.insertGroup(getActivity().getContentResolver(), text);
						
					}else{
						ToastUtils.showToast(getActivity(), "群组名不能为空");
					}
				}
				@Override
				public void onCancel() {
				}
			});
			break;
		}

	}

}
