package com.asen.demo;

import android.test.AndroidTestCase;
import android.util.Log;

public class MyTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Log.i("haha", "setUp");
	}
	public void testMethodOne(){
		assertEquals(3, 1+2);
	}
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Log.i("haha", "tearDown");
	}
	
}
