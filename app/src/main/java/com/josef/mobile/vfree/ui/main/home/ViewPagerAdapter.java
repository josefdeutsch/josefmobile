package com.josef.mobile.vfree.ui.main.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.vfree.ui.main.home.model.Profile;
import com.josef.mobile.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private final RequestManager requestManager;

    private List<Profile> profileList;
    private ViewpagerAdapterOnClickListener viewpagerAdapterOnClickListener;

    public void setViewpagerAdapterOnClickListener(ViewpagerAdapterOnClickListener viewpagerAdapterOnClickListener) {
        this.viewpagerAdapterOnClickListener = viewpagerAdapterOnClickListener;
    }

    public void setProfiles(List<Profile> profileList) {
        this.profileList = profileList;
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(RequestManager requestManager, Context context) {
        this.requestManager = requestManager;
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
        if (profileList == null) profileList = new ArrayList<>();
        return profileList.size();
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
            int last = profileList.size() - 1;
         //   requestManager.load(profileList.get(position).getUrl()).into(animatedGif);
            header.setText(profileList.get(position).getArticle());
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
