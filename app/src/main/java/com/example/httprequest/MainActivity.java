package com.example.httprequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    Boolean isSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public void onSignin(View v)
    {
        RequestParams params = new RequestParams();
        params.add("username", username.getText().toString());
        params.add("password", password.getText().toString());
//        params.add("username", "game");
//        params.put("password", "123456789Za");
        Log.d("iiiii","Hi");
        AsyncHttpClient http = new AsyncHttpClient();
        http.post("http://gg.harmonicmix.xyz/Te_api", params, new JsonHttpResponseHandler() {
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
                        if (status.equals("Mem")) {
                                 //Toast.makeText(getApplicationContext(), "username password ถูกกต้อง", Toast.LENGTH_LONG).show();

                            //editor.putString(EMP_TYPE_PREFER, item.getString("empType"));


                            Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                            startActivity(intent);
                            isSuccess = true;

                        }
                        else if(status.equals("io")){
                            //Toast.makeText(getApplicationContext(), "ประกัน", Toast.LENGTH_LONG).show();
                            Intent io = new Intent(getApplicationContext(), IOActivity.class);
                            startActivity(io);
                            //isSuccess = true;
                        }
                        else if(status.equals("tech")){
                            //Toast.makeText(getApplicationContext(), "ช่างซ่อม", Toast.LENGTH_LONG).show();
                            Intent tech = new Intent(getApplicationContext(), TechActivity.class);
                            startActivity(tech);
                            //isSuccess = true;
                        }
                        else if(status.equals("false")){
                                 Toast.makeText(getApplicationContext(), "ไม่พบรหัสผู้ใช้งาน", Toast.LENGTH_LONG).show();
                                 username.setText(null);
                                 password.setText(null);
                                 username.requestFocus();
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
}
