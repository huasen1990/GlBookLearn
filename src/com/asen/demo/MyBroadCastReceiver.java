package com.asen.demo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

public class MyBroadCastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AlertDialog.Builder builder  = new AlertDialog.Builder(context);
		builder.setTitle("this is title");
		builder.setMessage("this is message ");
		builder.setCancelable(false);
		builder.setPositiveButton("ok", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("haha", "bingo");
			}
		});
		AlertDialog dialog  = builder.create();
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}

}
