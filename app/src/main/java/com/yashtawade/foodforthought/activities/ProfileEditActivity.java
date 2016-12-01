package com.yashtawade.foodforthought.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.constants.FFTConstant;
import com.yashtawade.foodforthought.models.DataParse;
import com.yashtawade.foodforthought.models.User;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.Response;

public class ProfileEditActivity extends AppCompatActivity {
    private static final String EXTRA_USER_ID = "fft_user_id";
    private int uid;

    BaseHttpRequestCallback mCallback = new BaseHttpRequestCallback(){
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers){
            DataParse dp = JSON.parseObject(response, DataParse.class);
            if(dp.getError() == 0){
                User user = JSON.parseObject(dp.getData(), User.class);

                ((EditText) findViewById(R.id.edit_email)).setText(user.getEmail());
                ((EditText) findViewById(R.id.edit_first_name)).setText(user.getFirstName());
                ((EditText) findViewById(R.id.edit_last_name)).setText(user.getLastName());
                ((EditText) findViewById(R.id.edit_profile_picture)).setText(user.getProfilePicture());
                ((EditText) findViewById(R.id.edit_country)).setText(user.getCountry());
                ((EditText) findViewById(R.id.edit_status)).setText(user.getStatus());
            }else{
                Toast.makeText(ProfileEditActivity.this, "Internal Error", Toast.LENGTH_SHORT).show();
            }
        }
    };

    BaseHttpRequestCallback mCallback1 = new BaseHttpRequestCallback(){
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers){
            DataParse dp = JSON.parseObject(response, DataParse.class);
            if(dp.getError() == 0){
                Toast.makeText(ProfileEditActivity.this, "Profile change successful!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileEditActivity.this, WelcomeActivity.class);
                startActivity(i);
            }else{
                Toast.makeText(ProfileEditActivity.this, "Editing Fail", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        uid = getIntent().getIntExtra(EXTRA_USER_ID, 0);
        String url = FFTConstant.LONG_BASE_URL + "user/profile?uid=" + uid;

        Http httpRequest = new Http();
        httpRequest.get(url, mCallback);

        Button editSubmit = (Button) findViewById(R.id.edit_profile_submit);
        editSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = FFTConstant.LONG_BASE_URL + "user/profile";

                Http httpRequest = new Http();
                RequestParams params = new RequestParams();
                params.addFormDataPart("uid", uid);
                params.addFormDataPart("firstName", ((EditText) findViewById(R.id.edit_first_name)).getText().toString());
                params.addFormDataPart("lastName", ((EditText) findViewById(R.id.edit_last_name)).getText().toString());
                params.addFormDataPart("profilePicture", ((EditText) findViewById(R.id.edit_profile_picture)).getText().toString());
                params.addFormDataPart("country", ((EditText) findViewById(R.id.edit_country)).getText().toString());
                params.addFormDataPart("status", ((EditText) findViewById(R.id.edit_status)).getText().toString());

                httpRequest.post(url, params, mCallback1);
            }
        });

    }

    public static Intent newIntent(Context mContext, int uid){
        Intent i = new Intent(mContext, ProfileEditActivity.class);
        i.putExtra(EXTRA_USER_ID, uid);
        return i;
    }
}
