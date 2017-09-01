package com.example.yymessage.dao;

import java.util.List;

import com.example.yymessage.globle.Constant;
import com.example.yymessage.receiver.SendSmsReceiver;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SmsDao {
	public static void sendSms(Context context,String address,String body){
		SmsManager manager=SmsManager.getDefault();
		List<String> smss=manager.divideMessage(body);
		Intent intent=new Intent(SendSmsReceiver.ACTION_SEND_SMS);
		//短信发送出去后，系统会发出一条广播，告诉我们短信发送成功了还是失败了
		PendingIntent sendIntent=PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		for (String text : smss) {
			manager.sendTextMessage(address, null, text, sendIntent, null);
			
			//吧短信插入到数据库
			insertSms(context, address, text);
		}
	}
	public static void insertSms(Context context,String address,String body) {
		ContentValues values=new ContentValues();
		values.put("address", address);
		values.put("body", body);
		values.put("type", Constant.SMS.TYPE_SEND);
		context.getContentResolver().insert(Constant.URI.URI_SMS, values);
	}
}
