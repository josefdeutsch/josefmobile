package com.josef.mobile.ui.main.post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.R;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.utils.UtilManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PostRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PostRecyclerAdapter";

    private final RequestManager requestManager;
    private final DataManager datamanager;
    private final UtilManager utilManager;
    private final Context context;


    private PostRecyclerViewOnClickListener postRecyclerViewOnClickListener;


    private List<LocalCache> posts = new ArrayList<>();

    @Inject
    public PostRecyclerAdapter(RequestManager requestManager, DataManager datamanager, UtilManager utilManager, Context context) {
        this.requestManager = requestManager;
        this.datamanager = datamanager;
        this.utilManager = utilManager;
        this.context = context;
        Log.d(TAG, "PostRecyclerAdapter: ");

    }

    public void setPostRecyclerViewOnClickListener(PostRecyclerViewOnClickListener postRecyclerViewOnClickListener) {
        this.postRecyclerViewOnClickListener = postRecyclerViewOnClickListener;
    }





    public void setPosts(List<LocalCache> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            ((PostViewHolder) holder).toggleButton.setChecked(posts.get(position).isFlag());
        }
        ((PostViewHolder) holder).bind(posts.get(position));
    }

    public interface PostRecyclerViewOnClickListener {

        void onClick(int position);

        void onChecked(Boolean isChecked, LocalCache favourite);

        void isFlagged(Boolean isChecked, LocalCache favourite);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = utilManager.getScreenWidth(context) / 2;
        view.setLayoutParams(layoutParams);
        //    view.setScaleX(0.95f);
        // view.setScaleY(0.95f)
        return new PostViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        TextView name;
        TextView tag;
        ImageView imageView;
        ToggleButton toggleButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cardview_name);
            tag = itemView.findViewById(R.id.cardview_tag);
            imageView = itemView.findViewById(R.id.image);
            toggleButton = itemView.findViewById(R.id.toggle);
            toggleButton.setOnCheckedChangeListener(this);
            imageView.setOnClickListener(this);
        }

        public void bind(final LocalCache localCache) {
            requestManager.load(localCache.getPng()).into(imageView);
            name.setText(localCache.getName());
            tag.setText(localCache.getTag());
        }

        @Override
        public void onClick(View v) {
            postRecyclerViewOnClickListener.onClick(getAdapterPosition());
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // map.put(getAdapterPosition(), isChecked);
            postRecyclerViewOnClickListener.onChecked(isChecked, posts.get(getAdapterPosition()));
            postRecyclerViewOnClickListener.isFlagged(isChecked, posts.get(getAdapterPosition()));
        }
    }
}
