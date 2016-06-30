package com.asen.demo;

import com.asen.demo.LhyService.MyBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LearnAsync extends Activity implements OnClickListener{

	private Button bt;
	ServiceConnection conn=  new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
			Log.i("haha", "ComponentName : "+name);
			Log.i("haha", "onServiceDisconnected");
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LhyService.MyBinder myBinder = (MyBinder) service;
			myBinder.startToDo();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learnasync);
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(this);
		
		Log.i("haha", "ThreadId : "+Thread.currentThread().getId());

//		Intent intent = new Intent(this, YssService.class);
//		startService(intent);
				
//		Intent serviceIntent =new Intent(this,LhyService.class);
//		bindService(service, conn, flags)
//		bindService(serviceIntent, conn, BIND_AUTO_CREATE);
//		Log.i("haha", "nanshou");
		
//		new AsyncTask<Params, Progress, Result>() {
//		};
//		new AsyncTask<Void, Integer, Boolean>() {
//
//			@Override
//			protected Boolean doInBackground(Void... params) {
//				Log.i("haha","doInBackground");
//				publishProgress(50);
//				return null;
//			}
//			@Override
//			protected void onProgressUpdate(Integer... values) {
//				Log.i("haha","onProgressUpdate");
//				Log.i("haha", ""+values[0]);
//				super.onProgressUpdate(values);
//			}
//			@Override
//			protected void onPreExecute() {
//				Log.i("haha","onPreExecute");
//				super.onPreExecute();
//			}
//			@Override
//			protected void onPostExecute(Boolean result) {
//				super.onPostExecute(result);
//				Log.i("haha","onPostExecute");
//			}
//			
//		}.execute();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt:
//			unbindService(conn);
			startService(new Intent(this,LongRunService.class));
			break;

		default:
			break;
		}
	}
}
