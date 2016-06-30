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
		Toast.makeText(MyApplication.getContext(), "ȫ�ֻ�ȡcontext_ȫ����", Toast.LENGTH_SHORT).show();

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
			//���մ��������� value �����е�һ���±��ֵ���ǵ�ǰ�Ĺ���ǿ��
			float value = event.values[0];
			tv.setText("��ǰ����ǿ��  : "+value);
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
				tv.setText("��⵽ҡһҡ  : x �� ���ٶ� _"+xValue+"  y �� ���ٶ�_"+yValue+"  z �� ���ٶ�_"+zValue);
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
			//�жϵ�ǰ�������ǵشŴ��������Ǽ��ٶȴ�����
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				//��ֵʱҪ���� clone ���� ��Ȼ����valuse����ָ��ͬһ������
				accelerometerValues = event.values.clone();
			}else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magneticValues = event.values.clone();
			}
			
			float[] R = new float[9];
			float[] values = new float[3];
			//���� getRotationMatrix Ϊ R ��ֵ
			SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
			//�ٵ��� getOrientation Ϊ values ��ֵ
			SensorManager.getOrientation(R, values);
			//��ʱ values �Ѱ������з�������ת�Ļ����� �� ���� values[0] ��ʾ�ֻ�Χ��z����ת�Ļ��� 
			//����Math.getDegrees()����ת���ɽǶ�
			float rotateDegree = -(float) Math.toDegrees(values[0]);
			if (Math.abs(rotateDegree - lastRotateDegree)>1) {
				tv.setText("Χ�� z ����ת�Ļ���  : "+values[0]);
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
