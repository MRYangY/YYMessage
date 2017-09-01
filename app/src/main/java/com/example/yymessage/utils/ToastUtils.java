package com.example.yymessage.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void showToast(Context context,String msg){
		Toast.makeText(context, msg, 0).show();
	}
}
