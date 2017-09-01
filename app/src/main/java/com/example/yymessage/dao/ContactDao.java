package com.example.yymessage.dao;

import java.io.InputStream;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;

public class ContactDao {
	/**
	 * ͨ�������ȡ��ϵ������
	 * @param resolver
	 * @param address
	 * @return
	 */
	public static String getNameByAddress(ContentResolver resolver,String address){
		String name=null;
		//�Ѻ���ƴ����Uri����
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		//���ݺ����ѯ��ϵ������
		Cursor cursor=resolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		if(cursor.moveToFirst()){
			name=cursor.getString(0);
			cursor.close();
		}
		return name;
	}
	/**
	 * ͨ�������ȡ��ϵ��ͷ��
	 * @param resolver
	 * @param address
	 * @return
	 */
	public static Bitmap getAvatarByAddress(ContentResolver resolver,String address){
		Bitmap bitmap=null;
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor cursor=resolver.query(uri, new String[]{PhoneLookup._ID}, null, null, null);
		if(cursor.moveToFirst()){
			String _id=cursor.getString(0);
			InputStream is=Contacts.openContactPhotoInputStream(resolver, Uri.withAppendedPath(Contacts.CONTENT_URI,_id));
			bitmap=BitmapFactory.decodeStream(is);
		}
		return bitmap;
		
	}
}
