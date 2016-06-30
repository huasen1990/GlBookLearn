package com.asen.demo;

import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AsenActivity extends Activity {

	private EditText et,etSmsPhone,etSmsContent;
	private TextView tvFromPhone,tvFromContent;
	private Button bt,btSms;
	private NotificationManager mNotificationManager;
	private SendSmsReceiver sendSmsReceiver;
	private MessageReceiver messageReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.asen);
		
		initView();
		initConfig();
		setListener();
	}
	private void initConfig() {
		
		IntentFilter senderFilter = new IntentFilter();
		senderFilter.addAction("SENT_SMS_ACTION");
		sendSmsReceiver = new SendSmsReceiver();
		registerReceiver(sendSmsReceiver, senderFilter);
		
		IntentFilter receiveSmsFilter  = new IntentFilter();
		receiveSmsFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		receiveSmsFilter.setPriority(100);
		messageReceiver = new MessageReceiver();
		registerReceiver(messageReceiver, receiveSmsFilter);
		
		
		mNotificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);		
	}
	private void initView() {
		et = (EditText) findViewById(R.id.et);
		bt = (Button) findViewById(R.id.bt);
		etSmsPhone = (EditText) findViewById(R.id.etSmsPhone);
		etSmsContent = (EditText) findViewById(R.id.etSmsContent);
		btSms = (Button) findViewById(R.id.btSms);
		
		etSmsPhone.setText("18210824890");
		etSmsContent.setText("haha this is a test sms");
		
		tvFromPhone = (TextView) findViewById(R.id.tvFromPhone);
		tvFromContent = (TextView) findViewById(R.id.tvFromContent);
		
		tvFromPhone.setText("发送人电话号码");
		tvFromContent.setText("接收的短信内容");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(sendSmsReceiver);
		unregisterReceiver(messageReceiver);
	}
	private void showNotifcation(String content) {
		
		Intent intent  = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		
//		Notification notification = new Notification(R.drawable.ic_launcher, "一闪而过", System.currentTimeMillis());
//		notification.setLatestEventInfo(this, "title", "cotent", pi);
//		notification.defaults = Notification.DEFAULT_ALL;
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
//		mNotificationManager.notify(1, notification);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		
		builder.setWhen(System.currentTimeMillis());
		builder.setTicker("tickerText");
		builder.setSmallIcon(R.drawable.ic_launcher);
		
		builder.setContentTitle("notification title");
		builder.setContentText("Notification content");
		builder.setContentIntent(pi);
//		builder.setDeleteIntent(pi);
//		builder.setFullScreenIntent(pi,true);
//		builder.setPriority(Notification.PRIORITY_MAX);
		builder.setOngoing(false);
		builder.setLights(0xffff0000, 3000, 2000);
		Notification notification =  builder.build();
		
//		notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中   
//	    notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用   
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
	    notification.flags |= Notification.FLAG_SHOW_LIGHTS;  
//		notification.ledARGB = Color.BLUE;
//		notification.ledOnMS = 3000;
//		notification.ledOffMS = 2000;
		
		notification.defaults = Notification.DEFAULT_LIGHTS;
		mNotificationManager.notify(1, notification);
	}
	private void setListener() {
		
		btSms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("SENT_SMS_ACTION");
				PendingIntent pi  = PendingIntent.getBroadcast(AsenActivity.this, 0, intent, 0);
				SmsManager smsManager = SmsManager.getDefault();
				List<String> divideSms = smsManager.divideMessage( etSmsContent.getText().toString().trim());
				for (String smsContent : divideSms) {
					smsManager.sendTextMessage(etSmsPhone.getText().toString().trim(), null,smsContent, pi, null);
				}
			}
		});
		
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				showNotifcation("");
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						showNotifcation("");
					}
				}, 5000);
				
			}
		});
	}
	class SendSmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (getResultCode() == RESULT_OK) {
				Toast.makeText(context, "sendsms success", Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(context, "sendsms fail", Toast.LENGTH_SHORT).show();
			}
		}
	}
	class MessageReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			Bundle bundle = intent.getExtras();
			Object[] pdus = (Object[]) bundle.get("pdus");//接收短信消息
			SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i = 0; i < messages.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
			}
			String address = messages[0].getOriginatingAddress();
			//获取发送方号码
			String fullMessage = "";
			for (SmsMessage smsMessage : messages) {
				fullMessage += smsMessage.getMessageBody();//获取短信内容
			}
			tvFromPhone.setText(address);
			tvFromContent.setText(fullMessage);
			
		}
		
	}
}
