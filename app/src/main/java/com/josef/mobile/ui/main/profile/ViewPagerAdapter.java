package com.josef.mobile.ui.main.profile;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.R;

import java.io.IOException;
import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private final RequestManager requestManager;
    private final Context context;


    private ArrayList<String> arrayList;
    private static final String TAG = "ViewPagerAdapter";

    public ViewPagerAdapter(RequestManager requestManager, Context context) {
        this.requestManager = requestManager;
        this.context = context;

    }

    private ArrayList<String> uri;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            holder.onBindViews(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setArrayList(ArrayList<String> arrayList, ArrayList<String> uri) {
        this.uri = uri;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public void onAnimationChanged() {

    }

    @Override
    public int getItemCount() {
        if (arrayList == null) return 0;
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout image_container;
        FrameLayout text_container;
        TextView article;
        TextView header;
        ImageView animatedGif;


        private Animator animator;

        ViewHolder(View itemView) {
            super(itemView);

        }

        private void onBindViews(int position) throws IOException {
            animatedGif = itemView.findViewById(R.id.animated_gif);
            requestManager.load(uri.get(position)).into(animatedGif);

            image_container = itemView.findViewById(R.id.profile_image_container);
            text_container = itemView.findViewById(R.id.profile_text_container);
            header = itemView.findViewById(R.id.header);
            article = itemView.findViewById(R.id.article);
            article.setText(arrayList.get(position));
            article.setTag(R.id.article);
            //article.setVisibility(View.INVISIBLE);

            if (position > 0) {
                Log.d(TAG, "onBindViews: ");
                text_container.setVisibility(View.VISIBLE);
                image_container.setVisibility(View.INVISIBLE);
                header.setVisibility(View.INVISIBLE);
            } else {
                Log.d(TAG, "onBindViews: ");
                text_container.setVisibility(View.INVISIBLE);
                image_container.setVisibility(View.VISIBLE);
                header.setVisibility(View.VISIBLE);
            }
        }
    }
}
