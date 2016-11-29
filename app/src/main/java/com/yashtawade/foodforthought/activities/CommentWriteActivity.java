package com.yashtawade.foodforthought.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.constants.FFTConstant;
import com.yashtawade.foodforthought.models.DataParse;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * This is the Activity for user to write comment
 */
public class CommentWriteActivity extends AppCompatActivity {
    //part of the url string
    private String keyword = "recipe/comment";
    private static final String EXTRA_RECIPE_ID = "fft_recipe_id";
    private static final String EXTRA_INPUT_INGREDIENTS = "fft_input_ingredients";

    //store the input ingredients to highlight the missing ones
    private String[] inputIngredients;
    private int uid;

    /**
     * Call the RecipeDetailActivity after server gives the success message
     */
    BaseHttpRequestCallback mCallback = new BaseHttpRequestCallback(){

        @Override
        public void onResponse(Response httpResponse, String response, Headers headers){
            DataParse dp = JSON.parseObject(response, DataParse.class);
            if(dp.getError() == 0){
                int rid = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
                Intent i = RecipeDetailActivity.newIntent(CommentWriteActivity.this, rid, inputIngredients);
                startActivity(i);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        /**
         * Get uid from sharedPreference
         */
        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        uid = sp.getInt("uid", 0);

        inputIngredients = getIntent().getStringArrayExtra(EXTRA_INPUT_INGREDIENTS);

        final EditText comment_edit_text = (EditText) findViewById(R.id.comment_write_edit_text);
        Button comment_submit_button = (Button) findViewById(R.id.comment_write_submit_button);
        comment_submit_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int rid = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
                String comment = comment_edit_text.getText().toString();
                String url = FFTConstant.LONG_BASE_URL + keyword;
                RequestParams params = new RequestParams();
                params.addFormDataPart("rid", rid);
                params.addFormDataPart("uid", uid);
                params.addFormDataPart("comment", comment);
                Http httpRequest = new Http();
                httpRequest.post(url, params, mCallback);
            }
        });
    }

    /**
     * For other activity to send recipe id and input ingredients to the CommentListActivity
     */
    public static Intent newIntent(Context packageContext, int recipeId, String[] inputIngredients) {
        Intent i = new Intent(packageContext, CommentWriteActivity.class);
        i.putExtra(EXTRA_RECIPE_ID, recipeId);
        i.putExtra(EXTRA_INPUT_INGREDIENTS, inputIngredients);
        return i;
    }
}
