package com.asen.demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseHelper extends SQLiteOpenHelper {

	public static final String CREATE_XXTABLE = "create table xx ("
			+ "id integer primary key autoincrement, " 
			+ "xxname text)";
	
//	public static final String CREATE_STUDENT = "create table Student (id integer primary key autoincrement,name text,height real)";
	public static final String CREATE_STUDENT = "create table Student (id integer primary key autoincrement,name text,height real,school_id integer)";
	public static final String CREATE_SCHOOL = "create table School (id integer primary key autoincrement,name text,phone integer)";
	//integer 整型 // real 浮点 // text 文本类型 // blob 二进制类型 //
	public MyDataBaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_STUDENT);
		db.execSQL(CREATE_SCHOOL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//注意 没有break 对应的不同版本 执行 不同的多个 语句 越是低的版本 升级到最新版 执行的语句越多
		switch (oldVersion) {
		case 1:
			db.execSQL(CREATE_SCHOOL);//从1版之后开始新建 school表
		case 2:
			db.execSQL("alter table Student add column school_id integer");//2版之前（1和2）创建student表是老的sql语句（被注释掉的 没有school_id这个字段） 2版之后开始插入新的字段
		default:
			break;
		}
	}

}
