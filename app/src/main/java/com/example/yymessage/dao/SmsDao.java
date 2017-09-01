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
		//���ŷ��ͳ�ȥ��ϵͳ�ᷢ��һ���㲥���������Ƕ��ŷ��ͳɹ��˻���ʧ����
		PendingIntent sendIntent=PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		for (String text : smss) {
			manager.sendTextMessage(address, null, text, sendIntent, null);
			
			//�ɶ��Ų��뵽���ݿ�
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
