package com.example.yymessage.dao;

import com.example.yymessage.globle.Constant;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ThreadGroupDao {
	/**
	 * �ж�����Ựid�Ƿ������ĳ��Ⱥ�飬�����ѯ�õ���cursor����ֵ�Ļ���˵��
	 * ����Ựid������ĳ��Ⱥ��,�Ѿ�������thread_group����
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static boolean hasGroup(ContentResolver resolver,int thread_id){
		Cursor cursor=resolver.query(Constant.URI.URI_THREAD_GROUP_QUERY, null, "thread_id= "+thread_id, null, null);
		return cursor.moveToNext();
	}
	/**
	 * ͨ���Ựid�õ�Ⱥ��id
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static int getGroupIdByThreadId(ContentResolver resolver,int thread_id){
		Cursor cursor=resolver.query(Constant.URI.URI_THREAD_GROUP_QUERY, new String[]{"group_id"}, "thread_id="+thread_id, null, null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
	/**
	 * �Ự����Ⱥ��
	 * @param resolver
	 * @param thread_id
	 * @param group_id
	 * @return
	 */
	public static boolean insertThreadGroup(ContentResolver resolver,int thread_id,int group_id){
		ContentValues values=new ContentValues();
		values.put("thread_id", thread_id);
		values.put("group_id", group_id);
		Uri uri=resolver.insert(Constant.URI.URI_THREAD_GROUP_INSERT, values);
		if(uri!=null){
			//�ı�Ⱥ���еĻỰ����
			int thread_count=GroupDao.getThreadCountByGroupId(resolver, group_id);
			GroupDao.updateThreadCount(resolver, group_id, thread_count+1);
		}
		return uri!=null;
	}
	/**
	 * ɾ���Ự
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static boolean deleteThreadFromThreadId(ContentResolver resolver,int thread_id,int group_id){
		int number=resolver.delete(Constant.URI.URI_THREAD_GROUP_DELETE, "thread_id="+thread_id, null);
		if(number>0){
			//�ı�Ⱥ���еĻỰ����
			int thread_count=GroupDao.getThreadCountByGroupId(resolver, group_id);
			GroupDao.updateThreadCount(resolver, group_id, thread_count-1);
		}
		return number>0;
	}
}
