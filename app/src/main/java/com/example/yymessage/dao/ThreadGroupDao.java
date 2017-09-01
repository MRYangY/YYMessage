package com.example.yymessage.dao;

import com.example.yymessage.globle.Constant;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ThreadGroupDao {
	/**
	 * 判断这个会话id是否存在于某个群组，如果查询得到的cursor中有值的话，说明
	 * 这个会话id存在于某个群组,已经存在于thread_group表中
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static boolean hasGroup(ContentResolver resolver,int thread_id){
		Cursor cursor=resolver.query(Constant.URI.URI_THREAD_GROUP_QUERY, null, "thread_id= "+thread_id, null, null);
		return cursor.moveToNext();
	}
	/**
	 * 通过会话id得到群组id
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
	 * 会话插入群组
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
			//改变群组中的会话数量
			int thread_count=GroupDao.getThreadCountByGroupId(resolver, group_id);
			GroupDao.updateThreadCount(resolver, group_id, thread_count+1);
		}
		return uri!=null;
	}
	/**
	 * 删除会话
	 * @param resolver
	 * @param thread_id
	 * @return
	 */
	public static boolean deleteThreadFromThreadId(ContentResolver resolver,int thread_id,int group_id){
		int number=resolver.delete(Constant.URI.URI_THREAD_GROUP_DELETE, "thread_id="+thread_id, null);
		if(number>0){
			//改变群组中的会话数量
			int thread_count=GroupDao.getThreadCountByGroupId(resolver, group_id);
			GroupDao.updateThreadCount(resolver, group_id, thread_count-1);
		}
		return number>0;
	}
}
