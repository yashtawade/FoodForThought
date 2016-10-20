package com.yashtawade.foodforthought;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.yashtawade.foodforthought.R.id.imageView;

public class recipe extends AppCompatActivity {



    String jsonString,myUrl;
    TextView header,subhead,steps;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        final Bundle b = getIntent().getExtras();
        String id = b.getString("id");
        header = (TextView) findViewById(R.id.recipename);
        subhead = (TextView) findViewById(R.id.steps);
        steps = (TextView) findViewById(R.id.steplist);
        header.setText(b.getString("recipename"));
        img = (ImageView) findViewById(R.id.rimg);

        AsyncTask<Void,Void,Void> recipe = new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    jsonString = getJsonFromServer(myUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    JSONArray ja = new JSONArray(jsonString);
                    for (int i=0;i<ja.length();i++){
                        JSONObject obj = new JSONObject(ja.getString(i));

                        sb.append(obj.get("name")+"\t\n");
                        JSONArray steps = new JSONArray(obj.getString("steps"));
                        for (int j=0;j<steps.length();j++){
                            JSONObject stepsobj = new JSONObject(steps.getString(j));
                            sb.append(stepsobj.get("number")+"\t:\t"+stepsobj.get("step")+"\n");
                        }
                    }
                    Picasso.with(getApplicationContext()).load(b.getString("img")).into(img);
                    steps.setText(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };


        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("192.168.0.15")
                .appendPath("search.php")
                .appendQueryParameter("type", "recipe")
                .appendQueryParameter("id", id);
        myUrl = builder.build().toString();
        recipe.execute();

    }


    public static String getJsonFromServer(String url) throws IOException {

        BufferedReader inputStream = null;

        Log.d("url",url);
        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        inputStream = new BufferedReader(new InputStreamReader(
                dc.getInputStream()));

        // read the JSON results into a string
        String jsonResult = inputStream.readLine();
        return jsonResult;
    }
}
