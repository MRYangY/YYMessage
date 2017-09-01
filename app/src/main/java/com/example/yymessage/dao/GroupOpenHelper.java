package com.example.yymessage.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupOpenHelper extends SQLiteOpenHelper {

	private static GroupOpenHelper instance;
	public static GroupOpenHelper getInstance(Context context){
		if(instance==null){
			instance=new GroupOpenHelper(context, "group.db", null, 1);
		}
		return instance;
	}
	private GroupOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建groups表
		db.execSQL("create table groups("+
				"_id integer primary key autoincrement,"+
				"name text,"+
				"create_date integer,"+
				"thread_count integer"+
				")");
		//创建会话和群组的映射表
		db.execSQL("create table thread_group("+
				"_id integer primary key autoincrement,"+
				"group_id integer,"+
				"thread_id integer"+
				")");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
