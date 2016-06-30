package com.asen.demo;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class YssService extends IntentService {

	public YssService(String name) {
		super(name);
	}
	public YssService() {
		super("YssService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int count =  0;
		for (int i = 0; i < 1000; i++) {
			count++;
		}
		Log.i("haha", "ThreadId : "+Thread.currentThread().getId());
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("haha", "onDestroy");
	}
}
