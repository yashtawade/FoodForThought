package com.yashtawade.foodforthought.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yashtawade.foodforthought.Http;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.adapters.ImgListAdapter;
import com.yashtawade.foodforthought.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private Button search_recipe_button;
    private EditText search_recipe_edit_text;
    private ListView result;

    private List<Recipe> result_list = new ArrayList<Recipe>();

    public ImgListAdapter adapter;
    public ProgressDialog dialog;

    //handle the response after http request
    BaseHttpRequestCallback mCallback = new BaseHttpRequestCallback(){

        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            List<Recipe> recipes = JSON.parseArray(response, Recipe.class);

            //clear the result from the last search
            result_list.clear();

            //add the recipes to the top
            for (Recipe recipe: recipes) {
                if(recipe.getMissedIngredientCount() != 0){
                    result_list.add(recipe);
                }
            }

            //add the suggested recipes to the bottom
            for (Recipe recipe: recipes) {
                if(recipe.getMissedIngredientCount() == 0){
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

            /*
            suggest = (ListView) findViewById(R.id.recipe_suggest);
            adapter = new ImgListAdapter(SearchActivity.this, CONTENT_SEARCH_SUGGEST);
            adapter.setDataSource(suggest_list);
            suggest.setAdapter(adapter);
            */
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

                String[] ingredients = input.split(",");
                String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=true&limitLicense=false&number=20&ranking=1&ingredients=";
                //concat the url
                for(String ingredient : ingredients) {
                    url = url + ingredient.trim() + ",";
                }
                url = url.substring(0, url.length() - 1);   //delete the useless ,

                //send asynchronous http request
                Http httpRequest = new Http();
                httpRequest.get(url, mCallback);
            }
        });



    }

}
