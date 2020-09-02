package com.example.httprequest;



import android.app.Person;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class IOActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Person> people;
    public String IMAGE_URL = "";
    public static final String USERNAME_PREFER = "usernamePref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);
        SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
        String sharePrefUsername = sharedPrefer.getString("usernamePref",null);


        Log.d("Statuse","hi");
        //List data
        recyclerView = findViewById(R.id.rcv);

        people = new ArrayList<>();
        RequestParams params = new RequestParams();
        //params.put("username", "025930461012-6");
        //params.put("password", "1234");
        Log.d("nitipong","hi");

        AsyncHttpClient http = new AsyncHttpClient();
        http.post("http://gg.harmonicmix.xyz/Select_api", params, new JsonHttpResponseHandler(){
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
                                    new Person(item.getString("idRental"),
                                            item.getString("Name_Brand"),
                                            item.getString("Name_Gen"),
                                            item.getString("startDate"),
                                            item.getString("endDate"),
                                            item.getString("totalprice")
                                    ));

                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(new PersonAdapter(people));
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "ไม่สามารถแสดงข้อมูลได้", Toast.LENGTH_LONG).show();
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
        public String id;
        public String NameB;
        public String NameG;
        public String startDate;
        public String endDate;
        public String price;


        public Person(String id, String NameB, String NameG, String startDate, String endDate, String price) {
            this.id = id;
            this.NameB = NameB;
            this.NameG = NameG;
            this.startDate = startDate;
            this.endDate = endDate;
            this.price = price;

        }
    }

    class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder>  {

        private List<Person> list;

        public PersonAdapter(@NonNull List<Person> list) {
            this.list = list;
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, final int position) {
            Person person = list.get(position);
            holder.person = person;

            //Log.d("mylog","xx" + person.imageFileName);

            holder.txt1.setText("รหัสการเช่า\r" + person.id + "\rยี่ห้อ\r" +person.NameB + "\rรุ่น\r" + person.NameG +
                    "\rวันที่เริ่มเช่า\r" + person.startDate + "\rวันสิ้นสุดการเช่า\r" + person.endDate + "\rราคารวม\r" + person.price);

            //holder.checkBox.setChecked(person.isChecked);
            //holder.textView.setText(String.format("%d %s(%d)", position, person.firstName, person.age));
            //holder.textView.setTextColor(Color.RED);

            final String id = person.id;

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class PersonHolder extends RecyclerView.ViewHolder {

            public Person person;
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
