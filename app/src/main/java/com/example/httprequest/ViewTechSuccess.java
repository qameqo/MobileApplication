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
import android.widget.ScrollView;
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

public class ViewTechSuccess extends AppCompatActivity {


    RecyclerView recyclerView;
    TextView txt;
    List<ViewTechSuccess.Person> people;
    public String IMAGE_URL = "";
    public static final String USERNAME_PREFER = "usernamePref";
    public static final String ID_PREFER = "idPref2";
    String idm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
        String sharePrefUsername = sharedPrefer.getString("usernamePref",null);
        String sharePrefID = sharedPrefer.getString("idPref2",null);
        idm = sharePrefID;
        BottomNavigationView btm = findViewById(R.id.btm);
//        Menu menu = bottomNavigationView.getMenu();
//        menu.findItem(R.id.action_favorites).setIcon(favDrawable);
//
//        switch (item.getItemId()) {
//            case R.id.action_favorites:
//                item.setIcon(favDrawableSelected);
//            case R.id.action_schedules:
//            case R.id.action_music:
//        }
        btm.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if(id == R.id.action_rent){
//                            menuItem.setIcon()
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
        txt.setText("รายการการซ่อมเสร็จสิ้น");
        people = new ArrayList<>();
//        RequestParams params = new RequestParams();
        //params.put("username", "025930461012-6");
        //params.put("password", "1234");
        Log.d("nitipong","hi");

        AsyncHttpClient http = new AsyncHttpClient();
        String url = getString(R.string.root_url10) + idm;
        http.post(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    //Log.d("Statuse","Success");
                    JSONObject obj = new JSONObject(response.toString());
                    String status = obj.getString("status");
                    if(status.equals("true")) {
                        JSONArray data = obj.getJSONArray("posts");
                        //Log.d("myCat",String.valueOf(data.length()));
                        for(int i=0; i < data.length(); i++ ) {
                            //Log.d("myCat", String.valueOf(i));
                            JSONObject item = data.getJSONObject(i);
                            people.add(
                                    new ViewTechSuccess.Person(item.getString("id_Problem"),

                                            item.getString("Detail"),
                                            item.getString("Detail_2"),
                                            item.getString("Date"),
                                            item.getString("Date_2"),
                                            item.getString("F_Name")


                                    ));

                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(new ViewTechSuccess.PersonAdapter(people));
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
        public String idd;
        public String lat;





        public Person(String idp, String idr, String de, String da, String  idd, String  lat) {
            this.idp = idp;
            this.idr = idr;
            this.de = de;
            this.da = da;
            this.idd = idd;
            this.lat = lat;




        }
    }

    class PersonAdapter extends RecyclerView.Adapter<ViewTechSuccess.PersonAdapter.PersonHolder>  {

        private List<ViewTechSuccess.Person> list;

        public PersonAdapter(@NonNull List<ViewTechSuccess.Person> list) {
            this.list = list;
        }

        @Override
        public ViewTechSuccess.PersonAdapter.PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
            return new ViewTechSuccess.PersonAdapter.PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewTechSuccess.PersonAdapter.PersonHolder holder, final int position) {
            ViewTechSuccess.Person person = list.get(position);
            holder.person = person;

            //Log.d("mylog","xx" + person.imageFileName);
//            final TextView txt = (TextView) findViewById(R.id.txt11);
//            txt.setText(ID);
            holder.txt1.setText("รหัสการแจ้ง:\r" + person.idp + "\nรายละเอียดลูกค้า:\r" +person.idr + "\nรายละเอียดพนักงาน:\r" + person.de +
                    "\nวันเวลาที่แจ้ง:\r" + person.da + "\nวันเวลาที่เสร็จสิ้น:\r" + person.idd + "\nพนักงานที่รับผิดชอบ:\r" + person.lat);

            //holder.checkBox.setChecked(person.isChecked);
            //holder.textView.setText(String.format("%d %s(%d)", position, person.firstName, person.age));
            //holder.textView.setTextColor(Color.RED);

//            final String id = person.idp;
//            final String idd = person.idd;
//            final String de = person.idr;
//            final String lat = person.lat;
//            final String lng = person.lng;
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
//                    String username = sharedPrefer.getString("usernamePref",null);
//
//
//                    Intent intent;
//                    intent = new Intent(getApplicationContext(), WorkTech.class);
//                    intent.putExtra("id_Problem", String.valueOf(id));
//                    intent.putExtra("idRental" , String.valueOf(idd));
//                    intent.putExtra("Detail" , String.valueOf(de));
//                    intent.putExtra("Lat" , String.valueOf(lat));
//                    intent.putExtra("Lng" , String.valueOf(lng));
//                    startActivity(intent);
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class PersonHolder extends RecyclerView.ViewHolder {

            public ViewTechSuccess.Person person;
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
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ViewTech.class);
        startActivity(i);
    }
}