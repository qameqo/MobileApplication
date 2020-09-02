package com.example.httprequest;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleActivity extends AppCompatActivity {
        private  RecyclerView rcv;
    private RecyclerView.Adapter<MyViewHolder> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rcv = new RecyclerView(this);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerView.Adapter<MyViewHolder>(){

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler, parent, false);
                return new MyViewHolder(card);
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 20;
            }
        };
        rcv.setAdapter(adapter);
        setContentView(rcv);
        //setContentView(R.layout.activity_recycler);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView){
            super(itemView);
        }
    }
}
