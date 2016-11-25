package com.yashtawade.foodforthought.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.Searchbyingri;

public class WelcomeActivity extends AppCompatActivity {

    Button search_recipe_welcome_button;
    Button missing;
    ImageButton profile_image_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        profile_image_button = (ImageButton) findViewById(R.id.profile_image_button);
        profile_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = ProfileActivity.newIntent(WelcomeActivity.this);
                startActivity(i);
            }
        });

        //to the missing page
        missing = (Button) findViewById(R.id.missing);
        missing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NearbyActivity.class));
            }
        });

        //to the search page
        search_recipe_welcome_button = (Button) findViewById(R.id.search_recipe_welcome_button);
        search_recipe_welcome_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });

        Button searchby = (Button) findViewById(R.id.searchbyingri);
        searchby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Searchbyingri.class));
            }
        });
    }

}
