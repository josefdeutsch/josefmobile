package com.josef.mobile.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import com.josef.josefmobile.R;
import com.josef.mobile.model.MainActivityAdapterBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mValues;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    public MainActivityAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.itemView.setTag(mValues.get(position));
       /** GlideApp.with(mContext)
                .load(mValues.get(position).getDescription())
                .centerCrop()
                .into(holder.imageButton);**/
        Picasso.get().load(mValues.get(position)).config(Bitmap.Config.ARGB_8888)
                .fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setRecipes(List<String> arrayList) {
        this.mValues = arrayList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = itemView.findViewById(R.id.imageButton);
        }
    }
}



