package com.josef.mobile.ui.main.profile;

import android.animation.Animator;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.josef.mobile.R;

import java.io.IOException;
import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private final RequestManager requestManager;
    private final Context context;


    private ArrayList<String> arrayList;

    public ViewPagerAdapter(RequestManager requestManager, Context context) {
        this.requestManager = requestManager;
        this.context = context;

    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

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

    @Override
    public int getItemCount() {
        if (arrayList == null) return 0;
        return 4;
    }

    public void onAnimationChanged() {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView article;
        ImageView animatedGif;

        private Animator animator;

        ViewHolder(View itemView) {
            super(itemView);

        }

        private void onBindViews(int position) throws IOException {
            article = itemView.findViewById(R.id.article);
            animatedGif = itemView.findViewById(R.id.animated_gif);
            String file = "http://joseph3d.com/wp-content/uploads/2020/11/LogoAnimatedBlack.gif";
            Glide.with(context).load(file).into(animatedGif);
            animatedGif.setImageURI(Uri.parse(file));

            article.setTag(R.id.article);
            // article.setText(arrayList.get(position));
        }
    }
}
