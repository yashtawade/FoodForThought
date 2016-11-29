package com.yashtawade.foodforthought.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.constants.FFTConstant;
import com.yashtawade.foodforthought.models.DataParse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.Response;

public class RegisterActivity extends Activity {

    /**
     * Check the registration status and call the new activity
     */
    BaseHttpRequestCallback mCallback = new BaseHttpRequestCallback(){
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers){
            DataParse dp = JSON.parseObject(response, DataParse.class);
            if(dp != null && dp.getError() != 1){
                Intent i = MainActivity.newIntent(RegisterActivity.this, false);
                startActivity(i);
                Toast.makeText(RegisterActivity.this, "Congratulations! You're now a registered user!", Toast.LENGTH_SHORT).show();
            }else if(dp == null){
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(RegisterActivity.this, "Email has been registered", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void onRegisterClick(View view) {
        if (view.getId() == R.id.bSignUp) {
            EditText firstName = (EditText) findViewById(R.id.firstName);
            EditText lastName = (EditText) findViewById(R.id.lastName);
            EditText email = (EditText) findViewById(R.id.email);
            EditText pass1 = (EditText) findViewById(R.id.pass1);
            EditText pass2 = (EditText) findViewById(R.id.pass2);

            String firstNameStr = firstName.getText().toString();
            String lastNameStr = lastName.getText().toString();
            String emailStr = email.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();

            if (firstNameStr.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
            }else if (lastNameStr.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
            }else if (!isValidEmailAddress(emailStr)) {
                Toast.makeText(RegisterActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            }else if(pass1str.isEmpty()){
                Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            }else if(pass2str.isEmpty()){
                Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            }else if (!pass1str.equals(pass2str)) {
                Toast.makeText(RegisterActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            }else{
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(pass1str.getBytes());

                byte byteData[] = md.digest();

                //convert the byte to hex format
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < byteData.length; i++) {
                    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                String password = sb.toString();

                String url = FFTConstant.LONG_BASE_URL + "user/register";

                RequestParams params = new RequestParams();
                params.addFormDataPart("firstname", firstNameStr);
                params.addFormDataPart("lastname", lastNameStr);
                params.addFormDataPart("email", emailStr);
                params.addFormDataPart("password", password);

                Http httpRequest = new Http();
                httpRequest.post(url, params, mCallback);
            }
        }

    }

    public void takeBack(View view) {
        if (view.getId() == R.id.button) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

    }

    private boolean isValidEmailAddress(String email){
        boolean result = true;
        try {
            InternetAddress address = new InternetAddress(email);
            address.validate();
        } catch (AddressException e) {
            result = false;
        }
        return result;
    }
}

