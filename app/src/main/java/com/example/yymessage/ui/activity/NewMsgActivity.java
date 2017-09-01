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
	//这个组件显示联系人列表也是使用adapter来设置列表条目
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
		//设置下拉列表的背景
		//et_newmsg_address.setDropDownBackgroundResource(id);
		//设置下拉列表的竖直偏移量
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
		//给输入框设置Adapter，该adapter负责显示输入框的下拉列表
		et_newmsg_address.setAdapter(adapter);
		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			//这个方法的调用，是用来执行查询的
			//constraint用户在输入框中输入的号码，也就是模糊查询的条件
			@Override
			public Cursor runQuery(CharSequence constraint) {
				String[] projection={
						"data1",
						"display_name",
						"_id"
				};
				//模糊查询
				String selection="data1 like '%"+constraint+"%'";
				Cursor cursor=getContentResolver().query(Phone.CONTENT_URI, projection, selection, null, null);
				//返回cursor对象就是吧cursor交给adapter
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
		//data中会携带一个uri，就是用户选择的联系人的uri
		Uri uri=data.getData();
		String [] projection={
				"_id",
				"has_phone_number"
		};
		//不需要where条件，因为uri是“一个”联系人的uri
		Cursor cursor=getContentResolver().query(uri, projection, null, null, null);
		//不需要判断是否查到，但是一定要移动指针
		cursor.moveToFirst();
		String _id=cursor.getString(cursor.getColumnIndex("_id"));
		int has_phone_number=cursor.getInt(cursor.getColumnIndex("has_phone_number"));
		
		if(has_phone_number==0){
			ToastUtils.showToast(this, "此联系人没有联系号码");
		}else{
			//如果有号码，拿着联系人id去Phone.CONTENT_URI查询号码
			String selection="contact_id="+_id;
			Cursor cursor2=getContentResolver().query(Phone.CONTENT_URI, new String[]{"data1"}, selection, null, null);
			cursor2.moveToFirst();
			String data1=cursor2.getString(cursor2.getColumnIndex("data1"));
			et_newmsg_address.setText(data1);
			//内容输入框获取焦点
			et_newmsg_body.requestFocus();
		}
	
	}
	private void initTitleBar() {
		findViewById(R.id.iv_titlebar_back_btn).setOnClickListener(this);
		((TextView)findViewById(R.id.tv_titlebar_title)).setText("发送短信");
	}
}
