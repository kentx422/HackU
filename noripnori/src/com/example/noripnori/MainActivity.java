package com.example.noripnori;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import com.nifty.cloud.mb.*;

import android.app.AlertDialog;

public class MainActivity extends Activity {
	//グローバルにしておくことでPostData()に引数を使わなくて良くしてる
	String str1,str2,str3,iD= "ini";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //アプリごとのAPIキーとクライアントキーを変える
        NCMB.initialize(this,  "2728a73fab160c77dfc7945f07692799197fa56cc2d3a3955b6d88488bb7d520", "24f64f2bac7a749bc16e31ef8e8f011df32325a645d11b983ec7e42ba15053af");

        //-------------------------
        Button btn = (Button) findViewById(R.id.Button);
        btn.setOnClickListener(new View.OnClickListener() {
             
        	//ボタンをクリックしたときの処理？
            @Override
            public void onClick(View v) {
                //上段のテキストフィールドの文字を読み取りグローバル変数に格納
                EditText editText = (EditText) findViewById(R.id.editText1);
                String text = editText.getText().toString();
                str1 = text;
               
                //下段のテキストフィールドの文字を読み取りグローバル変数に格納
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                String text2 = editText2.getText().toString();
                str2 = text2;
                
               
                
                //テキストフィールドの文字をサーバ側に送る
                PostData();
                
                //TextClassのデータを取得する
                GetData();
                
                
                
//                NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("TestClass");
//                query.findInBackground(new FindCallback<NCMBObject>() {
//                    @Override
//                    public void done(List<NCMBObject> result, NCMBException e){
//                        if (result.isEmpty() != true){
//                            dispMessage(result.get(0));
//                        } else {
//                            PostData();
//                        }
//                    }
//                });
            }
        });
    }
    
    private void PostData(){
        NCMBObject TestData = new NCMBObject("TestData");
        TestData.put("message", ""+str1+"");
        TestData.put("comment", ""+str2+"");
        String did = TestData.getObjectId();
        try {
			TestData.save();
		} catch (NCMBException e) {}
        TestData.saveInBackground();
      //最上段の文字列の値を変化
    	TextView textView = (TextView) findViewById(R.id.textView1);
        textView.setText(""+did+"");
    }
    
    private void GetData(){
    	NCMBQuery<NCMBObject> query = NCMBQuery.getQuery("TestData");
    	//"Ft3Y1axUMMmfP56X"はObjectID
    	query.getInBackground("DbwvpawMyISkJSQC", new GetCallback<NCMBObject>() {
    	    @Override
    	    public void done(NCMBObject TestClass, NCMBException e) {
    	        if (e == null) {
    	            // 成功
    	            
       	            //str3 = TestClass.getString("message");
       	            
//	       	         String objectId = gameScore.getObjectId();
//	                 Date updatedAt = gameScore.getUpdatedAt();
//	                 Date createdAt = gameScore.getCreatedAt();
    	        } else {
    	            // エラー
    	        	str3 = "error";
    	        }
    	    }
    	});
    }
 
    
    private void dispMessage(NCMBObject message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        
        alertDialogBuilder.setTitle("データ取得");
        alertDialogBuilder.setMessage(message.getString("message"));
        alertDialogBuilder.show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
   

}