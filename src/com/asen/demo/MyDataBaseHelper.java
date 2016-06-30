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
	//integer ���� // real ���� // text �ı����� // blob ���������� //
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
		//ע�� û��break ��Ӧ�Ĳ�ͬ�汾 ִ�� ��ͬ�Ķ�� ��� Խ�ǵ͵İ汾 ���������°� ִ�е����Խ��
		switch (oldVersion) {
		case 1:
			db.execSQL(CREATE_SCHOOL);//��1��֮��ʼ�½� school��
		case 2:
			db.execSQL("alter table Student add column school_id integer");//2��֮ǰ��1��2������student�����ϵ�sql��䣨��ע�͵��� û��school_id����ֶΣ� 2��֮��ʼ�����µ��ֶ�
		default:
			break;
		}
	}

}
