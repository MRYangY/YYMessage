package com.example.yymessage.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yymessage.R;
import com.example.yymessage.adapter.AutoSearchAdapter;
import com.example.yymessage.base.BaseActivity;
import com.example.yymessage.dao.SmsDao;
import com.example.yymessage.utils.ToastUtils;

public class NewMsgActivity extends BaseActivity {
	//��������ʾ��ϵ���б�Ҳ��ʹ��adapter�������б���Ŀ
	private AutoCompleteTextView et_newmsg_address;
	private AutoSearchAdapter adapter;
	private ImageView iv_newmsg_select_contact;
	private EditText et_newmsg_body;
	private Button bt_newmsg_send;
	@Override
	public void initView() {
		setContentView(R.layout.activity_newmsg);
		et_newmsg_address = (AutoCompleteTextView) findViewById(R.id.et_newmsg_address);
		iv_newmsg_select_contact = (ImageView) findViewById(R.id.iv_newmsg_select_contact);
		et_newmsg_body = (EditText) findViewById(R.id.et_newmsg_body);
		bt_newmsg_send = (Button) findViewById(R.id.bt_newmsg_send);
		//���������б�ı���
		//et_newmsg_address.setDropDownBackgroundResource(id);
		//���������б����ֱƫ����
		et_newmsg_address.setDropDownVerticalOffset(5);
	}

	@Override
	public void initListener() {
		iv_newmsg_select_contact.setOnClickListener(this);
		bt_newmsg_send.setOnClickListener(this);
	}

	@Override
	public void initData() {
		initTitleBar();
		adapter = new AutoSearchAdapter(this, null);
		//�����������Adapter����adapter������ʾ�����������б�
		et_newmsg_address.setAdapter(adapter);
		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			//��������ĵ��ã�������ִ�в�ѯ��
			//constraint�û��������������ĺ��룬Ҳ����ģ����ѯ������
			@Override
			public Cursor runQuery(CharSequence constraint) {
				String[] projection={
						"data1",
						"display_name",
						"_id"
				};
				//ģ����ѯ
				String selection="data1 like '%"+constraint+"%'";
				Cursor cursor=getContentResolver().query(Phone.CONTENT_URI, projection, selection, null, null);
				//����cursor������ǰ�cursor����adapter
				return cursor;
			}
		});
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titlebar_back_btn:
			finish();
			break;
		case R.id.iv_newmsg_select_contact:
			Intent intent=new Intent(Intent.ACTION_PICK);
			intent.setType("vnd.android.cursor.dir/contact");
			startActivityForResult(intent, 0);
			break;
		case R.id.bt_newmsg_send:
			String address=et_newmsg_address.getText().toString();
			String body=et_newmsg_body.getText().toString();
			if(!TextUtils.isEmpty(address)&&!TextUtils.isEmpty(body)){
				SmsDao.sendSms(this, address, body);
			}
			break;
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//data�л�Я��һ��uri�������û�ѡ�����ϵ�˵�uri
		Uri uri=data.getData();
		String [] projection={
				"_id",
				"has_phone_number"
		};
		//����Ҫwhere��������Ϊuri�ǡ�һ������ϵ�˵�uri
		Cursor cursor=getContentResolver().query(uri, projection, null, null, null);
		//����Ҫ�ж��Ƿ�鵽������һ��Ҫ�ƶ�ָ��
		cursor.moveToFirst();
		String _id=cursor.getString(cursor.getColumnIndex("_id"));
		int has_phone_number=cursor.getInt(cursor.getColumnIndex("has_phone_number"));
		
		if(has_phone_number==0){
			ToastUtils.showToast(this, "����ϵ��û����ϵ����");
		}else{
			//����к��룬������ϵ��idȥPhone.CONTENT_URI��ѯ����
			String selection="contact_id="+_id;
			Cursor cursor2=getContentResolver().query(Phone.CONTENT_URI, new String[]{"data1"}, selection, null, null);
			cursor2.moveToFirst();
			String data1=cursor2.getString(cursor2.getColumnIndex("data1"));
			et_newmsg_address.setText(data1);
			//����������ȡ����
			et_newmsg_body.requestFocus();
		}
	
	}
	private void initTitleBar() {
		findViewById(R.id.iv_titlebar_back_btn).setOnClickListener(this);
		((TextView)findViewById(R.id.tv_titlebar_title)).setText("���Ͷ���");
	}
}
