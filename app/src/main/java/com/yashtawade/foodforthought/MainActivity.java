package com.yashtawade.foodforthought;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public void clickSignIn(View view){

        if (view.getId() == R.id.signIn){
            EditText a = (EditText)findViewById(R.id.username);
            String string = (String) a.getText().toString();

            Intent i = new Intent(MainActivity.this, Menu.class);
            i.putExtra("Username",string);
            startActivity(i);
        }
    }

    public void clickRegister(View view){
        if (view.getId() == R.id.register){
            Intent i = new Intent(MainActivity.this, Register.class);
            startActivity(i);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button signin = (Button) findViewById(R.id.signIn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),WelcomeActivity.class));
            }
        });

    }
}
