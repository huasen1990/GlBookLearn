package com.asen.demo;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class LongRunService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//异步执行的逻辑功能
				Log.i("haha", "the service executed at "+new Date().toString());
			}
		}).start();
		
		AlarmManager manager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		int halfMinute = 30*1000;
//		long triggerAtTime = SystemClock.elapsedRealtime()+halfMinute;
		long triggerAtTime = System.currentTimeMillis()+halfMinute;
		
		Intent i = new Intent(this,AlarmReceiver.class);
		PendingIntent pi  = PendingIntent.getBroadcast(this, 0, i, 0);
		
//		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		manager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pi);
//		manager.setExact(type, triggerAtMillis, operation);
		
		return super.onStartCommand(intent, flags, startId);
	}
}
