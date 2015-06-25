package com.example.hackuapp;

import java.util.List;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapActivity;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.MapView.MapTouchListener;
import jp.co.yahoo.android.maps.MyLocationOverlay;
import jp.co.yahoo.android.maps.OverlayItem;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.PopupOverlay;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.FindCallback;
import com.nifty.cloud.mb.GetCallback;
import com.nifty.cloud.mb.NCMBException;
import com.nifty.cloud.mb.NCMBObject;
import com.nifty.cloud.mb.NCMBQuery;

public class MapPostActivity extends MapActivity implements LocationListener {
	private MapView MapView;//MapViewメンバー
	GeoPoint p;
	private MyLocationOverlay _overlay;
	String UName;
	String ObjectID;

	String kenmei = "dameyo-";
	String getLat = "135770611";
	String getLng = "34801480";

	int setLat = 135770611;
	int setLng = 34801480;

	int mNum;
	String pData[][] = new String[100][100];

	public PinOverlay mPin ;
	/*
	 * pData[0] = 件名
	 * pData[1] = Lat
	 * pdata[2] = Lng
	 * pData[3] = 投稿者
	 * pData[4]
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_map_post);

	      UName = (String)getIntent().getSerializableExtra("userName");

	    //アセットからTypeface作成
	      Typeface typeface = Typeface.createFromAsset(getAssets(), "manteka.ttf");
	      //FONTをTextViewに設定
	      TextView uP = (TextView)findViewById(R.id.uPoint);
	      if(typeface != null && uP != null) uP.setTypeface(typeface);

	      TextView uN = (TextView)findViewById(R.id.uName);
	      uN.setText(UName);
	      if(typeface != null && uN != null) uN.setTypeface(typeface);


	      //データ取得
	     // for(int i=1; i<6; i++) getData(i);
	      getAllData();

	      MapView=new MapView(this,"dj0zaiZpPWEwdE9wZm1HR3VhNSZzPWNvbnN1bWVyc2VjcmV0Jng9NWM-");
	      MapView.setBuiltInZoomControls(true);
	      MapView.setLongPress(true);
	      MapView.setMapTouchListener(new MapTouchListener(){
			@Override
			public boolean onLongPress(jp.co.yahoo.android.maps.MapView arg0, Object arg1, PinOverlay arg2,
					GeoPoint arg3) {
				MapView.getOverlays().remove(arg2);
				if(mPin != null){
					MapView.getOverlays().remove(mPin);
				}
				Drawable pinicon = getResources().getDrawable(R.drawable.msos);
				mPin = new PinOverlay(pinicon);

				MapView.getOverlays().add(mPin);
				mPin.addPoint(arg3,null);
				setLat = arg3.getLatitudeE6();
				setLng = arg3.getLongitudeE6();
				System.out.println(setLat);

				return false;
			}

			@Override
			public boolean onPinchIn(jp.co.yahoo.android.maps.MapView arg0) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}

			@Override
			public boolean onPinchOut(jp.co.yahoo.android.maps.MapView arg0) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}

			@Override
			public boolean onTouch(jp.co.yahoo.android.maps.MapView arg0, MotionEvent arg1) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}
	      });
	      MapController c = MapView.getMapController();

	  	//MyLocationOverlayのサブクラスであるSubMyLocationOverlayインスタンスを作成
	  	    _overlay = new SubMyLocationOverlay(getApplicationContext(), MapView, this);

	  	    //現在位置取得開始
	  	    _overlay.enableMyLocation();

	  	    //MapViewにMyLocationOverlayを追加。
	  	    MapView.getOverlays().add(_overlay);
	  	    MapView.invalidate();


		   ImageButton postbtn = (ImageButton)findViewById(R.id.postbutton);
		   postbtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
						if (mPin != null){
							Intent pIntent = new Intent(MapPostActivity.this, PostActivity.class);
							pIntent.putExtra("setLng", setLng);
				    		pIntent.putExtra("setLat", setLat);
				    		pIntent.putExtra("userName", UName);
							startActivity(pIntent);
						}
						else{
							 Toast.makeText(getApplication(),  "位置を指定してください", Toast.LENGTH_SHORT).show();
						}
				}
			});

		   ImageButton updatebtn = (ImageButton)findViewById(R.id.updatebutton);
		   updatebtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent uIntent = new Intent(MapPostActivity.this, MapPostActivity.class);
			        uIntent.putExtra("userName", UName);
			        startActivity(uIntent);
				}
			});
		   ImageButton localbtn = (ImageButton)findViewById(R.id.localbutton);
	      localbtn.setOnClickListener(new View.OnClickListener() {
			@Override
				public void onClick(View v) {
				if(p != null){
					  MapView.getMapController().animateTo(p);
				}

				}
	      });

	      c.setCenter(new GeoPoint(34801480, 135770611)); //初期表示の地図を指定
	      c.setZoom(1); 			       	      //初期表示の縮尺を指定


	      FrameLayout mainLayout = (FrameLayout)findViewById(R.id.mapContainer);
	      mainLayout.addView(MapView);


	   }
	public class SubMyLocationOverlay extends MyLocationOverlay {
	    MapView _mapView = null;
	    Activity _activity = null;

	    public SubMyLocationOverlay(Context context, MapView mapView, Activity activity) {
	        super(context, mapView);

	        _mapView = mapView;
	        _activity = activity;
	    }

	    //現在地更新のリスナーイベント
	    @Override
	    public void onLocationChanged(android.location.Location location) {
	        super.onLocationChanged(location);

	        if (_mapView.getMapController() != null) {
	            //位置が更新されると地図の位置も変える。
	            p = new GeoPoint((int) (location.getLatitude() * 1E6),(int) (location.getLongitude() * 1E6));
	            //_mapView.getMapController().animateTo(p);
	            _mapView.invalidate();
	        }
	    }
	}

	public void pinCreate(int mNum){
		 //ピン設置
	      GeoPoint mid = new GeoPoint(Integer.valueOf(pData[mNum][1]), Integer.valueOf(pData[mNum][2]));
	      Drawable star = null;
	      System.out.println(pData[mNum][7]);
	      if(pData[mNum][3].equals(UName) && Integer.parseInt(pData[mNum][7]) == 0){
	    	 star = getResources().getDrawable(R.drawable.msos2);
	      }else if(pData[mNum][3].equals(UName) && Integer.parseInt(pData[mNum][7]) == 1){
	    	  star = getResources().getDrawable(R.drawable.sosrx);
	      }else if(Integer.parseInt(pData[mNum][7]) == 0){
	    	  star = getResources().getDrawable(R.drawable.osos);
	      }else if(Integer.parseInt(pData[mNum][7]) == 1){
	    	  star = getResources().getDrawable(R.drawable.sosbx);
	      }else if(Integer.parseInt(pData[mNum][7]) == 2){
	    	  star = getResources().getDrawable(R.drawable.smile);
	      }
	      PinOverlay pinOverlay = new PinOverlay(star);
	      MapView.getOverlays().add(pinOverlay);
	      pinOverlay.addPoint(mid, "件名:"+pData[mNum][0], "件名について");

	      //ポップアップ
	      PopupOverlay popupOverlay = new PopupOverlay(){
	    	  @Override
	    	  public void onTap(OverlayItem item){
	    		  Intent oIntent = new Intent(MapPostActivity.this, OrderActivity.class);
//	    		  oIntent.putExtra("subject", pData[mNum][0].toString());
//	    		  oIntent.putExtra("client", pData[mNum][3]);
//	    		  oIntent.putExtra("point", pData[mNum][4]);
//	    		  oIntent.putExtra("body", pData[mNum][5]);
	    		  item.getPoint();
	    		  oIntent.putExtra("geoLng", item.getPoint().getLatitudeE6());
	    		  oIntent.putExtra("geoLat", item.getPoint().getLatitudeE6());
	    		  oIntent.putExtra("userName", UName);
////	    		  oIntent.putExtra("oData", pData[mNum]);
	   // 		  System.out.println(item.getPoint().getLatitudeE6());

					startActivity(oIntent);
	    	  }
	      };

	      MapView.getOverlays().add(popupOverlay);
	      pinOverlay.setOnFocusChangeListener(popupOverlay);
	}
	public void getAllData(){
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("Mission");
		//missionNumber を検索
		//query.whereEqualTo("missionNumber", mNum + "");
		query.whereNotEqualTo("missionNumber", "0");
		query.findInBackground(new FindCallback<NCMBObject>(){
			@Override
			public void done(List<NCMBObject> list, NCMBException e) {
				if(e == null){
					for(int i = 0; i < list.size(); i++){
						ObjectID = list.get(i).getObjectId();
						//System.out.println(ObjectID);
						getEtcAllData(i);
					}
				}
			}
		});
	}

	public void getEtcAllData(final int misNum){
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("Mission");
		//件名などを取得
		query.getInBackground(ObjectID, new GetCallback<NCMBObject>() {
	         @Override
	         public void done(NCMBObject Mission, NCMBException e) {
	             if (e == null) {
	                 // 成功
	                  pData[misNum][0] = Mission.getString("subject").toString();
	                  pData[misNum][1] = Mission.getString("LAT").toString();
	                  pData[misNum][2] = Mission.getString("LNG").toString();
	                  pData[misNum][3] = Mission.getString("client".toString());
//	                  pData[misNum][4] = Mission.getString("point".toString());
//	                  pData[misNum][5] = Mission.getString("text".toString());
	                  pData[misNum][6] = Mission.getString("missionNumber".toString());
	                  pData[misNum][7] = Mission.getString("state".toString());
	       //           System.out.println(pData[mNum][0]);
	                  pinCreate(misNum);
	             } else {
	                 // エラー
	              String str3 = "error";
	              System.out.println(str3);
	             }
	         }
	     });
	}

	public void getData(int mNum){
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("Mission");
		//missionNumber を検索
		query.whereEqualTo("missionNumber", mNum + "");
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

	public void getEtcData(){
		NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("Mission");
		//件名などを取得
		query.getInBackground(ObjectID, new GetCallback<NCMBObject>() {
	         @Override
	         public void done(NCMBObject Mission, NCMBException e) {
	             if (e == null) {
	                 // 成功
	                  pData[mNum][0] = Mission.getString("subject").toString();
	                  pData[mNum][1] = Mission.getString("LAT").toString();
	                  pData[mNum][2] = Mission.getString("LNG").toString();
	                  pData[mNum][3] = Mission.getString("client".toString());
	                  pData[mNum][4] = Mission.getString("point".toString());
	                  pData[mNum][5] = Mission.getString("text".toString());
	                  pData[mNum][6] = Mission.getString("missionNumber".toString());
	       //           System.out.println(pData[mNum][0]);
	                  pinCreate(mNum);
	             } else {
	                 // エラー
	              String str3 = "error";
	              System.out.println(str3);
	             }
	         }
	     });
	}

	//@Override
	protected boolean isRouteDisplayed() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		//setLat = Double.toString(location.getLatitude());
		Double.toString(location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
