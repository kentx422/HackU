package com.example.norinoripp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.*;

import com.nifty.cloud.mb.*;

import android.app.AlertDialog;

public class MainActivity extends Activity {
	 
    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword;
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        mUserName = (EditText)findViewById(R.id.UserName);
        mEmail = (EditText) findViewById(R.id.MailAddress);
        mPassword = (EditText) findViewById(R.id.Password);
         
        NCMB.initialize(getApplication(), "2728a73fab160c77dfc7945f07692799197fa56cc2d3a3955b6d88488bb7d520", "24f64f2bac7a749bc16e31ef8e8f011df32325a645d11b983ec7e42ba15053af");
        
        Button btn = (Button) findViewById(R.id.Button);
        btn.setOnClickListener(new View.OnClickListener() {
         
            @Override
            public void onClick(View v) {
         
                // ここに Sign up 処理だよー
         
                NCMBUser user = new NCMBUser();
                user.setUsername(mUserName.getText().toString());
                user.setEmail(mEmail.getText().toString());
                user.setPassword(mPassword.getText().toString());
         
                // 既定値以外のフィールドに値を設定
                // user.put("phone", "123-456-7890");
                 
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(NCMBException e) {
         
                        if (e == null) {
                            // Sign up 成功！
                            Toast.makeText(getApplication(), "成功！", Toast.LENGTH_SHORT).show();
         
                        } else {
                            // Sign up 失敗！
                            Toast.makeText(getApplication(), "失敗！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
         
         
    }
    
 
}

