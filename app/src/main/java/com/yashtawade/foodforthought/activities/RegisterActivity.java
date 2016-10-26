package com.yashtawade.foodforthought.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yashtawade.foodforthought.R;

/**
 * Created by yashtawade on 10/5/16.
 */

public class RegisterActivity extends Activity {

    public void onRegisterClick(View view){
        if(view.getId() == R.id.bSignUp)
        {
            EditText name = (EditText) findViewById(R.id.name);
            EditText uname = (EditText) findViewById(R.id.username);
            EditText email = (EditText) findViewById(R.id.email);
            EditText pass1 = (EditText) findViewById(R.id.pass1);
            EditText pass2 = (EditText) findViewById(R.id.pass2);

            String namestr = name.getText().toString();
            String unamestr = uname.getText().toString();
            String emailstr = email.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();

            if (unamestr.isEmpty()){
                Toast.makeText(RegisterActivity.this,"Please enter a valid username",Toast.LENGTH_SHORT).show();
            }
            if (emailstr.isEmpty()){
                Toast.makeText(RegisterActivity.this,"Please enter a valid email address",Toast.LENGTH_SHORT).show();
            }
            if (namestr.isEmpty()){
                Toast.makeText(RegisterActivity.this,"Please enter a valid name",Toast.LENGTH_SHORT).show();
            }

            if(!pass1str.equals(pass2str)) {
                Toast.makeText(RegisterActivity.this,"Passwords don't match!",Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(RegisterActivity.this,"Congratulations, you're now a registered user!",Toast.LENGTH_SHORT).show();
            }

        }

    public void takeBack(View view){
        if(view.getId() == R.id.button){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);}

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);

    }
    }

