package com.example.hackuapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//アセットからTypeface作成
	    Typeface typeface = Typeface.createFromAsset(getAssets(), "manteka.ttf");
	    //FONTをTextViewに設定
		TextView txt = (TextView)findViewById(R.id.touchtxt);
		if(typeface != null && txt != null) txt.setTypeface(typeface);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
       return true;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
