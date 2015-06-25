package com.example.hackuapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nifty.cloud.mb.LogInCallback;
import com.nifty.cloud.mb.NCMB;
import com.nifty.cloud.mb.NCMBException;
import com.nifty.cloud.mb.NCMBUser;

public class LoginActivity extends Activity {

//    private EditText mUserName;
//    private EditText mEmail;
//    private EditText mPassword;

    private EditText mLoginUserName;
    private EditText mLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NCMB.initialize(getApplication(), "d320b684493141c449071ff46e6af35fdd2cc9edb69f2536ea9d5c7c850fcd92",
        		"033d264bc6c10783b8b5011617f31225722ac8da2e9f7805768aff10cde74b3d");

        mLoginUserName = (EditText) findViewById(R.id.login_UserName);
        mLoginPassword = (EditText) findViewById(R.id.login_Password);

        ImageButton loginBtn = (ImageButton) findViewById(R.id.login_Button);

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // ここに Login 処理
                final String userName = mLoginUserName.getText().toString();
                String userPassword = mLoginPassword.getText().toString();

                // ログイン
                NCMBUser.logInInBackground(userName, userPassword, new LogInCallback() {
                    @Override
                    public void done(NCMBUser user, NCMBException e) {
                        if (e == null) {
                            Toast.makeText(getApplication(),  "ログイン成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MapPostActivity.class);
                            intent.putExtra("userName", userName);
                            startActivity(intent);
                        } else {

                            Toast.makeText(getApplication(),  "ログイン失敗！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }


}
