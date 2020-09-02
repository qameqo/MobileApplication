package com.example.httprequest;



import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {
    private List<JSONData> feedItemList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public MyRecyclerViewAdapter(Context context, List<JSONData> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final JSONData jsonData = feedItemList.get(i);

        //Render image using Picasso library

        customViewHolder.txt1.setText(Html.fromHtml(jsonData.getStart()));
        //Setting text view title
        customViewHolder.txt2.setText(Html.fromHtml(jsonData.getEnd()));
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(jsonData);
            }
        };
        customViewHolder.txt1.setOnClickListener(listener);
        customViewHolder.txt2.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView txt1;
        protected TextView txt2;

        public CustomViewHolder(View view) {
            super(view);
            this.txt1 = (TextView) view.findViewById(R.id.start);
            this.txt2 = (TextView) view.findViewById(R.id.end);
        }
    }
    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
