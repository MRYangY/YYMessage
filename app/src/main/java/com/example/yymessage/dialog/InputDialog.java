package com.example.yymessage.dialog;

import com.example.yymessage.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InputDialog extends BaseDialog {

	private TextView tv_inputdialog_title;
	private EditText et_inputdialog_message;
	private Button bt_inputdialog_cancel;
	private Button bt_inputdialog_confirm;
	private String title;
	private OnInputDialogListener onInputDialogListener;

	protected InputDialog(Context context,String title,OnInputDialogListener onInputDialogListener) {
		super(context);
		this.title=title;
		this.onInputDialogListener=onInputDialogListener;
	}
	public static void showInputDialog(Context context,String title,OnInputDialogListener onInputDialogListener){
		InputDialog inputDialog=new InputDialog(context,title,onInputDialogListener);
		//对话框默认不支持文本输入，手动把一个输入框设置成对话框的内荣，Android自动对其进行设置
		inputDialog.setView(new EditText(context));
		inputDialog.show();
	}
	@Override
	public void initView() {
		setContentView(R.layout.dialog_input);
		tv_inputdialog_title = (TextView) findViewById(R.id.tv_inputdialog_title);
		et_inputdialog_message = (EditText) findViewById(R.id.et_inputdialog_message);
		bt_inputdialog_cancel = (Button) findViewById(R.id.bt_inputdialog_cancel);
		bt_inputdialog_confirm = (Button) findViewById(R.id.bt_inputdialog_confirm);

	}

	@Override
	public void initListener() {
		bt_inputdialog_cancel.setOnClickListener(this);
		bt_inputdialog_confirm.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		tv_inputdialog_title.setText(title);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_inputdialog_cancel:
			if(onInputDialogListener!=null){
				onInputDialogListener.onCancel();
			}
			break;
		case R.id.bt_inputdialog_confirm:
			if(onInputDialogListener!=null){
				onInputDialogListener.onComfirm(et_inputdialog_message.getText().toString());
			}
			break;
		}
		dismiss();
	}
	public interface OnInputDialogListener{
		void onCancel();
		void onComfirm(String text);
	}
}
