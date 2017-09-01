package com.example.yymessage.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements OnClickListener{
	//返回一个View对象，这个对象会作为Fragment的显示内容
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return initView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initListener();
		initData();
	}
	public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	public abstract void initListener();
	public abstract void initData();
	public abstract void progressClick(View v);
	@Override
	public void onClick(View v) {
		progressClick(v);
		
	}
}
