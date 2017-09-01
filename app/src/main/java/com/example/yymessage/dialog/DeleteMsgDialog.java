package com.example.yymessage.dialog;

import com.example.yymessage.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DeleteMsgDialog extends BaseDialog {

	private TextView tv_deletemsg_title;
	private ProgressBar pb_deletemsg;
	private Button bt_deletemsg_pause;
	private int maxProgress;
	private OnDeleteListener onDeleteListener;
	
	protected DeleteMsgDialog(Context context,int maxProgress,OnDeleteListener onDeleteListener) {
		super(context);
		this.maxProgress=maxProgress;
		this.onDeleteListener=onDeleteListener;
	}
	public static DeleteMsgDialog showDeleteMsgDialog(Context context,int maxProgress,OnDeleteListener onDeleteListener){
		DeleteMsgDialog deleteMsgDialog=new DeleteMsgDialog(context, maxProgress, onDeleteListener);
		deleteMsgDialog.show();
		return deleteMsgDialog;
	}
	@Override
	public void initView() {
		setContentView(R.layout.dialog_deletemsg);
		tv_deletemsg_title = (TextView) findViewById(R.id.tv_deletemsg_title);
		pb_deletemsg = (ProgressBar) findViewById(R.id.pb_deletemsg);
		bt_deletemsg_pause = (Button) findViewById(R.id.bt_deletemsg_pause);
		
	}

	@Override
	public void initListener() {
		bt_deletemsg_pause.setOnClickListener(this);

	}

	@Override
	public void initdata() {
		tv_deletemsg_title.setText("正在删除(0/"+maxProgress+")");
		pb_deletemsg.setMax(maxProgress);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.bt_deletemsg_pause:
			if(onDeleteListener!=null){
				onDeleteListener.onDeleteCancel();
			}
			dismiss();
			break;
		}

	}
	public interface OnDeleteListener{
		public void onDeleteCancel();
	}
	public void onUpdateProgress(int progress){
		pb_deletemsg.setProgress(progress);
		tv_deletemsg_title.setText("正在删除("+progress+"/"+maxProgress+")");
	}
}
