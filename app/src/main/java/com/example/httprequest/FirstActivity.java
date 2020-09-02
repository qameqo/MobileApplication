package com.example.httprequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import android.widget.ImageView;

import static com.example.httprequest.MainActivity.APP_PREFER;

public class FirstActivity extends AppCompatActivity{
//implements NavigationView.OnNavigationItemSelectedListener
    private AppBarConfiguration mAppBarConfiguration;
    RecyclerView recyclerView;
    List<FirstActivity.Person> people;
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
//        Log.d("Statuse","hi");
        //List data
        recyclerView = findViewById(R.id.rcv);

        people = new ArrayList<>();
//        RequestParams params = new RequestParams();
        //params.put("username", "025930461012-6");
        //params.put("password", "1234");
//        Log.d("nitipong","hi");

        AsyncHttpClient http = new AsyncHttpClient();
        String url = getString(R.string.root_url) + idm;
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
                                    new FirstActivity.Person(item.getString("idRental"),
                                            item.getString("Name_Brand"),
                                            item.getString("Name_Gen"),
                                            item.getString("startDate"),
                                            item.getString("endDate"),
                                            item.getString("totalprice"),
                                            item.getString("idCarregis3")
                                    ));


                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(new FirstActivity.PersonAdapter(people));
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "ไม่พบข้อมูลสำหรับการแจ้งปัญหาหรือได้แจ้งปัญหาไปแล้ว", Toast.LENGTH_LONG).show();
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
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_rent) {
//            Intent i = new Intent(FirstActivity.this,FirstActivity.class);
//            startActivity(i);
//        } else if (id == R.id.nav_problem) {
//            Intent i = new Intent(FirstActivity.this,NextActivity.class);
//            startActivity(i);
//        } else if (id == R.id.nav_logout) {
//            SharedPreferences sharePrefer = getSharedPreferences(MainActivity.APP_PREFER,
//                    Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharePrefer.edit();
//            editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
//            editor.commit();  // ยืนยันการแก้ไข preferences
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//            return true;
//        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//    @Override
//    public boolean onNavigationItemSelected(MenuItem menuItem) {
//        int id = menuItem.getItemId();
//
//        if (id == R.id.nav_home) {
//            Intent i = new Intent(FirstActivity.this,FirstActivity.class);
//            startActivity(i);
//        } else if (id == R.id.nav_gallery) {
//            Intent i = new Intent(FirstActivity.this,NextActivity.class);
//            startActivity(i);
//        } else if (id == R.id.nav_tools) {
//           SharedPreferences sharePrefer = getSharedPreferences(MainActivity.APP_PREFER,
//                    Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharePrefer.edit();
//            editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
//            editor.commit();  // ยืนยันการแก้ไข preferences
//            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//            return true;
//        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }


    class Person {
        public String id;
        public String NameB;
        public String NameG;
        public String startDate;
        public String endDate;
        public String price;
        public String car;


        public Person(String id, String NameB, String NameG, String startDate, String endDate, String price, String car) {
            this.id = id;
            this.NameB = NameB;
            this.NameG = NameG;
            this.startDate = startDate;
            this.endDate = endDate;
            this.price = price;
            this.car = car;

        }
    }

    class PersonAdapter extends RecyclerView.Adapter<FirstActivity.PersonAdapter.PersonHolder>  {

        private List<FirstActivity.Person> list;

        public PersonAdapter(@NonNull List<FirstActivity.Person> list) {
            this.list = list;
        }

        @Override
        public FirstActivity.PersonAdapter.PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
            return new FirstActivity.PersonAdapter.PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(FirstActivity.PersonAdapter.PersonHolder holder, final int position) {
            FirstActivity.Person person = list.get(position);
            holder.person = person;

            //Log.d("mylog","xx" + person.imageFileName);
//            final TextView txt = (TextView) findViewById(R.id.txt11);
//            txt.setText(ID);
            holder.txt1.setText("รหัสการเช่า:\r" + person.id + "\nยี่ห้อ:\r" +person.NameB + "\nรุ่น:\r" + person.NameG +
                    "\nวันที่เริ่มเช่า:\r" + person.startDate + "\nวันสิ้นสุดการเช่า:\r" + person.endDate + "\nรหัสรถสำรอง:\r" + person.car);

            //holder.checkBox.setChecked(person.isChecked);
            //holder.textView.setText(String.format("%d %s(%d)", position, person.firstName, person.age));
            //holder.textView.setTextColor(Color.RED);

            final String id = person.id;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
                    String username = sharedPrefer.getString("usernamePref",null);


                    Intent intent;
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("idRental", String.valueOf(id));
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class PersonHolder extends RecyclerView.ViewHolder {

            public FirstActivity.Person person;
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



}

//setContentView(R.layout.activity_nav_bar);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);toolbar



//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        Menu menu = navigationView.getMenu();
//        View headView = navigationView.getHeaderView(0);
//        ImageView imgProfile = headView.findViewById(R.id.imageViewo);
//        imgProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(FirstActivity.this,FirstActivity.class);
//                startActivity(i);
//            }
//        });

//navigationView.setCheckedItem(R.id.nav_home);
//navigationView.getMenu().getItem(1).setActionView(R.layout.menu_image);


// Passing each menu ID as a set of Ids because each
// menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_rent, R.id.nav_problem, R.id.nav_logout)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_view);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);



//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
// Passing each menu ID as a set of Ids because each
// menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);


// find MenuItem you want to change
//        MenuItem nav_camara = menu.findItem(R.id.nav_tools);
//        nav_camara.setVisible(false);
