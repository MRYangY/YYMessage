package com.example.yymessage.utils;

import android.database.Cursor;

public class CursorUtils {
	public static void printCursor(Cursor cursor){
									//��ȡ����
		LogUtils.i(cursor,"һ���У�"+cursor.getCount()+"������");
		while(cursor.moveToNext()){
			//					��ȡ�ֶ�����
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				//					��ȡ�ֶ���
				String name=cursor.getColumnName(i);
				String content=cursor.getString(i);
				LogUtils.i(cursor, name+":"+content);
			}
			LogUtils.i(cursor, "================================");
		}
	}
}
