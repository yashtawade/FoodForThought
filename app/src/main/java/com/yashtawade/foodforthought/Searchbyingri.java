package com.yashtawade.foodforthought;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.drive.internal.StringListResponse;
import com.google.android.gms.nearby.connection.dev.Strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Searchbyingri extends AppCompatActivity {

    Button searchbtn;
    EditText search_inp;
    ListView result_list;
    String jsonString,myUrl;
    String url = "http://192.168.0.15/search.php";
    AsyncTask<Void, Void, Void> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbyingri);
        searchbtn = (Button) findViewById(R.id.searchbtn);
        search_inp = (EditText) findViewById(R.id.ingri_text);
        result_list = (ListView) findViewById(R.id.resultlist);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                URL url;
//                HttpURLConnection urlConnection = null;
//                try {
//                    url = new URL("http://localhost/search.php");
//
//                    urlConnection = (HttpURLConnection) url
//                            .openConnection();
//
//                    InputStream in = urlConnection.getInputStream();
//
//                    InputStreamReader isw = new InputStreamReader(in);
//
//                    int data = isw.read();
//                    while (data != -1) {
//                        char current = (char) data;
//                        data = isw.read();
//                        Log.d("ye: ",current+"");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (urlConnection != null) {
//                        urlConnection.disconnect();
//                    }
//                }
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("192.168.0.15")
                        .appendPath("search.php")
                        .appendQueryParameter("type", "searchingri")
                        .appendQueryParameter("query", search_inp.getText().toString());
                myUrl = builder.build().toString();
                mTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            jsonString = getJsonFromServer(myUrl);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        try {
                            JSONArray obj = new JSONArray(jsonString);
                            final ArrayList<String> lists = new ArrayList<String>(obj.length());
                            final ArrayList<String> ids = new ArrayList<String>(obj.length());
                            final ArrayList<String> imgs = new ArrayList<String>(obj.length());
                            for (int i=0;i<obj.length();i++){
                                JSONObject o = new JSONObject(obj.getString(i));
                                lists.add(o.getString("title"));
                                ids.add(o.getString("id"));
                                imgs.add(o.getString("image"));
                                Log.d(o.get("title")+",  ",o.getString("id"));
                            }
                            result_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,lists));
                            result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Bundle b = new Bundle();
                                    b.putString("id",ids.get(i));
                                    b.putString("recipename",lists.get(i));
                                    b.putString("img",imgs.get(i));
                                    Intent in = new Intent(getApplicationContext(),recipe.class);
                                    in.putExtras(b);
                                    startActivity(in);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                };
                mTask.execute();
            }
        });


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


//    private class UserAdapter extends BaseAdapter{
//
//        @Override
//        public int getCount() {
//            return ulist.size();
//        }
//
//        @Override
//        public ParseObject getItem(int i) {
//            return ulist.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            if (view==null){
//                view=getLayoutInflater().inflate(R.layout.event_row, null);
//            }
//            final ParseObject c= getItem(i);
//
//            TextView label= (TextView)view.findViewById(R.id.eventname);
//            TextView from= (TextView)view.findViewById(R.id.from);
//            TextView to= (TextView)view.findViewById(R.id.to);
//            TextView createdby= (TextView)view.findViewById(R.id.by);
//            from.setText(c.getString("eventdate"));
//            to.setText(c.getString("enddate"));
//            createdby.setText("created by: "+c.getString("createdby"));
//            label.setText(c.getString("name"));
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i=new Intent(EventView.this,EventDetails.class);
//                    i.putExtra("objectId",c.getObjectId());
//                    startActivity(i);
//                }
//            });
//            return view;
//        }
//    }
}

