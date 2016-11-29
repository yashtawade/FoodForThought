package com.yashtawade.foodforthought.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.adapters.DetailListAdapter;
import com.yashtawade.foodforthought.constants.FFTConstant;
import com.yashtawade.foodforthought.models.DataParse;
import com.yashtawade.foodforthought.models.InstructionStep;
import com.yashtawade.foodforthought.models.Recipe;
import com.yashtawade.foodforthought.models.Step;
import com.yashtawade.foodforthought.views.ArcMenu;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class RecipeDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private static final String EXTRA_RECIPE_ID = "fft_recipe_id";
    private static final String EXTRA_INPUT_INGREDIENTS = "fft_input_ingredients";
    private int recipeId;
    private Recipe recipe;

    private String keyWord1 = "information";
    private boolean includeNutrition = false;

    private String keyWord2 = "analyzedInstructions";
    private boolean stepBreakdown = false;


    private Context context;
    private boolean getIngredients, getSteps, getCount, getLiked;
    private String[] inputIngredients;
    List<String> instructionContent;

    private int uid;

    int count;
    boolean isLiked;

    private ArcMenu shareBut;


    /**
     * Store the recipe information from the server response
     */
    BaseHttpRequestCallback mCallback1 = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            recipe = JSON.parseObject(response, Recipe.class);
            getIngredients = true;
            setList();
        }
    };

    /**
     * Parse and store the instruction step from the response
     */
    BaseHttpRequestCallback mCallback2 = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            List<InstructionStep> instructionSteps = JSON.parseArray(response, InstructionStep.class);
            instructionContent = new ArrayList<>();

            int i = 1;
            for (InstructionStep instep : instructionSteps) {
                for (Step step : instep.getSteps()) {
                    for (String s : step.getStep().split("\\.")) {
                        String each = s.trim();
                        if(each.startsWith("(") || each.startsWith(")")){
                            each = each.substring(1, each.length());
                        }

                        if (each.length() > 10) {
                            instructionContent.add(i++ + ". " + each);
                        }
                    }
                }
            }

            getSteps = true;
            setList();
        }
    };

    /**
     * Get the number of likes of recipes from server
     */
    BaseHttpRequestCallback mCallback3 = new BaseHttpRequestCallback(){
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            DataParse dp = JSON.parseObject(response, DataParse.class);
            if(dp.getError() == 0){
                count = Integer.valueOf(dp.getData());
            }

            getCount = true;
            setList();
        }
    };

    /**
     * Toggle the like
     */
    BaseHttpRequestCallback mCallback4 = new BaseHttpRequestCallback(){
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            DataParse dp = JSON.parseObject(response, DataParse.class);
            if(dp.getError() == 0){
                if(Integer.valueOf(dp.getData()) == 1){
                    isLiked = true;
                }
            }

            getLiked = true;
            setList();
        }
    };

    /**
     * Render the RecipeDetailActivity after all the callbacks are called
     */
    public void setList() {
        if (getSteps && getIngredients && getCount && getLiked) {

            final LinearLayoutManager layoutManager = new LinearLayoutManager(RecipeDetailActivity.this);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            DetailListAdapter adapter = new DetailListAdapter(this, recipe, instructionContent, count, isLiked, uid, inputIngredients);
            mRecyclerView.setAdapter(adapter);

        }
    }

    public static Intent newIntent(Context packageContext, int recipeId, String[] inputIngredients) {
        Intent i = new Intent(packageContext, RecipeDetailActivity.class);
        i.putExtra(EXTRA_RECIPE_ID, recipeId);
        i.putExtra(EXTRA_INPUT_INGREDIENTS, inputIngredients);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        context = this;
        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        uid = sp.getInt("uid", 0);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_detail);

        inputIngredients = getIntent().getStringArrayExtra(EXTRA_INPUT_INGREDIENTS);
        recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        if (recipeId == 0) {
            throw new InvalidParameterException();
        }

        String url1 = FFTConstant.API_BASE_URL + recipeId + "/" + keyWord1 + "?includeNutrition=" + includeNutrition;
        String url2 = FFTConstant.API_BASE_URL + recipeId + "/" + keyWord2 + "?stepBreakdown=" + stepBreakdown;
        String url3 = FFTConstant.LONG_BASE_URL + "recipe/like/count?rid=" + recipeId;
        String url4 = FFTConstant.LONG_BASE_URL + "recipe/like/get?rid=" + recipeId + "&uid=" + uid;

        getIngredients = false;
        getSteps = false;

        Http httpRequest = new Http();
        httpRequest.get(url1, mCallback1);
        httpRequest.get(url2, mCallback2);
        httpRequest.get(url3, mCallback3);
        httpRequest.get(url4, mCallback4);

        // xiaolei
        shareBut = (ArcMenu) findViewById(R.id.sharebtn);

        final int itemCount = 2;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);

            if(i==0)
                item.setImageResource(R.drawable.facebookbut);
            else
                item.setImageResource(R.drawable.googleplusbut);

            final int position = i;
            shareBut.addItem(item, new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    Intent intent;
                    if(position == 0)
                    {
                        // facebook
//                        ShareLinkContent content = new ShareLinkContent.Builder()
//                                .setContentUrl(Uri.parse("https://www.fackbook.com"))
//                                .setContentTitle(recipe.getTitle())
//                                .setImageUrl(Uri.parse(recipe.getImage()))
//                                .build();
//                        ShareApi.share(content, null);
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                                .setContentTitle(recipe.getTitle())
                                .setImageUrl(Uri.parse(recipe.getImage()))
                                .build();
                        ShareDialog shareDialog = new ShareDialog((Activity) context);
                        shareDialog.show(content);

                    }else{
                        // google+
                        intent = new PlusShare.Builder(context)
                                .setType("text/plain")
                                .setText(recipe.getTitle())
                                .setContentUrl(Uri.parse(recipe.getImage()))//"https://developers.google.com/+/"
                                .getIntent();
                        startActivityForResult(intent, 0);
                    }

                }

            });// Add a menu item
        }
    }
}
