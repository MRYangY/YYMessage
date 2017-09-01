package com.example.yymessage.dialog;

import com.example.yymessage.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class BaseDialog extends AlertDialog implements android.view.View.OnClickListener{

	protected BaseDialog(Context context) {
		//通过构造指定主题，主题中就已经设置了弧形边角的背景
		super(context,R.style.BaseDialog);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initdata();
	}
	public abstract void initView();
	public abstract void initListener();
	public abstract void initdata();
	public abstract void processClick(View v);
	@Override
	public void onClick(View v) {
		processClick(v);
		
	}
}
