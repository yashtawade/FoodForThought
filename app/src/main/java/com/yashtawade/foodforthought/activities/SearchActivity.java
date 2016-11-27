package com.yashtawade.foodforthought.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.adapters.ImgListAdapter;
import com.yashtawade.foodforthought.constants.FFTConstant;
import com.yashtawade.foodforthought.models.Recipe;
import com.yashtawade.foodforthought.models.Result;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {
    private Button search_recipe_button;
    private EditText search_recipe_edit_text;
    private ListView result;
    public ImgListAdapter adapter;
    public ProgressDialog dialog;

    private String keyword = "searchComplex";
    private int ranking = 2;
    private int number = 20;
    private int offset = 0;
    private boolean fillIngredients = true;
    private boolean limitLicense = false;

    private String[] inputIngredients;

    private List<Recipe> result_list = new ArrayList<Recipe>();

    //handle the response after http request
    BaseHttpRequestCallback mCallback = new BaseHttpRequestCallback(){

        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

//            List<Recipe> recipes = JSON.parseArray(response, Recipe.class);
            Result recipeResult = JSON.parseObject(response, Result.class);
            List<Recipe> recipes = recipeResult.getResults();

            //clear the result from the last search
            result_list.clear();

            //add the recipes to the top, fix API count bug
            for (Recipe recipe: recipes) {
                int count = recipe.getMissedIngredients().size();
                if(count == 0){
                    recipe.setMissedIngredientCount(count);
                    result_list.add(recipe);
                }
            }

            //add the suggested recipes to the bottom, fix API count bug
            for (Recipe recipe: recipes) {
                int count = recipe.getMissedIngredients().size();
                if(count != 0){
                    recipe.setMissedIngredientCount(count);
                    result_list.add(recipe);
                }
            }

            //use adapter to show the recipe list
            if(adapter == null) {
                result = (ListView) findViewById(R.id.recipe_result);
                ImgListAdapter adapter = new ImgListAdapter(SearchActivity.this);
                adapter.setDataSource(result_list);
                result.setAdapter(adapter);
            }else{
                adapter.setDataSource(result_list);
                adapter.notifyDataSetChanged();
            }

            //loading dialog dismiss
            dialog.dismiss();

            //set item click event: go to the detail activity
            result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    int recipeId = result_list.get(position).getId();
                    Intent i = RecipeDetailActivity.newIntent(SearchActivity.this, recipeId, inputIngredients);
                    startActivity(i);
                }
            });

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //loading dialog initialize
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //create search button and click event
        search_recipe_button = (Button) findViewById(R.id.search_recipe_button);
        search_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                search_recipe_edit_text = (EditText) findViewById(R.id.search_recipe_edit_text);
                String input = search_recipe_edit_text.getEditableText().toString();

                inputIngredients = input.split(",");
                String url = FFTConstant.API_BASE_URL + keyword + "?fillIngredients=" + fillIngredients + "&offset=" + offset
                        + "&limitLicense=" + limitLicense + "&number=" + number + "&ranking=" + ranking + "&includeIngredients=";
                //add ingredients' name to concat the url
                for(String ingredient : inputIngredients) {
                    url = url + ingredient.trim() + ",";
                }
                url = url.substring(0, url.length() - 1);   //delete the useless


                CheckBox cb_american = (CheckBox)findViewById(R.id.CB_american);
                if(cb_american.isChecked()){
                    String cuisine = "american";
                    url += "&cuisine=" + cuisine;
                }

                CheckBox cb_maxCalories = (CheckBox)findViewById(R.id.CB_maxCalories);
                if(cb_maxCalories.isChecked()){
                    int maxCalories = 50;
                    url += "&maxCalories=" + maxCalories;
                }

                CheckBox cb_maxFat = (CheckBox)findViewById(R.id.CB_maxFat);
                if(cb_maxFat.isChecked()){
                    int maxFat = 10;
                    url += "&maxFat=" + maxFat;
                }

                //send asynchronous http request
                Http httpRequest = new Http();
                httpRequest.get(url, mCallback);
            }
        });



    }

}
