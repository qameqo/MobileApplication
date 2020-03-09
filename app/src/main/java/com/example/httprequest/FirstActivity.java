package com.example.httprequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.httprequest.MainActivity.APP_PREFER;

public class FirstActivity extends AppCompatActivity {

    private EditText ok;
    private EditText ok2;
    public static final String USERNAME_PREFER = "usernamePref";
//    public static final String ID_PREFER = "idPref";
    //SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        ok = findViewById(R.id.ok);
        ok2 = findViewById(R.id.ok2);

        SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
        String sharePrefUsername = sharedPrefer.getString("usernamePref",null);

        if (sharedPrefer.contains(USERNAME_PREFER)) {
            ok.setText(sharedPrefer.getString(USERNAME_PREFER, ""));
//            ok2.setText(sharedPrefer.getString(ID_PREFER, ""));
        }

    }
    public void onInsert(View v)
    {

        RequestParams params = new RequestParams();
        params.add("detail", ok.getText().toString());
        params.add("title",ok2.getText().toString());
        //params.add("password", ok2.getText().toString());

        AsyncHttpClient http = new AsyncHttpClient();
        http.post("http://gg.harmonicmix.xyz/Test_api/",params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response.toString());
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               String status = null;
                try {
                    status = (String) obj.get("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("insert")) {
                    Toast.makeText(getApplicationContext(), "ลงเบส", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
//                    startActivity(intent);
//                    isSuccess = true;

                }
                else {
                    Toast.makeText(getApplicationContext(), "ไม่ลง", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("onFailure", Integer.toString(statusCode));
            }
        });
        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
    }
    public  void  onImage(View view){
        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(intent);
    }
    public  void  onMap(View view){
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);
    }
    //Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SharedPreferences sharePrefer = getSharedPreferences(MainActivity.APP_PREFER,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharePrefer.edit();
            editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
            editor.commit();  // ยืนยันการแก้ไข preferences
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
