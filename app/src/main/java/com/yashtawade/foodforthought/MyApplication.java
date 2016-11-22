package com.yashtawade.foodforthought;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.yashtawade.foodforthought.constants.FFTConstant;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import okhttp3.Headers;


public class MyApplication extends Application{

    @Override
    public void onCreate(){

        OkHttpFinalConfiguration.Builder builder =
                new OkHttpFinalConfiguration.Builder();
        //standard http request header
        Headers commonHeaders = new Headers.Builder().add(FFTConstant.API_KEY).build();
        builder.setCommenHeaders(commonHeaders);

        OkHttpFinal.getInstance().init(builder.build());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
