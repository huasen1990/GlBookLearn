package com.asen.demo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SensorActivity extends Activity implements OnClickListener{

	private TextView tv;
	private Button btLight,btAccelerometer,btMagneticAndAccelerometer;
	private ImageView iv;
	private SensorManager sensorManager;
	private Sensor sensor;
	private SensorEventListener listener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor);
		
		tv = (TextView) findViewById(R.id.tv);
		iv = (ImageView) findViewById(R.id.img);
		btLight = (Button) findViewById(R.id.light);
		btAccelerometer = (Button) findViewById(R.id.accelerometer);
		btMagneticAndAccelerometer = (Button) findViewById(R.id.accelerometerandmagnetic);
		
		btLight.setOnClickListener(this);
		btAccelerometer.setOnClickListener(this);
		btMagneticAndAccelerometer.setOnClickListener(this);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Toast.makeText(MyApplication.getContext(), "全局获取context_全局类", Toast.LENGTH_SHORT).show();

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.light:
			startLightSensor();
			break;
		case R.id.accelerometer:
			startAccelerometer();
			break;
		case R.id.accelerometerandmagnetic:
			startAccelerometerAndMagnetic();
			break;

		default:
			break;
		}
	}
	private void startAccelerometerAndMagnetic() {
		Sensor aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		listener = new MagneticAndAccelerometerListener();
		sensorManager.registerListener(listener, aSensor, SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(listener, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	private void startAccelerometer() {
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		listener = new AccelerateListener();
		sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
		
	}
	private void startLightSensor() {

		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		listener = new LightSensorListener();
		//SENSOR_DELAY_UI\SENSOR_DELAY_NORMAL\SENSOR_DELAY_GAME\SENSOR_DELAY_FASTEST
		sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	class LightSensorListener implements SensorEventListener{

		@Override
		public void onSensorChanged(SensorEvent event) {
			//光照传感器监听 value 数组中第一个下标的值就是当前的光照强度
			float value = event.values[0];
			tv.setText("当前光照强度  : "+value);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
		
	}
	class AccelerateListener implements SensorEventListener{
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			float xValue = Math.abs(event.values[0]);
			float yValue = Math.abs(event.values[1]);
			float zValue = Math.abs(event.values[2]);
			if (xValue>15||yValue>15||zValue>15) {
				tv.setText("检测到摇一摇  : x 轴 加速度 _"+xValue+"  y 轴 加速度_"+yValue+"  z 轴 加速度_"+zValue);
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
		
	}
	class MagneticAndAccelerometerListener implements SensorEventListener{
		
		float[] accelerometerValues = new float[3];
		float[] magneticValues = new float[3];
		private float lastRotateDegree;
		@Override
		public void onSensorChanged(SensorEvent event) {
			//判断当前传感器是地磁传感器还是加速度传感器
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				//赋值时要调用 clone 方法 不然两个valuse将会指向同一个引用
				accelerometerValues = event.values.clone();
			}else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magneticValues = event.values.clone();
			}
			
			float[] R = new float[9];
			float[] values = new float[3];
			//调用 getRotationMatrix 为 R 赋值
			SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
			//再调用 getOrientation 为 values 赋值
			SensorManager.getOrientation(R, values);
			//此时 values 已包含所有方向上旋转的弧度了 ， 其中 values[0] 表示手机围绕z轴旋转的弧度 
			//调用Math.getDegrees()将它转化成角度
			float rotateDegree = -(float) Math.toDegrees(values[0]);
			if (Math.abs(rotateDegree - lastRotateDegree)>1) {
				tv.setText("围绕 z 轴旋转的弧度  : "+values[0]);
				RotateAnimation animation = new RotateAnimation(lastRotateDegree, rotateDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				animation.setFillAfter(true);
				iv.startAnimation(animation);
				lastRotateDegree = rotateDegree;
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (listener!=null) {
			sensorManager.unregisterListener(listener);
		}
	}
}
