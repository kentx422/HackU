package com.example.hackuapp;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.FindCallback;
import com.nifty.cloud.mb.GetCallback;
import com.nifty.cloud.mb.NCMBException;
import com.nifty.cloud.mb.NCMBObject;
import com.nifty.cloud.mb.NCMBQuery;

public class OrderActivity extends Activity {
	String ObjectID;
    String[] oData = new String[10];
    int geolat = 0, geolng=0;
	String UName="none";
    String state;
    ImageButton odbtn;
    String passNum;
    private Dialog varDialog;

    Typeface typeface;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		// 現在のintentを取得する
		Intent intent = getIntent();

		// intentから指定キーの文字列を取得する
//		mNum = intent.getStringExtra("missionNumber").toString();
		geolat = intent.getIntExtra("geoLat", 0);
		geolng = intent.getIntExtra("geoLng", 0);
		System.out.println(geolat);

		getData(geolat);
	    UName = (String)getIntent().getSerializableExtra("userName");

	    //アセットからTypeface作成
	    typeface = Typeface.createFromAsset(getAssets(), "manteka.ttf");

	    //FONTをTextViewに設定
	    TextView uP = (TextView)findViewById(R.id.uPoint);
	    if(typeface != null && uP != null) uP.setTypeface(typeface);

	    TextView uN = (TextView)findViewById(R.id.uName);
	    uN.setText(UName);
	    if(typeface != null && uN != null) uN.setTypeface(typeface);

	    TextView cN = (TextView)findViewById(R.id.cName);
	    cN.setText(oData[3]);
	    if(typeface != null && cN != null) cN.setTypeface(typeface);

	    odbtn = (ImageButton)findViewById(R.id.acbtn);
		odbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(oData[3].equals(UName) && oData[7].equals("0")){
				}else if(oData[3].equals(UName)&& oData[7].equals("1")){
					dialogOutput();
				}else if(oData[7].equals("0")){
					oData[7] ="1";
					renewData();
					dialogInput();
				}else if(oData[7].equals("1")){
				}else if(oData[7].equals("2")){}
			}
		});
	}

	 public void dialogOutput() {

		   // Dialogクラスのインスタンスを生成
		   varDialog = new Dialog(OrderActivity.this);

		   // ダイアログのインスタンスに設定済みのビューを追加
		   varDialog.setContentView(R.layout.dialog_output);

		   //画面外をタッチした時にキャンセルできるかどうか
		   varDialog.setCancelable(false);
		   String title = "Mission Complete";

		   // ダイアログのタイトルを設定
		   varDialog.setTitle(title);

	       varDialog.show();

	       TextView cT = (TextView)varDialog.findViewById(R.id.compTxt);
		    if(typeface != null && cT != null) cT.setTypeface(typeface);
		   TextView oT = (TextView)varDialog.findViewById(R.id.outputTxt);
		   if(typeface != null && oT != null) oT.setTypeface(typeface);

		   oT.setText("Pass : " + passNum);


		   // ダイアログ上のボタンのインスタンスを取得して
		   // イベントリスナーを登録
	       varDialog.findViewById(R.id.okbtn)
	       .setOnClickListener(
			   new OnClickListener() {
				   // ダイアログボタンのクリック時に呼ばれるコールバックメソッド
				   public void onClick(View v) {
					   // ダイアログを画面上から消去するメソッド
					   varDialog.dismiss();
					   Intent pIntent = new Intent(OrderActivity.this, MapPostActivity.class);
					   pIntent.putExtra("userName", UName);
					   startActivity(pIntent);
					   oData[7]="2";
						renewData();
					   Toast.makeText(getApplicationContext(), "Smile!!", Toast.LENGTH_SHORT).show();
				   }
	           }
	    	);
	     }
	 public void dialogInput() {

		   // Dialogクラスのインスタンスを生成
		   varDialog = new Dialog(OrderActivity.this);

		   // ダイアログのインスタンスに設定済みのビューを追加
		   varDialog.setContentView(R.layout.dialog_input);
		   
		   //画面外をタッチした時にキャンセルできるかどうか
		   varDialog.setCancelable(false);
		   
		   String title = "Mission Running";

		   // ダイアログのタイトルを設定
		   varDialog.setTitle(title);

	       varDialog.show();

	       TextView t1 = (TextView)varDialog.findViewById(R.id.text1);
		    if(typeface != null && t1 != null) t1.setTypeface(typeface);
		   // ダイアログ上のボタンのインスタンスを取得して
		   // イベントリスナーを登録
	       varDialog.findViewById(R.id.donebtn)
	       .setOnClickListener(
			   new OnClickListener() {
				   // ダイアログボタンのクリック時に呼ばれるコールバックメソッド
				   public void onClick(View v) {
					   EditText editText1 = (EditText)varDialog.findViewById(R.id.editText1);
					   System.out.println(passNum);
					   SpannableStringBuilder sb = 
							   (SpannableStringBuilder)editText1.getText();
					   String inputPass = sb.toString();
					   System.out.println(inputPass);
					   if(inputPass.equals(passNum)){
						   // ダイアログを画面上から消去するメソッド
						   varDialog.dismiss();
						   Intent pIntent = new Intent(OrderActivity.this, MapPostActivity.class);
						   pIntent.putExtra("userName", UName);
						   startActivity(pIntent);
						   oData[7]="2";
						   renewData();
						   Toast.makeText(getApplicationContext(), "Complete!!", Toast.LENGTH_SHORT).show();
					   }else{
						   Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_SHORT).show();
					   }

				   }
	           }
	    	);
	       varDialog.findViewById(R.id.cancelbtn)
	       .setOnClickListener(
			   new OnClickListener() {
				   // ダイアログボタンのクリック時に呼ばれるコールバックメソッド
				   public void onClick(View v) {
					   // ダイアログを画面上から消去するメソッド
					   varDialog.dismiss();
					   Toast.makeText(getApplicationContext(), "Mission Cancel", Toast.LENGTH_SHORT).show();
					   oData[7]="0";
					   renewData();
					   // エディットテキストへの出力
					   
					  // varResultText.setText("OKボタンがクリックされました。");
				   }
	           }
	    	);
	     }

	private void getData(int geoLat) {
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("Mission");
		//missionNumber を検索
		query.whereEqualTo("LAT", geoLat + "");
		query.findInBackground(new FindCallback<NCMBObject>(){
			@Override
			public void done(List<NCMBObject> list, NCMBException e) {
				if(e == null){
					for(int i = 0; i<list.size(); i++){
						ObjectID = list.get(i).getObjectId();
						getEtcData();
					}
				}
			}


		});
	}

	private void getEtcData() {
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("Mission");
		//件名などを取得
		query.getInBackground(ObjectID, new GetCallback<NCMBObject>() {
	         @Override
	         public void done(NCMBObject Mission, NCMBException e) {
	             if (e == null) {

					// 成功
	                  oData[0] = Mission.getString("subject").toString();
//	                  oData[1] = Mission.getString("LAT").toString();
//	                  oData[2] = Mission.getString("LNG").toString();
	                  oData[3] = Mission.getString("client".toString());
	                  oData[4] = Mission.getString("point".toString());
	                  oData[5] = Mission.getString("text".toString());
	                  oData[6] = Mission.getString("missionNumber".toString());
	                  oData[7] = Mission.getString("state".toString());
	                  oData[8] = Mission.getString("date".toString());
	                  oData[9] = Mission.getString("numPass".toString());
	                  //System.out.println(oData[0]);
	                  setText();
	             } else {
	                 // エラー
	              String str3 = "error";
	              System.out.println(str3);
	             }
	         }
	     });
	}

	public void renewData(){
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("Mission");
		//件名などを取得
		System.out.println(ObjectID);
		query.getInBackground(ObjectID, new GetCallback<NCMBObject>() {
	         @Override
	         public void done(NCMBObject Mission, NCMBException e) {
	             if (e == null) {
	            	 Mission.put("state", oData[7]);
	            	 try {
						Mission.save();
					} catch (NCMBException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
	             } else {
	                 // エラー
	              String str3 = "error";
	              System.out.println(str3);
	             }
	         }
	     });
	}

	private void setText() {
		TextView subText = (TextView)findViewById(R.id.subject);
		subText.setText(oData[0]);

		TextView bodyText = (TextView)findViewById(R.id.body);
		bodyText.setText(oData[5]);

		TextView cName = (TextView)findViewById(R.id.cName);
		cName.setText(oData[3]);

//		TextView cPoint = (TextView)findViewById(R.id.cPoint);
//		cPoint.setText(oData[4]);

		TextView timeText = (TextView)findViewById(R.id.timetxt);
		timeText.setText(oData[8]);

		passNum =oData[9];
		ImageButton ib = (ImageButton)findViewById(R.id.acbtn);
		Drawable abt = getResources().getDrawable(R.drawable.accepted);
		Drawable rbt = getResources().getDrawable(R.drawable.ran);
		Drawable cbt = getResources().getDrawable(R.drawable.comp);
		Drawable wbt = getResources().getDrawable(R.drawable.wait);

		if(oData[3].equals(UName) && oData[7].equals("0")){
			ib.setBackgroundDrawable(wbt);
		}else if(oData[3].equals(UName)&& oData[7].equals("1")){
			ib.setBackgroundDrawable(cbt);
		}else if(oData[7].equals("0")){
			ib.setBackgroundDrawable(abt);
		}else if(oData[7].equals("1")){
			ib.invalidate();
			ib.setBackgroundDrawable(rbt);
		}else if(oData[7].equals("2")){
			ib.setBackgroundDrawable(cbt);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order, menu);
		return true;
	}

}
