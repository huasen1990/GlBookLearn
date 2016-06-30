package com.asen.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MessageAbortReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			abortBroadcast();
			Log.i("haha", "ÒÑ±»À¹½Ø");
		}
	}

}
