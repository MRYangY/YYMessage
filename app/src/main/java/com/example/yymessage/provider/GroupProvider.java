package com.example.yymessage.provider;

import java.net.URI;

import com.example.yymessage.dao.GroupOpenHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class GroupProvider extends ContentProvider {
	GroupOpenHelper helper;
	private SQLiteDatabase db;
	private static final String TABLE_GROUPS="groups";
	private static final String TABLE_THREAD_GROUP="thread_group";
	static final int CODE_GROUPS_INSERT=0;
	static final int CODE_GROUPS_QUERY=1;
	static final int CODE_GROUPS_DELETE=2;
	static final int CODE_GROUPS_UPDATE=3;
	static final int CODE_THREAD_GROUP_INSERT=4;
	static final int CODE_THREAD_GROUP_QUERY=5;
	static final int CODE_THREAD_GROUP_DELETE=6;
	static final int CODE_THREAD_GROUP_UPDATE=7;
	private static final String authority="com.example.yymessage";
	public static final Uri BASE_URI=Uri.parse("content://"+authority);
	UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);	
	{
		//添加匹配规则
		matcher.addURI(authority, "groups/insert", CODE_GROUPS_INSERT);
		matcher.addURI(authority, "groups/query", CODE_GROUPS_QUERY);
		matcher.addURI(authority, "groups/delete", CODE_GROUPS_DELETE);
		matcher.addURI(authority, "groups/update", CODE_GROUPS_UPDATE);
		
		matcher.addURI(authority, "thread_group/insert", CODE_THREAD_GROUP_INSERT);
		matcher.addURI(authority, "thread_group/query", CODE_THREAD_GROUP_QUERY);
		matcher.addURI(authority, "thread_group/delete", CODE_THREAD_GROUP_DELETE);
		matcher.addURI(authority, "thread_group/update", CODE_THREAD_GROUP_UPDATE);
	}	

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (matcher.match(uri)) {
		case CODE_GROUPS_INSERT:
			long rawId=db.insert(TABLE_GROUPS, null, values);
			if(rawId==-1){
				return null;
			}else{
				getContext().getContentResolver().notifyChange(BASE_URI, null);
				//把返回的行id，拼接在uri后面，并返回
				return ContentUris.withAppendedId(uri, rawId);
			}
		case CODE_THREAD_GROUP_INSERT:
			rawId=db.insert(TABLE_THREAD_GROUP, null, values);
			if(rawId==-1){
				return null;
			}else{
				getContext().getContentResolver().notifyChange(BASE_URI, null);
				//把返回的行id，拼接在uri后面，并返回
				return ContentUris.withAppendedId(uri, rawId);
			}
		default:
			throw new IllegalArgumentException("未识别的Uri:"+uri);
		}
	}

	
	public boolean onCreate() {
		helper=GroupOpenHelper.getInstance(getContext());
		db = helper.getWritableDatabase();
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (matcher.match(uri)) {
		case CODE_GROUPS_QUERY:
			Cursor cursor=db.query(TABLE_GROUPS, projection, selection, selectionArgs, null, null, sortOrder);
			//监视URI上数据改变的一个内容观察者
			//只要这个uri上的数据发生改变，内容观察者会立即发现，重新查询
			cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
			return cursor;
		case CODE_THREAD_GROUP_QUERY:
			cursor=db.query(TABLE_THREAD_GROUP, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
			return cursor;
		default:
			throw new IllegalArgumentException("未识别的Uri:"+uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		switch (matcher.match(uri)) {
		case CODE_GROUPS_UPDATE:
			int number=db.update(TABLE_GROUPS, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(BASE_URI, null);
			return number;
		case CODE_THREAD_GROUP_UPDATE:
			number=db.update(TABLE_THREAD_GROUP, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(BASE_URI, null);
			return number;
		default:
			throw new IllegalArgumentException("未识别的Uri:"+uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (matcher.match(uri)) {
		case CODE_GROUPS_DELETE:
			int number=db.delete(TABLE_GROUPS, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(BASE_URI, null);
			return number;
		case CODE_THREAD_GROUP_DELETE:
			number=db.delete(TABLE_THREAD_GROUP, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(BASE_URI, null);
			return number;
		default:
			throw new IllegalArgumentException("未识别的Uri:"+uri);
		}
	}

}
