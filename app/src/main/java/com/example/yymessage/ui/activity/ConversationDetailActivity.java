package com.example.yymessage.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yymessage.R;
import com.example.yymessage.adapter.ConversationDetailAdapter;
import com.example.yymessage.base.BaseActivity;
import com.example.yymessage.dao.ContactDao;
import com.example.yymessage.dao.SimpleQueryHandler;
import com.example.yymessage.dao.SmsDao;
import com.example.yymessage.globle.Constant;

public class ConversationDetailActivity extends BaseActivity {

	private String address;
	private int thread_id;
	private SimpleQueryHandler queryHandler;
	private ConversationDetailAdapter adapter;
	private ListView lv_conversation_detail;
	private EditText et_conversation_detail;
	private Button bt_conversation_detail;

	@Override
	public void initView() {
		setContentView(R.layout.activity_conversation_detail);
		lv_conversation_detail = (ListView) findViewById(R.id.lv_conversation_detail);
		et_conversation_detail = (EditText) findViewById(R.id.et_conversation_detail);
		bt_conversation_detail = (Button) findViewById(R.id.bt_conversation_detail);
		//ֻҪlistviewˢ�£��ͻỬ��
		lv_conversation_detail.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	}

	@Override
	public void initListener() {
		bt_conversation_detail.setOnClickListener(this);

	}

	@Override
	public void initData() {
		// �õ����ݹ���������
		Intent intent=getIntent();
		if(intent!=null){
			address = intent.getStringExtra("address");
			thread_id = intent.getIntExtra("thread_id", -1);
			
			initTitleBar();
		}
		//���Ự��ϸ�����listview����adapter����ʾ�Ự�����ж���
		adapter = new ConversationDetailAdapter(this, null,lv_conversation_detail);
		lv_conversation_detail.setAdapter(adapter);
		//���ջỰID��ѯ���ڸûỰID�����ж���
		String[] projection={
				"_id",
				"body",
				"type",
				"date"
		};
		String selection="thread_id="+thread_id;
		//�첽��ѯ����		
		queryHandler = new SimpleQueryHandler(getContentResolver());
		queryHandler.startQuery(0, adapter, Constant.URI.URI_SMS, projection, selection, null, "date");
//		getContentResolver().query(Constant.URI.URI_SMS, projection, selection, null, null);
	}
	
	/**
	 * ��ʼ��������
	 */
	private void initTitleBar() {
		findViewById(R.id.iv_titlebar_back_btn).setOnClickListener(this);
		String name=ContactDao.getNameByAddress(getContentResolver(), address);
		((TextView)findViewById(R.id.tv_titlebar_title)).setText(TextUtils.isEmpty(name)? address:name);
		
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titlebar_back_btn:
			finish();
			break;
		case R.id.bt_conversation_detail:
			String body=et_conversation_detail.getText().toString();
			if(!TextUtils.isEmpty(body)){
				SmsDao.sendSms(this,address, body);
				et_conversation_detail.setText("");
			}
			break;
		}

	}

}
