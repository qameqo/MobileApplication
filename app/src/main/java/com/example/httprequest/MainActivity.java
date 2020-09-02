package com.example.httprequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
    private SharedPreferences sharedPrefer;
    public static final String APP_PREFER = "appPrefer" ;
    public static final String USERNAME_PREFER = "usernamePref";
    public static final String PASSWORD_PREFER = "passwordPref";
    public static final String ID_PREFER = "idPref";
    public static final String ID_PREFER2 = "idPref2";
    public static final String ID_PREFER3 = "idPref3";
    private EditText username;
    private EditText password;
    Boolean isSuccess = false;
    String user_id = null;
    String user_emp = null;
    String user_io = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public void onSignin(View v)
    {
        boolean cancel = false;
        String prefix = "กรุณาระบุ";
        username.setError(null);
        password.setError(null);
        if (TextUtils.isEmpty(username.getText().toString())){
            cancel = true;
            username.setError(prefix + username.getHint().toString());
            username.requestFocus();
        }else if (TextUtils.isEmpty(password.getText().toString())){
            cancel = true;
            password.setError(prefix + password.getHint().toString());
            password.requestFocus();
        }
        if(cancel == false) {
            RequestParams params = new RequestParams();
            params.add("username", username.getText().toString());
            params.add("password", password.getText().toString());
//        params.add("username", "game");
//        params.put("password", "123456789Za");
            Log.d("iiiii", "Hi");
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
                        user_id = (String) obj.get("id_Member");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        status = (String) obj.get("status");
                        user_emp = (String) obj.get("id_Employee");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        status = (String) obj.get("status");
                        user_io = (String) obj.get("id_Employeeio");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (status.equals("Mem")) {
                        Toast.makeText(getApplicationContext(), "เข้าสู่ระบบเรียบร้อย" + user_id, Toast.LENGTH_LONG).show();

                        //editor.putString  (EMP_TYPE_PREFER, item.getString("empType"));
                        //Create shared preference to store user data
                        SharedPreferences.Editor editor = sharedPrefer.edit();
                        editor.putString(USERNAME_PREFER, username.getText().toString());
                        editor.putString(PASSWORD_PREFER, password.getText().toString());
                        editor.putString(ID_PREFER, user_id);
//                            editor.putString(ID_PREFER, password.getText().toString());
                        //editor.putString(EMP_TYPE_PREFER, item.getString("empType"));
                        editor.commit();
                        Intent intent;
                        intent = new Intent(getApplicationContext(), FirstActivity.class);
//                            intent.putExtra("id_Member", user_id);
//                            Log.d(user_id, "onSuccess: ");
                        startActivity(intent);
                        isSuccess = true;

                    } else if (status.equals("io")) {
                        Toast.makeText(getApplicationContext(), "เข้าสู่ระบบเรียบร้อย", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedPrefer.edit();
                        editor.putString(USERNAME_PREFER, username.getText().toString());
                        editor.putString(PASSWORD_PREFER, password.getText().toString());
                        editor.putString(ID_PREFER3, user_io);
                        editor.commit();
                        Intent io = new Intent(getApplicationContext(), Insurance.class);
                        startActivity(io);
                        isSuccess = true;
                    } else if (status.equals("tech")) {
                        Toast.makeText(getApplicationContext(), "เข้าสู่ระบบเรียบร้อย" + user_emp, Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedPrefer.edit();
                        editor.putString(USERNAME_PREFER, username.getText().toString());
                        editor.putString(PASSWORD_PREFER, password.getText().toString());
                        editor.putString(ID_PREFER2, user_emp);
//                            Toast.makeText(getApplicationContext(), "เข้าสู่ระบบเรียบร้อย"+user_emp, Toast.LENGTH_LONG).show();
//                            editor.putString(ID_PREFER, password.getText().toString());
                        //editor.putString(EMP_TYPE_PREFER, item.getString("empType"));
                        editor.commit();
                        Intent tech;
                        tech = new Intent(getApplicationContext(), ViewTech.class);
                        startActivity(tech);
                        isSuccess = true;
                    } else if (status.equals("false")) {
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
        }
        //Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {
        sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
        if ((sharedPrefer.contains(USERNAME_PREFER)) && sharedPrefer.contains(PASSWORD_PREFER) &&(sharedPrefer.contains(ID_PREFER))){
            Intent i = new Intent(this, FirstActivity.class);
            startActivity(i);
            finish();
        }
        else if((sharedPrefer.contains(USERNAME_PREFER)) && sharedPrefer.contains(PASSWORD_PREFER) &&(sharedPrefer.contains(ID_PREFER2))){
            Intent i = new Intent(this, ViewTech.class);
            startActivity(i);
            finish();
        }
        else if((sharedPrefer.contains(USERNAME_PREFER)) && sharedPrefer.contains(PASSWORD_PREFER) &&(sharedPrefer.contains(ID_PREFER3))){
            Intent i = new Intent(this, Insurance.class);
            startActivity(i);
            finish();
        }
        super.onResume();
    }

}
