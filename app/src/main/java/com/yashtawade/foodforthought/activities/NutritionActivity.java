package com.yashtawade.foodforthought.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.constants.FFTConstant;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.Response;

public class NutritionActivity extends AppCompatActivity {
    private static final String EXTRA_INGREDIENT_LIST = "fft_ingredient_list";
    private String keyword = "visualizeNutrition";

    /**
     * Render the WebView with the html response
     */
    BaseHttpRequestCallback mCallback = new BaseHttpRequestCallback(){

        @Override
        public void onResponse(Response httpResponse, String response, Headers headers){
            response = response.substring(0, response.length() - 133);
            WebView webView = (WebView) findViewById(R.id.wv_nutrition_detail);
            webView.loadData(response, "text/html; charset=utf-8", "utf-8");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        String ingredientList = getIntent().getStringExtra(EXTRA_INGREDIENT_LIST);

        String url = FFTConstant.API_BASE_URL + keyword;
        RequestParams params = new RequestParams();
        params.addFormDataPart("defaultCss", true);
        params.addFormDataPart("ingredientList", ingredientList);
        params.addFormDataPart("servings", 1);
        params.addFormDataPart("showBacklink", false);

        Http httpRequest = new Http();

        httpRequest.post(url, params, mCallback);

    }

    public static Intent newIntent(Context packageContext, String ingredients){
        Intent i = new Intent(packageContext, NutritionActivity.class);
        i.putExtra(EXTRA_INGREDIENT_LIST, ingredients);
        return i;
    }
}
