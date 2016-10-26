package com.yashtawade.foodforthought;

import android.app.Application;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import okhttp3.Headers;


public class MyApplication extends Application{

    @Override
    public void onCreate(){

        OkHttpFinalConfiguration.Builder builder =
                new OkHttpFinalConfiguration.Builder();
        //标准http请求头
        Headers commonHeaders = new Headers.Builder().add("X-Mashape-Key:eSTTv3Urw2mshZJrgoQZoKJbwtmDp1AE9gajsnm5CFL4X5DDw6").build();
        builder.setCommenHeaders(commonHeaders);

        OkHttpFinal.getInstance().init(builder.build());
    }
}
