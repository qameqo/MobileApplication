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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class NextActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    TextView txt;
    List<NextActivity.Person> people;
    public String IMAGE_URL = "";
    public static final String USERNAME_PREFER = "usernamePref";
    public static final String ID_PREFER = "idPref";
    String idm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);
        SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
        String sharePrefUsername = sharedPrefer.getString("usernamePref",null);
        String sharePrefID = sharedPrefer.getString("idPref",null);
        idm = sharePrefID;
        BottomNavigationView btm = findViewById(R.id.btm);
        btm.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if(id == R.id.action_rent){
                            Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                            startActivity(intent);
                        }else if(id == R.id.action_problem){
                            Intent intent = new Intent(getApplicationContext(), NextActivity.class);
                            startActivity(intent);
                        }else if(id == R.id.action_pro){
//                            SharedPreferences sharePrefer = getSharedPreferences(MainActivity.APP_PREFER,
//                                    Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharePrefer.edit();
//                            editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
//                            editor.commit();  // ยืนยันการแก้ไข preferences
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                        return true;
                    }
                }
        );
        //TextView txt = (TextView) findViewById(R.id.textView11);
//        txt.setText(idm);
//        Log.d(idm, "onCreate: ");
//        Intent intent = getIntent();
//        idm = intent.getExtras().getString("id_Member");
//
//        Log.d( "onCreate: ",idm);
//        Intent intent = getIntent();
//        ID = intent.getExtras().getString("id_Member");
//        txt = (TextView) findViewById(R.id.textView11);
//        txt.setText(ID);
        Log.d("Statuse","hi");
        //List data
        recyclerView = findViewById(R.id.rcv);
        txt = findViewById(R.id.textView9);
        txt.setText("รายการการแจ้งปัญหา");
        people = new ArrayList<>();
//        RequestParams params = new RequestParams();
        //params.put("username", "025930461012-6");
        //params.put("password", "1234");
        Log.d("nitipong","hi");

        AsyncHttpClient http = new AsyncHttpClient();
        String url = getString(R.string.root_url3) + idm;
        http.post(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    //Log.d("Statuse","Success");
                    JSONObject obj = new JSONObject(response.toString());
                    String status = obj.getString("status");
                    if(status.equals("true")) {
                        JSONArray data = obj.getJSONArray("data");
                        //Log.d("myCat",String.valueOf(data.length()));
                        for(int i=0; i < data.length(); i++ ) {
                            //Log.d("myCat", String.valueOf(i));
                            JSONObject item = data.getJSONObject(i);
                            people.add(
                                    new NextActivity.Person(item.getString("id_Problem"),
                                            item.getString("idRental"),
                                            item.getString("Detail"),
                                            item.getString("Date"),
                                            item.getString("Name_Type_problem"),
                                            item.getString("Name_Status")

                                    ));

                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(new NextActivity.PersonAdapter(people));
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "ไม่มีข้อมูลการแจ้งปัญหา", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.d("onFailure", Integer.toString(statusCode));
            }
        });


    }
    class Person {
        public String idp;
        public String idr;
        public String de;
        public String da;
        public String name;
        public String status;



        public Person(String idp, String idr, String de, String da, String name, String status) {
            this.idp = idp;
            this.idr = idr;
            this.de = de;
            this.da = da;
            this.name = name;
            this.status = status;


        }
    }

    class PersonAdapter extends RecyclerView.Adapter<NextActivity.PersonAdapter.PersonHolder>  {

        private List<NextActivity.Person> list;

        public PersonAdapter(@NonNull List<NextActivity.Person> list) {
            this.list = list;
        }

        @Override
        public NextActivity.PersonAdapter.PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
            return new NextActivity.PersonAdapter.PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(NextActivity.PersonAdapter.PersonHolder holder, final int position) {
            NextActivity.Person person = list.get(position);
            holder.person = person;

            //Log.d("mylog","xx" + person.imageFileName);
//            final TextView txt = (TextView) findViewById(R.id.txt11);
//            txt.setText(ID);
            holder.txt1.setText("รหัสการแจ้ง:\r" + person.idp + "\nรหัสการเช่า:\r" +person.idr + "\nรายละเอียด:\r" + person.de +
                    "\nวันที่และเวลาแจ้ง:\r" + person.da + "\nประเภทการแจ้ง:\r" + person.name + "\nสถานะ:\r" + person.status);

            //holder.checkBox.setChecked(person.isChecked);
            //holder.textView.setText(String.format("%d %s(%d)", position, person.firstName, person.age));
            //holder.textView.setTextColor(Color.RED);

//            final String id = person.id;
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
//                    String username = sharedPrefer.getString("usernamePref",null);
//
//
//                    Intent intent;
//                    intent = new Intent(getApplicationContext(), MapActivity.class);
//                    intent.putExtra("idRental", String.valueOf(id));
//                    startActivity(intent);
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class PersonHolder extends RecyclerView.ViewHolder {

            public NextActivity.Person person;
            public TextView txt1;

            //public ImageView imageView;
            //public CheckBox checkBox;

            public PersonHolder(View itemView) {
                super(itemView);

                txt1 = itemView.findViewById(R.id.start);


             /*
             checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    person.isChecked = b;
                }
            });
            */
            }
        }

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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), FirstActivity.class);
        startActivity(i);
    }
}