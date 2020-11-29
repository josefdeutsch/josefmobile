package com.josef.mobile.ui.main.profile;

import android.content.Context;
import android.util.Log;
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
import com.josef.mobile.R;

import java.io.IOException;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private final RequestManager requestManager;
    private final Context context;


    private List<String> arrayList;
    private static final String TAG = "ViewPagerAdapter";

    private ViewpagerAdapterOnClickListener viewpagerAdapterOnClickListener;

    public void setViewpagerAdapterOnClickListener(ViewpagerAdapterOnClickListener viewpagerAdapterOnClickListener) {
        this.viewpagerAdapterOnClickListener = viewpagerAdapterOnClickListener;
    }

    interface ViewpagerAdapterOnClickListener {
        void onItemInfoClicked();

        void onItemContinueClicked();
    }

    public ViewPagerAdapter(RequestManager requestManager, Context context) {
        this.requestManager = requestManager;
        this.context = context;

    }

    private List<String> uri;

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

    public void setArrayList(List<String> arrayList, List<String> uri) {
        this.uri = uri;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (arrayList == null) return 0;
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FrameLayout image_container;
        FrameLayout text_container;
        RelativeLayout button_container;
        TextView article;
        TextView header;
        ImageView animatedGif;
        ImageView pointsLeftRight;
        Button continue_action;
        Button learn_more;

        ViewHolder(View itemView) {
            super(itemView);
            button_container = itemView.findViewById(R.id.profile_button_container);
            image_container = itemView.findViewById(R.id.profile_image_container);
            text_container = itemView.findViewById(R.id.profile_text_container);
            learn_more = itemView.findViewById(R.id.learnmore);
            pointsLeftRight = itemView.findViewById(R.id.pointsleftright);
            animatedGif = itemView.findViewById(R.id.animated_gif);
            header = itemView.findViewById(R.id.header);
            article = itemView.findViewById(R.id.article);
            continue_action = itemView.findViewById(R.id.continue_action);
        }

        private void onBindViews(int position) throws IOException {
            int last = arrayList.size() - 1;
            requestManager.load(uri.get(position)).into(animatedGif);

            article.setText(arrayList.get(position));
            article.setTag(R.id.article);

            continue_action.setOnClickListener(this);
            learn_more.setOnClickListener(this);

            if (position > 0) {
                text_container.setVisibility(View.VISIBLE);
                image_container.setVisibility(View.INVISIBLE);
                header.setVisibility(View.INVISIBLE);
                pointsLeftRight.setVisibility(View.VISIBLE);
                button_container.setVisibility(View.INVISIBLE);
                if (last == position) {
                    button_container.setVisibility(View.VISIBLE);
                } else {
                    button_container.setVisibility(View.INVISIBLE);
                }
            } else {
                text_container.setVisibility(View.INVISIBLE);
                image_container.setVisibility(View.VISIBLE);
                header.setVisibility(View.VISIBLE);
                pointsLeftRight.setVisibility(View.INVISIBLE);
                button_container.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.continue_action:
                    Log.d(TAG, "onClick: ");// do what you want when user click layout
                    viewpagerAdapterOnClickListener.onItemContinueClicked();
                    break;

                case R.id.learnmore:
                    Log.d(TAG, "onClicsdsk: "); // do what you want when user click first view
                    viewpagerAdapterOnClickListener.onItemInfoClicked();
                    break;


            }

        }
    }
}
