package com.josef.mobile.components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.josefmobile.R;
import com.josef.mobile.free.DetailActivity;
import com.squareup.picasso.Picasso;

import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivityViewPagerAdapter extends RecyclerView.Adapter<MainActivityViewPagerAdapter.MyViewHolder> {

    private Context context;
    private List<String> mValues;

    public MainActivityViewPagerAdapter(Context context, ArrayList<String> arrayList) {
        mValues = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mainactiviy_viewpagerfragment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mValues.get(position)).config(Bitmap.Config.RGB_565)
                .fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;

        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.imgBanner);
            textView = itemView.findViewById(R.id.tvName);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}