package com.example.httprequest;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class IOActivity extends AppCompatActivity {

    private EditText io;
    //private EditText ok2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);
        io = findViewById(R.id.io);
        //ok2 = findViewById(R.id.ok2);
    }


}