package com.asen.demo;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity implements OnClickListener {

	private Button bt1, bt2;
	private TextView tv;
	private LocationManager locationManager;
	private String mProvider;
	private String addressInfo;
	private Location mLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);

		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		tv = (TextView) findViewById(R.id.tv);

		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//		List<String> providerList = locationManager.getProviders(true);
//
//		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
//			provider = LocationManager.GPS_PROVIDER;
//		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
//			provider = LocationManager.NETWORK_PROVIDER;
//		} else if (providerList.contains(LocationManager.PASSIVE_PROVIDER)) {
//			provider = LocationManager.PASSIVE_PROVIDER;
//		} else {
//			Toast.makeText(this, "No location provider to use",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		mLocation = locationManager.getLastKnownLocation(provider);
		getBestValidLocation();
		showLocationInfo(mLocation);
		locationManager.requestLocationUpdates(mProvider, 5000, 1, listener);

	}

	private void getBestValidLocation(){
		Location bestLocation = null;
		List<String> providers = locationManager.getProviders(true);
		for (String provide : providers) {
			Location l = locationManager.getLastKnownLocation(provide);
			if (l==null) {
				continue;
			}
			if (bestLocation==null||bestLocation.getAccuracy()<l.getAccuracy()) {
				bestLocation = l;
				mProvider = provide;
			}
		}
		mLocation  = bestLocation;
	}
	private void showLocationInfo(Location location) {
		if (location != null) {
			double latitude = location.getLatitude();// 纬度
			double longitude = location.getLongitude();// 经度
			double altitude = location.getAltitude();// 海拔
			String locationInfo = "所在位置\n纬度 : " + latitude + "\n经度 : "
					+ longitude + "\n海拔 : " + altitude;
			tv.setText(locationInfo);
		}else {
			Log.i("haha", "location  is null");
		}
	}

	private void geocodeLocation(final Location location) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				StringBuilder url = new StringBuilder();
				url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
				url.append(
						location.getLatitude() + "," + location.getLongitude())
						.append("&sensor=false");
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url.toString());
				Log.i("haha", url.toString());

				get.addHeader("Accept-Language", "zh-CN");
				HttpResponse response;
				try {
					response = client.execute(get);
					if (response.getStatusLine().getStatusCode() == 200) {
						Log.i("haha", "请求成功  状态码 200");

						HttpEntity entity = response.getEntity();
						String result = EntityUtils.toString(entity, "utf-8");
						JSONObject jsonObject = new JSONObject(result);
						JSONArray arr = jsonObject.getJSONArray("results");
						if (arr.length() > 0) {
							Log.i("haha",
									"请求成功  有数据 arr.length : " + arr.length());
							JSONObject object = arr.getJSONObject(0);
							addressInfo = object.getString("formatted_address");
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									tv.setText(addressInfo);
								}
							});
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationManager != null) {
			locationManager.removeUpdates(listener);
		}
	}

	private LocationListener listener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			Toast.makeText(LocationActivity.this, "reLocating ---",
					Toast.LENGTH_SHORT).show();
			mLocation = location;
			showLocationInfo(location);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt1:
			startGetLocation();
			break;
		case R.id.bt2:
			stopLocation();
			break;

		default:
			break;
		}
	}

	private void startGetLocation() {
		geocodeLocation(mLocation);

	}

	private void stopLocation() {

	}
}
