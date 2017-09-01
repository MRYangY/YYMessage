package com.example.yymessage.dialog;

import com.example.yymessage.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog extends BaseDialog {
	private String title;
	private String message;
	private TextView tv_dialog_title;
	private TextView tv_dialog_message;
	private Button bt_dialog_confirm;
	private Button bt_dialog_cancel;
	private OnConfirmListener onConfirmListener;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	protected ConfirmDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public static void showDialog(Context context,String title,String message,OnConfirmListener onConfirmListener){
		ConfirmDialog dialog = new ConfirmDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setOnConfirmListener(onConfirmListener);
		dialog.show();
	}
	@Override
	public void initView() {
		// 设置对话框显示的布局文件
		setContentView(R.layout.dialog_config);
		tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
		tv_dialog_message = (TextView) findViewById(R.id.tv_dialog_message);
		bt_dialog_confirm = (Button) findViewById(R.id.bt_dialog_confirm);
		bt_dialog_cancel = (Button) findViewById(R.id.bt_dialog_cancel);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		bt_dialog_confirm.setOnClickListener(this);
		bt_dialog_cancel.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub
		tv_dialog_title.setText(title);
		tv_dialog_message.setText(message);
	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_dialog_confirm:
			//如果取消按钮按下时，侦听存在，那么调用侦听的onConfirm()方法
			if(onConfirmListener!=null){
				onConfirmListener.onConfirm();
			}
			break;
		case R.id.bt_dialog_cancel:
			if(onConfirmListener!=null){
				onConfirmListener.onCancel();
			}
			break;		
		}
		dismiss();
	}
	
	public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
		this.onConfirmListener = onConfirmListener;
	}

	public interface OnConfirmListener{
		void onCancel();
		void onConfirm();
	}
}
