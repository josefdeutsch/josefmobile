package com.josef.mobile.vfree.ui.main.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.vfree.ui.main.home.model.Home;
import com.josef.mobile.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    @NonNull
    private final RequestManager requestManager;
    @Nullable
    private List<Home> homeList;
    @NonNull
    private ViewpagerAdapterOnClickListener viewpagerAdapterOnClickListener;

    public void setViewpagerAdapterOnClickListener(@NonNull ViewpagerAdapterOnClickListener viewpagerAdapterOnClickListener) {
        this.viewpagerAdapterOnClickListener = viewpagerAdapterOnClickListener;
    }

    public void setProfiles(@Nullable List<Home> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(@NonNull RequestManager requestManager,
                            @NonNull Context context) {
        this.requestManager = requestManager;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
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
        if (homeList == null) homeList = new ArrayList<>();
        return homeList.size();
    }

    interface ViewpagerAdapterOnClickListener {

        void onItemInfoClicked();

        void onItemContinueClicked();

        void onItemArrowClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "ViewHolder";

        @BindView(R.id.profile_container)
        ConstraintLayout image_container;
        @BindView(R.id.learnmore)
        Button learn_more;
        @BindView(R.id.vision_header)
        TextView header;
        @BindView(R.id.vision_desc)
        TextView desc;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.continue_action)
        Button continue_action;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void onBindViews(int position) throws IOException {
            int last = homeList.size() - 1;
            requestManager.load(homeList.get(position).getUrl()).into(image);
            header.setText(homeList.get(position).getArticle());
            desc.setText(homeList.get(position).getDesc());
            // article.setTag(R.id.article);

            if (last == position) {
                learn_more.setVisibility(View.VISIBLE);
                continue_action.setVisibility(View.VISIBLE);
            } else {
                learn_more.setVisibility(View.INVISIBLE);
                continue_action.setVisibility(View.INVISIBLE);
            }
        }


        @OnClick(R.id.continue_action)
        public void onItemContinueClicked(View v) {
            viewpagerAdapterOnClickListener.onItemContinueClicked();
        }

        @OnClick(R.id.learnmore)
        public void onItemInfoClicked(View v) {
            viewpagerAdapterOnClickListener.onItemInfoClicked();
        }
    }
}
