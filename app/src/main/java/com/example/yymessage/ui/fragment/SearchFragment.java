package com.example.yymessage.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yymessage.R;
import com.example.yymessage.adapter.ConversationListAdaptor;
import com.example.yymessage.base.BaseFragment;
import com.example.yymessage.bean.Conversation;
import com.example.yymessage.dao.SimpleQueryHandler;
import com.example.yymessage.dialog.InputDialog;
import com.example.yymessage.globle.Constant;
import com.example.yymessage.ui.activity.ConversationDetailActivity;
import com.example.yymessage.ui.activity.GroupDetailActivity;

public class SearchFragment extends BaseFragment {

	private ListView lv_search_thread;
	private EditText et_search_thread;
	private ConversationListAdaptor adapter;
	private SimpleQueryHandler queryHandler;
	String[] projection = { "sms.body AS snippet", "sms.thread_id AS _id",
			"groups.msg_count AS msg_count", "address AS address",
			"date AS date" };

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_search, null);
		lv_search_thread = (ListView) view.findViewById(R.id.lv_search_thread);
		et_search_thread = (EditText) view.findViewById(R.id.et_search_thread);
		return view;
	}

	@Override
	public void initListener() {
		//添加文本改变侦听
		et_search_thread.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS_CONVERSATION, projection, "body like '%"+s+"%'", null, "date desc");
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		lv_search_thread.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				//跳转到GroupDetailActivity
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
		});
		lv_search_thread.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//获取输入法管理器
				//隐藏输入法软键盘
				InputMethodManager imm=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	@Override
	public void initData() {
		adapter = new ConversationListAdaptor(getActivity(), null);
		lv_search_thread.setAdapter(adapter);
		queryHandler = new SimpleQueryHandler(getActivity().getContentResolver());

	}

	@Override
	public void progressClick(View v) {
		// TODO Auto-generated method stub

	}

}
