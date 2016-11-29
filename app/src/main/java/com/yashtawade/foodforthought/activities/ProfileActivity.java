package com.yashtawade.foodforthought.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import okhttp3.Headers;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {
    private static final String EXTRA_USER_ID = "fft_user_id";

    /**
     * Render the profile page after getting the data from the server
     */
    BaseHttpRequestCallback mCallback = new BaseHttpRequestCallback(){
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers){
            DataParse dp = JSON.parseObject(response, DataParse.class);
            if(dp.getError() == 0){
                User user = JSON.parseObject(dp.getData(), User.class);
                ImageView imageView = (ImageView) findViewById(R.id.profile_image);
                if(user.getProfilePicture() != null){
                    Picasso.with(ProfileActivity.this).load(user.getProfilePicture()).into(imageView);
                    imageView.setClipToOutline(true);
                }else{
                    imageView.setImageResource(R.mipmap.ic_default_user);
                }

                ((TextView) findViewById(R.id.profile_name)).setText(user.getFirstName() + " " + user.getLastName());
                ((TextView) findViewById(R.id.profile_email)).setText(user.getEmail());
                ((TextView) findViewById(R.id.profile_country)).setText(user.getCountry());
                ((TextView) findViewById(R.id.profile_status)).setText(user.getStatus());
            }else{
                Toast.makeText(ProfileActivity.this, "Profile Error", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        int uid = sp.getInt("uid", 0);
        String url = FFTConstant.LONG_BASE_URL + "user/profile?uid=" + uid;

        Http httpRequest = new Http();
        httpRequest.get(url, mCallback);

        Button logout = (Button) findViewById(R.id.profile_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
                sp.edit().putString("email", null).apply();
                sp.edit().putString("password", null).apply();
                sp.edit().putInt("uid", 0).apply();
                Intent i = MainActivity.newIntent(ProfileActivity.this, true);
                startActivity(i);
            }
        });
    }

    public static Intent newIntent(Context mContext){
        return new Intent(mContext, ProfileActivity.class);
    }
}
