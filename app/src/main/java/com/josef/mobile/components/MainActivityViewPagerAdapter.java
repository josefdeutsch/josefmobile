package com.josef.mobile.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.josef.josefmobile.R;
import com.squareup.picasso.Picasso;

import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;
import java.util.List;

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
        return 15;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        imageView = itemView.findViewById(R.id.imgBanner);
        }
    }
}