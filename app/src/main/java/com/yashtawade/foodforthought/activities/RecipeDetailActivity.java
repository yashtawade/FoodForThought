package com.yashtawade.foodforthought.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.android.gms.plus.PlusShare;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.adapters.DetailListAdapter;
import com.yashtawade.foodforthought.constants.FFTConstant;
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
import android.widget.ImageView;
import android.widget.Toast;


public class RecipeDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private static final String EXTRA_RECIPE_ID = "fft.recipe_id";
    private int recipeId;
    private Recipe recipe;

    private String keyWord1 = "information";
    private boolean includeNutrition = false;

    private String keyWord2 = "analyzedInstructions";
    private boolean stepBreakdown = false;


    private Context context;
    private boolean getIngredients, getSteps;
    List<String> instructionContent;

    private ArcMenu shareBut; // xiaolei


    BaseHttpRequestCallback mCallback1 = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            recipe = JSON.parseObject(response, Recipe.class);
            getIngredients = true;
            setList();

//            mRecipeImage = (ImageView) findViewById(R.id.recipe_detail_image);
//            Picasso.with(RecipeDetailActivity.this).load(recipe.getImage()).into(mRecipeImage);
//            mRecipeTitle = (TextView) findViewById(R.id.recipe_detail_title);
//            mRecipeTitle.setText(recipe.getTitle());
//
//            if(mIngredientListAdapter == null){
//                mIngredients = (ListView) findViewById(R.id.recipe_detail_ingredients);
//                DetailListAdapter adapter = new DetailListAdapter(RecipeDetailActivity.this);
//                adapter.setDataSource(ingredients);
//                mIngredients.setAdapter(adapter);
//            }else{
//                mIngredientListAdapter.setDataSource(ingredients);
//                mIngredientListAdapter.notifyDataSetChanged();
//            }
        }
    };

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
                        if (each.length() > 10) {
                            instructionContent.add(i++ + ". " + each);
                        }
                    }
                }
            }

            getSteps = true;
            setList();

//            if(mStepListAdapter == null){
//                mRecipeSteps = (ListView) findViewById(R.id.recipe_detail_steps);
//                StepListAdapter adapter = new StepListAdapter(RecipeDetailActivity.this);
//                adapter.setDataSource(instructionContent);
//                mRecipeSteps.setAdapter(adapter);
//            }else{
//                mStepListAdapter.setDataSource(instructionContent);
//                mStepListAdapter.notifyDataSetChanged();
//            }

//            Instruction instruction = new Instruction();
//            instruction.setInstructionSteps(instructionSteps);
//            recipe.setInstruction(instruction);
//
//            Log.d("instructions", recipe.getInstructionContent().toString());
        }
    };

    public void setList() {
        if (getSteps && getIngredients) {

            final LinearLayoutManager layoutManager = new LinearLayoutManager(RecipeDetailActivity.this);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            DetailListAdapter adapter = new DetailListAdapter(this, recipe, instructionContent);
            mRecyclerView.setAdapter(adapter);
        }
    }

    public static Intent newIntent(Context packageContext, int recipeId) {
        Intent i = new Intent(packageContext, RecipeDetailActivity.class);
        i.putExtra(EXTRA_RECIPE_ID, recipeId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        context = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.list_detail);

        recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        if (recipeId == 0) {
            throw new InvalidParameterException();
        }

        String url1 = FFTConstant.API_BASE_URL + recipeId + "/" + keyWord1 + "?includeNutrition=" + includeNutrition;
        String url2 = FFTConstant.API_BASE_URL + recipeId + "/" + keyWord2 + "?stepBreakdown=" + stepBreakdown;

        getIngredients = false;
        getSteps = false;

        Http httpRequest = new Http();
        httpRequest.get(url1, mCallback1);
        httpRequest.get(url2, mCallback2);

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
