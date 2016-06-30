package com.asen.demo;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

public class LhyService extends Service {

	MyBinder mBinder = new MyBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		NotificationCompat.Builder mBuilder = new Builder(this);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setTicker("foreground service start");
		mBuilder.setContentTitle("foreground service title");
		mBuilder.setContentText("foreground service content");
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pi);
		startForeground(1, mBuilder.build());
		
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//TODO 
				
				stopSelf();
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	class MyBinder extends Binder{
		public void startToDo(){
			Log.i("haha", "startToDo");
		};
		public void stopToDo(){
			Log.i("haha", "stopToDo");
		};
	}

}
