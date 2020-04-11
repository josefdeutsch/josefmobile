package com.josef.mobile.free.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityViewPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ArchiveActivityAdapter extends RecyclerView.Adapter<ArchiveActivityAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mValues;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    public ArchiveActivityAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archive_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.itemView.setTag(position);
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

    public void setListItems(List<String> arrayList) {
        this.mValues = arrayList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        mValues.remove(position);
        notifyDataSetChanged();

    }
    public Context getContext(){
        return mContext;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
        }
    }
}



