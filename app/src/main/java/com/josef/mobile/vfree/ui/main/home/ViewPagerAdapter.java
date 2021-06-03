package com.josef.mobile.vfree.ui.main.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        @BindView(R.id.profile_button_container)
        RelativeLayout button_container;
        @BindView(R.id.profile_logo_container)
        FrameLayout image_container;
        @BindView(R.id.profile_arrow_container)
        FrameLayout text_container;
        @BindView(R.id.learnmore)
        Button learn_more;
        @BindView(R.id.vision_header)
        TextView header;
        @BindView(R.id.continue_action)
        Button continue_action;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void onBindViews(int position) throws IOException {
            int last = homeList.size() - 1;
            //   requestManager.load(profileList.get(position).getUrl()).into(animatedGif);
            header.setText(homeList.get(position).getArticle());
            // article.setTag(R.id.article);

            if (last == position) {
                button_container.setVisibility(View.VISIBLE);
            } else {
                button_container.setVisibility(View.INVISIBLE);
            }
        }

        @OnClick(R.id.arrow_button)
        public void onItemArrowClicked(View v) {
            viewpagerAdapterOnClickListener.onItemArrowClicked(getAdapterPosition());
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
