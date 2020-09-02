package com.example.httprequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import static com.example.httprequest.MainActivity.APP_PREFER;

public class WorkTech extends AppCompatActivity {
    String idr = "";
    String idp = "";
    String de = "";
    String lat = "";
    String lng = "";
    TextView tx;
    EditText detail;
    String ide = "";
    String idc = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worktech);
        Intent i = getIntent();
        idr = i.getExtras().getString("idRental");
        idp = i.getExtras().getString("id_Problem");
        de = i.getExtras().getString("Detail");
        lat = i.getExtras().getString("Lat");
        lng = i.getExtras().getString("Lng");
        idc = i.getExtras().getString("idCarregis");
        detail = findViewById(R.id.editText7);
        TextView tx = (TextView) findViewById(R.id.textView18);
        TextView tx2 = (TextView) findViewById(R.id.textView19);
        TextView tx3 = (TextView) findViewById(R.id.textView17);
        tx.setText("รหัสการแจ้ง:" + idp);
        tx2.setText("รายละเอียดจากลูกค้า:" + de);
        tx3.setText("รายการซ่อม");
        tx3.setTextSize(20);
        SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
//        String sharePrefUsername = sharedPrefer.getString("usernamePref",null);
        String sharePrefID = sharedPrefer.getString("idPref2",null);
        ide = sharePrefID;
        BottomNavigationView btm2 = findViewById(R.id.btm);
        btm2.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if(id == R.id.action_rent){
                            Intent intent = new Intent(getApplicationContext(), ViewTech.class);
                            startActivity(intent);
                        }else if(id == R.id.action_problem){
                            Intent intent = new Intent(getApplicationContext(), ViewTechSuccess.class);
                            startActivity(intent);
                        }else if(id == R.id.action_pro){
//                            SharedPreferences sharePrefer = getSharedPreferences(MainActivity.APP_PREFER,
//                                    Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharePrefer.edit();
//                            editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
//                            editor.commit();  // ยืนยันการแก้ไข preferences
                            Intent intent = new Intent(getApplicationContext(), ProfileTech.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                        return true;
                    }
                }
        );
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_map:
                                map();
                                return true;
                            case R.id.item_success:
                                success();
                                return true;
                            case R.id.item_fail:
                                fail();
                                return true;

                        }
                        return false;
                    }
                });


    }
    public void success() {
        Boolean cancel = false;
        String prefix = "กรุณาระบุ";
        detail.setError(null);
        if (TextUtils.isEmpty(detail.getText().toString())){
            cancel = true;
            detail.setError(prefix + detail.getHint().toString());
            detail.requestFocus();
        }
        if(cancel==false) {
//        EditText detail = (EditText) findViewById(R.id.editText7);
            RequestParams params = new RequestParams();
            params.add("detail2", detail.getText().toString());
            AsyncHttpClient http = new AsyncHttpClient();
            String url = getString(R.string.root_url8) + idp + "/" + idr + "/" + ide;
            http.post(url, params, new JsonHttpResponseHandler() {
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
                    if (status.equals("true")) {
                        Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), ViewTechSuccess.class);
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("onFailure", Integer.toString(statusCode));
                }
            });
        }
//        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
    }
    public void fail(){
        Boolean cancel = false;
        String prefix = "กรุณาระบุ";
        detail.setError(null);
        if (TextUtils.isEmpty(detail.getText().toString())){
            cancel = true;
            detail.setError(prefix + detail.getHint().toString());
            detail.requestFocus();
        }
        if(cancel==false) {
//        EditText detail = (EditText) findViewById(R.id.editText7);
            RequestParams params = new RequestParams();
            params.add("detail2", detail.getText().toString());
            AsyncHttpClient http = new AsyncHttpClient();
            String url = getString(R.string.root_url9) + idp + "/" + idr + "/" + ide + "/" + idc;
            http.post(url, params, new JsonHttpResponseHandler() {
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
                    if (status.equals("true")) {
                        Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), ViewTechSuccess.class);
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("onFailure", Integer.toString(statusCode));
                }
            });
        }
//        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
    }
    public void map(){
        Uri uri = Uri.parse("geo:"+String.valueOf(lng+lat)+"?q="+String.valueOf(lat)+","+String.valueOf(lng));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(intent
                , "View map with"));
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exit, menu);
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
//        else if(id == R.id.action_rent){
//            Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
//            startActivity(intent);
//        }
//        else if(id == R.id.action_problem){
//            Intent intent = new Intent(getApplicationContext(), NextActivity.class);
//            startActivity(intent);
//        }

        return super.onOptionsItemSelected(item);
    }


}

