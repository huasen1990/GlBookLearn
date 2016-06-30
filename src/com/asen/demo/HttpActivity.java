package com.asen.demo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Entity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HttpActivity extends Activity implements OnClickListener {

	private Button bt1;
	private TextView tv;
	private String content;

	private Button bt2, bt3,bt4,bt5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.http);
		bt1 = (Button) findViewById(R.id.bt);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		bt4 = (Button) findViewById(R.id.bt4);
		bt5 = (Button) findViewById(R.id.bt5);
		tv = (TextView) findViewById(R.id.tv);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt:
			// startHttpUrlConnection();
			startHttpClientByGet();
			break;
		case R.id.bt2:
			startParsePull(xmlData);
			break;
		case R.id.bt3:
			startParseSax(xmlData);
			break;
		case R.id.bt4:
			startParseJSONObect(jsonData);
			break;
		case R.id.bt5:
			startParseGSON(jsonData);
			break;
		default:
			break;
		}
	}
	
	String jsonData = "[{\"id\":\"1\",\"name\":\"Android\"},{\"id\":\"2\",\"name\":\"IOS\"}]";

	private void startParseGSON(String jsonData) {
		Gson gson = new Gson();
		
		List<Language> list = gson.fromJson(jsonData, new TypeToken<List<HttpActivity.Language>>(){}.getType());
		
		if (list!=null) {
			for (Language language : list) {
				Log.i("haha","id : "+language.getId());
				Log.i("haha","name : "+language.getName());
			}
		}
	}
	class Language{
		private int id;
		private String name;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
	private void startParseJSONObect(String jsonData) {
		Log.i("haha", jsonData);
		try {
			JSONArray arr = new JSONArray(jsonData);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonObject = arr.getJSONObject(i);
				String id  = jsonObject.getString("id");
				String name = jsonObject.getString("name");
				Log.i("haha", "id : "+id);
				Log.i("haha", "name : "+name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("haha", e.getMessage().toString());
		}
	}

	
	String xmlData = "<apps><app><id>1</id><name>China</name></app><app><id>2</id><name>Japan</name></app><app><id>3</id><name>American</name></app></apps>";

	private void startParseSax(String xmlData) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
//			SAXParser parser = factory.newSAXParser();
//			XMLReader reader = parser.getXMLReader();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			SaxHandler handler = new SaxHandler();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(new StringReader(xmlData)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class SaxHandler extends DefaultHandler {

		private String nodeName;
		private StringBuilder id;
		private StringBuilder name;
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			id = new StringBuilder();
			name = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			nodeName = localName;//记录当前节点名
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			if ("id".equals(nodeName)) {
				id.append(ch, start, length);
			}else if ("name".equals(nodeName)) {
				name.append(ch, start, length);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if ("app".equals(localName)) {
				Log.i("haha", "id : "+id.toString());
				Log.i("haha", "name : "+name.toString());
				//最后将StringBuilder 清空掉
				id.setLength(0);
				name.setLength(0);
			}
		}

		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}

	}

	private void startParsePull(String xmlData) {

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();

			parser.setInput(new StringReader(xmlData));

			String id = "";
			String name = "";

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if ("id".equals(nodeName)) {
						id = parser.nextText();
					} else if ("name".equals(nodeName)) {
						name = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if ("app".equals(nodeName)) {
						Log.i("haha", "id : " + id);
						Log.i("haha", "name : " + name);
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void startHttpClientByPost() {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://xxx.xxx.xxx");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", "admin"));
		params.add(new BasicNameValuePair("password", "123456"));
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
					"utf-8");
			client.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startHttpClientByGet() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet("http://www.baidu.com");
				try {
					HttpResponse response = client.execute(get);
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						content = EntityUtils.toString(entity, "utf-8");
						Log.i("haha", content);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void startHttpUrlConnectionByPost() {
		HttpURLConnection conn = null;
		try {
			URL url = new URL("");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes("username=admin&password=123456");

			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			Log.i("haha", response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

	}

	private void startHttpUrlConnection() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				HttpURLConnection conn = null;
				try {
					URL url = new URL("http://www.baidu.com");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(8000);
					InputStream in = conn.getInputStream();

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					content = response.toString();
					if (TextUtils.isEmpty(content)) {
						return;
					}
					Log.i("haha", content);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							tv.setText(content);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					Log.i("haha", e.getMessage().toString());
				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}

			}
		}).start();
	}
}
