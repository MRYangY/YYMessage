package com.example.yymessage.dao;

import com.example.yymessage.globle.Constant;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

public class GroupDao {
	/**
	 * 插入新的群组
	 * @param resolver
	 * @param groupName
	 */
	public static void insertGroup(ContentResolver resolver,String groupName){
		ContentValues values=new ContentValues();
		values.put("name", groupName);
		values.put("create_date", System.currentTimeMillis());
		values.put("thread_count", 0);
		resolver.insert(Constant.URI.URI_GROUP_INSERT, values);
	}
	public static void updateGroupName(ContentResolver resolver,String groupName,int _id){
		ContentValues values=new ContentValues();
		values.put("name",groupName);
		resolver.update(Constant.URI.URI_GROUP_UPDATE, values, "_id="+_id, null);
	}
	public static void deleteGroup(ContentResolver resolver,int _id){
		resolver.delete(Constant.URI.URI_GROUP_DELETE, "_id="+_id, null);
	}
	/**
	 * 通过群组id得到群组名
	 * @param resolver
	 * @param _id
	 * @return
	 */
	public static String getGroupNameFromGroupId(ContentResolver resolver,int _id){
		String name=null;
		Cursor cursor=resolver.query(Constant.URI.URI_GROUP_QUERY, new String[]{"name"}, "_id="+_id, null, null);
		if(cursor.moveToFirst()){
			name=cursor.getString(cursor.getColumnIndex("name"));
		}
		return name;
	}
	/**
	 * 获得群组里的会话数量
	 * @param resolver
	 * @param _id
	 * @return
	 */
	public static int getThreadCountByGroupId(ContentResolver resolver,int _id){
		int threadId=-1;
		Cursor cursor=resolver.query(Constant.URI.URI_GROUP_QUERY, new String[]{"thread_count"}, "_id="+_id, null, null);
		if(cursor.moveToFirst()){
			threadId=cursor.getInt(0);
		}
		return threadId;
	}
	/**
	 * 更新群组里面的会话数量
	 * @param resolver
	 * @param _id
	 * @param thread_count
	 */
	public static void updateThreadCount(ContentResolver resolver,int _id,int thread_count){
		ContentValues values=new ContentValues();
		values.put("thread_count", thread_count);
		resolver.update(Constant.URI.URI_GROUP_UPDATE, values, "_id="+_id, null);
	}
}
