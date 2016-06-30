package com.asen.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.DebugUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends FragmentActivity {

	private boolean flag = true;
	private LocalBroadcastManager manager;
	private MyBroadCastReceiver mReceiver;

	private EditText et;
	
	private EditText etName,etPsw;
	private CheckBox cbSave;
	private Button btLogin;

	private SharedPreferences mSf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.cancelAll();
//		notificationManager.cancel(1);
		
		etName = (EditText) findViewById(R.id.etName);
		etPsw = (EditText) findViewById(R.id.etPsw);
		
		cbSave = (CheckBox) findViewById(R.id.cb);
		btLogin  = (Button) findViewById(R.id.login);
		
//		Uri uri = Uri.parse("content://com.example.app.provider/table1");
		
		
		
//		SQLiteOpenHelper dbheHelper  = new MyDataBaseHelper(context, name, factory, version);
//		SQLiteOpenHelper dbHelper  = new MyDataBaseHelper(this, "xx.db", null, 1);
//		dbHelper.getWritableDatabase();//可读写 磁盘必须充足
//		dbHelper.getReadableDatabase();//只读   磁盘已满 也可调用
//		
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		
//		values.put("ziduanming", "duiyingleixingdezhi");
//		values.put("ziduanming1", 12);
//		values.put("ziduanming2", 12.12);
//		
//		db.insert("xx", null, values);
//		
//		//更新name为xxx的所有数据
//		values.put("yaogengxindeziduanming","xindezhi");
//		db.update("xx", values, "name = ?", new String[]{"nameduiyingdezhi"});
//		//删除数据
//		db.delete("xx", "id = ?", new String[]{"1"});
		//查询数据
//		Cursor cursor  = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
//		Cursor cursor  = db.query("xx", null, null, null, null, null, null);
//		if (cursor.moveToFirst()) {
//			do {
//				String name = cursor.getString(cursor.getColumnIndex("name"));
////				String name  = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//				double price  = cursor.getDouble(cursor.getColumnIndex("price"));
//			} while (cursor.moveToNext());
//		}
//		cursor.close();
		
		//使用SQL操作数据库(结构化查询语言) Structured Query Language  CRUD 
//		db.execSQL("insert into Book (name,author,pages,price) values(?,?,?,?)",new String[]{"myName","asen","121","48"});
//		db.execSQL("update Book set price = ? where name = ?",new String[]{"92","myName"});
//		db.execSQL("delete from Book where pages > ?",new String[]{"97"});
//		db.rawQuery("select * from Book ", null);//除了查询数据时调用的是 SQLiteDatabase的rawQuery()方法,其他的操作方法都是调用的execSQL()方法
//		
//		db.beginTransaction();
//		try {
//			//DO something about db
//			//db CURD
//			db.setTransactionSuccessful();
//	
//		} catch (Exception e) {
//			
//		}finally{
//			db.endTransaction();
//		}
//		mSf = getSharedPreferences("mySFName", Context.MODE_MULTI_PROCESS);//多线程操作同一个sf
//		mSf = getSharedPreferences("mySFName", Context.MODE_PRIVATE);//自定义sf文件名
//		mSf = getPreferences(Context.MODE_PRIVATE);//sf名  为 当前活动的类名
		mSf = PreferenceManager.getDefaultSharedPreferences(this);//sf 名 当前包名为前缀
		
		Log.i("haha", "hello create");
		if (mSf.getBoolean("autologin", false)) {
			etName.setText(mSf.getString("name", ""));
			etPsw.setText(mSf.getString("code", ""));
			cbSave.setChecked(true);
		}
		
		btLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name  = etName.getText().toString().trim();
				String code = etPsw.getText().toString().trim();
				if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(code)) {
					if (cbSave.isChecked()) {
						Editor editor = mSf.edit();
						editor.putString("name", name);
						editor.putString("code", code);
						editor.putBoolean("autologin", true);
						editor.commit();
						Log.i("haha", "hello asen");
					}
				}
			}
		});
		
//		et = (EditText) findViewById(R.id.edittext);
//		String content  = getSavedContent();
//		if (!TextUtils.isEmpty(content)) {
//			et.setText(content);
//			et.setSelection(content.length());
//		}
//		
		
		manager = LocalBroadcastManager.getInstance(this);
		IntentFilter infFilter = new IntentFilter();
		infFilter.addAction("com.haha.mybroad");
		mReceiver = new MyBroadCastReceiver();
		manager.registerReceiver(mReceiver, infFilter);
		// test();

		Button button = (Button) findViewById(R.id.button);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				manager.sendBroadcast(new Intent("com.haha.mybroad"));

				 FragmentManager manager = getSupportFragmentManager();
				 FragmentTransaction transaction = manager.beginTransaction();
				 if (flag) {
				 RightFragmentNew f = new RightFragmentNew();
				 transaction.replace(R.id.right, f);
				 transaction.addToBackStack(null);
				 transaction.commit();
				 flag =false;
				 }else {
				 RightFragment f = new RightFragment();
				 transaction.replace(R.id.right, f);
				 transaction.addToBackStack(null);
				 transaction.commit();
				 flag = true;
				 }
			}
		});
	}

	private String getSavedContent() {
		FileInputStream input  = null;
		BufferedReader reader  = null;
		StringBuilder content  = new StringBuilder();
		try {
			input  = openFileInput("savedfilename");
			reader = new BufferedReader(new InputStreamReader(input));
			String line  = "";
			while ((line = reader.readLine())!=null) {
				content.append(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content.toString();
	}

	private void test() {

		// LocalBroadcastManager.getInstance(this).registerReceiver(xxReceiver,
		// intentFilter);
		// LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		// LocalBroadcastManager.getInstance(this).unregisterReceiver(xxReceiver);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.unregisterReceiver(mReceiver);
//		save();
	}

	private void save() {
		String content = et.getText().toString().trim();
		FileOutputStream out = null;
		BufferedWriter writer = null;

		try {
			out = openFileOutput("savedfilename", Context.MODE_WORLD_READABLE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
