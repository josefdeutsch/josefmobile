package com.josef.mobile.ui.main.profile;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private final ArrayList<String> arrayList;
    Context context;
    private ImageView mContentView;

    ViewPagerAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBindViews(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void onAnimationChanged() {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView header, subheader, article;

        private Animator animator;

        ViewHolder(View itemView) {
            super(itemView);

        }

        private void onBindViews(int position) {
            header = itemView.findViewById(R.id.header);
            subheader = itemView.findViewById(R.id.subheader);
            article = itemView.findViewById(R.id.article);
            article.setTag(R.id.article);
            article.setText(arrayList.get(position));
        }
    }
}
