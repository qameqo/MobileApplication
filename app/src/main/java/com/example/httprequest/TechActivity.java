package com.example.httprequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TechActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerViewJSON";
    private List<JSONData> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tech);

            mRecyclerView = (RecyclerView) findViewById(R.id.ree);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


            //String url = "http://stacktips.com/?json=get_category_posts&slug=news&count=30";
            String url = "http://gg.harmonicmix.xyz/Select_api";
            new GetDataBinding().execute(url);
        }
    private class GetDataBinding extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());

                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {


            if (result == 1) {
                adapter = new MyRecyclerViewAdapter(TechActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);


                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(JSONData item) {
                        Toast.makeText(TechActivity.this, item.getStart(), Toast.LENGTH_LONG).show();

                    }
                });

            } else {
                Toast.makeText(TechActivity.this, "Failed to fetch JSON data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(String s) {
        try {
            JSONObject response = new JSONObject(s);
            JSONArray posts = response.optJSONArray("posts");
            feedsList = new ArrayList<>();
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                JSONData item = new JSONData();
                item.setStart(post.optString("วันที่เริ่มเช่า"));
                item.setEnd(post.optString("วันสิ้นสุด"));
                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}