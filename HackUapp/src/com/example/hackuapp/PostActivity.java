package com.example.hackuapp;

import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.NCMBException;
import com.nifty.cloud.mb.NCMBObject;
import com.nifty.cloud.mb.NCMBQuery;

public class PostActivity extends Activity implements LocationListener {
	public Double lat, lng;
	public Location location;
	String kennmei =null;
	String honnbun = null;
	String person = null;
	String missionNum = null;
	String strLat, strLng;
	int point = 10;
	String UName;
	String state ="0";
	String date;
	String passNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		final EditText subtext = (EditText)findViewById(R.id.subtxt);
		final EditText bodytext = (EditText)findViewById(R.id.bodytxt);

		// 現在のintentを取得する
		Intent intent = getIntent();

		// intentから指定キーの文字列を取得する
//					mNum = intent.getStringExtra("missionNumber").toString();
		int setLat = intent.getIntExtra("setLat", 0);
		int setLng = intent.getIntExtra("setLng", 0);
		strLat = Integer.toString(setLat);
		strLng = Integer.toString(setLng);
		System.out.println(setLat);


	    UName = (String)getIntent().getSerializableExtra("userName");

	    //アセットからTypeface作成
	    Typeface typeface = Typeface.createFromAsset(getAssets(), "manteka.ttf");

	    //FONTをTextViewに設定
	    TextView uP = (TextView)findViewById(R.id.uPoint);
	    if(typeface != null && uP != null) uP.setTypeface(typeface);

	    TextView uN = (TextView)findViewById(R.id.uName);
	    uN.setText(UName);
	    if(typeface != null && uN != null) uN.setTypeface(typeface);

		ImageButton postbtn = (ImageButton)findViewById(R.id.postbtn);
		postbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					Intent pIntent = new Intent(PostActivity.this, MapPostActivity.class);
					pIntent.putExtra("userName", UName);
					startActivity(pIntent);

					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);        //(2)現在の年を取得
				    int month = cal.get(Calendar.MONTH) + 1;  //(3)現在の月を取得
				    int day = cal.get(Calendar.DATE);         //(4)現在の日を取得
				    int hour = cal.get(Calendar.HOUR_OF_DAY); //(5)現在の時を取得
				    int minute = cal.get(Calendar.MINUTE);  //(6)現在の分を取得

				    date = year + "/" + month + "/" + day + " " + hour + ":" + minute;

				    Random rnd = new Random();
				    passNum = Integer.toString(rnd.nextInt(10000));
				if(subtext.getText() == null){
					Toast.makeText(getApplication(),  "だめっす", Toast.LENGTH_SHORT).show();
				}else{
					kennmei = subtext.getText().toString();
					honnbun = bodytext.getText().toString();;
					String numMission = null;
					try {
						numMission = getMissionNumber();
					} catch (NCMBException e) {
						e.printStackTrace();
					}
					postData(kennmei, honnbun, person, numMission);
				}
			}
		});

	}

	public String getMissionNumber() throws NCMBException{
		String num = null;
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("Mission");
		int numMission = query.count();
//		query.countInBackground(new CountCallback(){
//			@Override
//			public void done(int count, NCMBException e) {
//				System.out.println(count);
//				if(e == null){
//					missionNum = Integer.toString(count+1);
//					System.out.println(missionNum);
//				}
//			}
//		});
		//System.out.println(numMission);
		num = Integer.toString(numMission+1);
		return num;
	}

	public void postData(String subject, String text, String client, String missionNumber){
		NCMBObject Mission = new NCMBObject("Mission");
		Mission.put("subject", subject);
		Mission.put("text", text);
		Mission.put("client", UName);
		Mission.put("missionNumber", missionNumber);
		Mission.put("LAT", strLat);
		Mission.put("LNG", strLng);
		Mission.put("point", point);
		Mission.put("state", state);
		Mission.put("date", date);
		Mission.put("numPass", passNum);
		Mission.saveInBackground();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
