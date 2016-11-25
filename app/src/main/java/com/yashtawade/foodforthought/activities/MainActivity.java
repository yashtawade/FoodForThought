package com.yashtawade.foodforthought.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.constants.FFTConstant;
import com.yashtawade.foodforthought.models.DataParse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public ProgressDialog dialog;
    private boolean isLogOut;

    private String email;
    private String password;

    BaseHttpRequestCallback mCallback = new BaseHttpRequestCallback(){
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers){
            dialog.dismiss();
            DataParse dp = JSON.parseObject(response, DataParse.class);
            if(dp.getError() == 1){
                Toast.makeText(MainActivity.this, "Wrong email or password!", Toast.LENGTH_SHORT).show();
            }else{
                if(Integer.valueOf(dp.getData()) == 0){
                    Toast.makeText(MainActivity.this, "Email does not exist!", Toast.LENGTH_SHORT).show();
                }else{
                    int uid = Integer.valueOf(dp.getData());
                    SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    sp.edit().putString("email", email).apply();
                    sp.edit().putString("password", password).apply();
                    sp.edit().putInt("uid", uid).apply();

                    Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(i);
                }
            }
        }
    };

    public void clickRegister(View view){
        if (view.getId() == R.id.register){
            Intent i = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(i);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isLogOut = getIntent().getBooleanExtra("isLogout", false);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");

        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        email = sp.getString("email", null);
        password = sp.getString("password", null);

        if(email != null && password != null){
            Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(i);
        }else{
            setContentView(R.layout.activity_main);

            Button signin = (Button) findViewById(R.id.signIn);
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();

                    email = ((EditText)findViewById(R.id.username)).getText().toString();
                    password = ((EditText)findViewById(R.id.password)).getText().toString();

                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    md.update(password.getBytes());

                    byte byteData[] = md.digest();

                    //convert the byte to hex format
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < byteData.length; i++) {
                        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    password = sb.toString();

                    String url = FFTConstant.LONG_BASE_URL + "user/login";

                    RequestParams params = new RequestParams();
                    params.addFormDataPart("email", email);
                    params.addFormDataPart("password", password);

                    Http httpRequest = new Http();
                    httpRequest.post(url, params, mCallback);
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        if(!isLogOut){
            super.onBackPressed();
        }

    }

    public static Intent newIntent(Context mContext, boolean isLogOut){
        Intent i = new Intent(mContext, MainActivity.class);
        i.putExtra("isLogout", isLogOut);
        return i;
    }
}
