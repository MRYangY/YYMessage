package com.example.yymessage.receiver;

import com.example.yymessage.utils.ToastUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SendSmsReceiver extends BroadcastReceiver {
	public static final String ACTION_SEND_SMS="com.example.yymessage.sendsms";
	@Override
	public void onReceive(Context context, Intent intent) {
		int code=getResultCode();
		if(Activity.RESULT_OK==code){
			ToastUtils.showToast(context, "发送成功");
		}else{
			ToastUtils.showToast(context, "发送失败");
		}
	}
	
}
