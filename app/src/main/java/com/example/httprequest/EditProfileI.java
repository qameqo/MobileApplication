package com.example.httprequest;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.httprequest.MainActivity.APP_PREFER;

public class EditProfileI extends AppCompatActivity {
    TextInputEditText txtpass;
    TextInputEditText txtfname;
    TextInputEditText txtlname;
    TextInputEditText txtaddress;
    TextInputEditText txttel;



    String ID = "";
    String idm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        txtpass = findViewById(R.id.editText);
        txtfname = findViewById(R.id.editText2);
        txtlname = findViewById(R.id.editText3);
        txtaddress = findViewById(R.id.editText4);
        txttel = findViewById(R.id.editText5);
        SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
        String sharePrefID = sharedPrefer.getString("idPref3",null);
        idm = sharePrefID;
        TextView view = (TextView) findViewById(R.id.textView11);
        view.setText("แก้ไขข้อมูลส่วนตัว");
        //Date picker (Birth date)



        //Spinner (Employee type)


        //Switch button (is Active)



        //Receive empID from caller


        //Toolbar

        Intent intent = getIntent();
        ID = intent.getExtras().getString("id_Employee");
        //Bottom navigation view


        BottomNavigationView btm = findViewById(R.id.btm);
        btm.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if(id == R.id.item_report){
//                            menuItem.setIcon()
                            Intent intent = new Intent(getApplicationContext(), Insurance.class);
                            startActivity(intent);
                        }else if(id == R.id.item_success){
                            Intent intent = new Intent(getApplicationContext(), InsSuccess.class);
                            startActivity(intent);
                        }else if(id == R.id.item_profile){
//                            SharedPreferences sharePrefer = getSharedPreferences(MainActivity.APP_PREFER,
//                                    Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharePrefer.edit();
//                            editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
//                            editor.commit();  // ยืนยันการแก้ไข preferences
                            Intent intent = new Intent(getApplicationContext(), ProfileIns.class);
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
                            case R.id.item_update:
                                update();
                                return true;

                        }
                        return false;
                    }
                });
        showData(idm);
    }
    private void showData(String idm){
        AsyncHttpClient http = new AsyncHttpClient();
        txtpass.requestFocus();
        String url = getString(R.string.root_url6) + idm;
        http.post(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    String status = obj.getString("status");
                    if(status.equals("true")) {
                        JSONArray data = obj.getJSONArray("data");
                        JSONObject item = data.getJSONObject(0);
                        txtpass.setText(item.getString("Password"));
                        txtfname.setText(item.getString("F_Name"));
                        txtlname.setText(item.getString("L_Name"));
                        txtaddress.setText(item.getString("Address"));
                        txttel.setText(item.getString("Tel"));


                    }else{
                        Toast.makeText(getApplicationContext(), "ไม่สามารถแสดงข้อมูลได้",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void update() {
        //Validate
        Boolean cancel = false;
        String prefix = "กรุณาระบุ";
        txtpass.setError(null);
        txtfname.setError(null);
        txtlname.setError(null);
        txtaddress.setError(null);
        txttel.setError(null);


        //Empty
        if (TextUtils.isEmpty(txtpass.getText().toString())){
            cancel = true;
            txtpass.setError(prefix + txtpass.getHint().toString());
            txtpass.requestFocus();
        }else if (TextUtils.isEmpty(txtfname.getText().toString())){
            cancel = true;
            txtfname.setError(prefix + txtfname.getHint().toString());
            txtfname.requestFocus();
        }else if (TextUtils.isEmpty(txtlname.getText().toString())){
            cancel = true;
            txtlname.setError(prefix + txtlname.getHint().toString());
            txtlname.requestFocus();
        }else if (TextUtils.isEmpty(txtaddress.getText().toString())){
            cancel = true;
            txtaddress.setError(prefix + txtaddress.getHint().toString());
            txtaddress.requestFocus();
        }else if (TextUtils.isEmpty(txttel.getText().toString())){
            cancel = true;
            txttel.setError(prefix + txttel.getHint().toString());
            txttel.requestFocus();
        }
        if(cancel==false) { //Send data to web API
            String url = getString(R.string.root_url7)+ idm;
            RequestParams rp = new RequestParams();
            rp.add("password", txtpass.getText().toString());
            rp.add("fname", txtfname.getText().toString());
            rp.add("lname", txtlname.getText().toString());
            rp.add("address", txtaddress.getText().toString());
            rp.add("tel", txttel.getText().toString());
            //queryใหม่
            AsyncHttpClient http = new AsyncHttpClient();
//            String url1 = getString(R.string.root_url7)+ idm;
            http.post(url,rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONObject json = new JSONObject(response.toString());
                        String status = (String) json.get("status");
                        if (status.equals("true")) {
                            Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อย",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(),ProfileIns.class);
                            startActivity(i);

                        } else {
                            Toast.makeText(getApplicationContext(), "ไม่สามารถบันทึกข้อมูลได้",
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    public void cancel() {
        showData(idm);
    }
    //    private void back(){
//        Intent intent = new Intent(getApplicationContext(), EmployeeDeleteActivity.class);
//        intent.putExtra("empID", String.valueOf(empID));
//        startActivity(intent);
//        finish();
//    }
    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SharedPreferences sharePrefer = getSharedPreferences(MainActivity.APP_PREFER, Context.MODE_PRIVATE);
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

