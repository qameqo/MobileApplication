package com.example.httprequest;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TechActivity extends AppCompatActivity {

    private EditText tech;
    //private EditText ok2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech);
        tech = findViewById(R.id.tech);
        //ok2 = findViewById(R.id.ok2);
    }


}