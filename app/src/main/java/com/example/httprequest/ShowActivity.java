package com.example.httprequest;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.httprequest.R;

public class ShowActivity extends AppCompatActivity {

    private TextView textView3;
    //private EditText ok2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        textView3 = findViewById(R.id.textView3);
        //ok2 = findViewById(R.id.ok2);
    }


}