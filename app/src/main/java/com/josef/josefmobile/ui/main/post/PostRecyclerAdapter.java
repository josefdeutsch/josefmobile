package com.josef.josefmobile.ui.main.post;

import android.content.Context;
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
import com.josef.josefmobile.data.DataManager;
import com.josef.josefmobile.data.local.db.model.LocalCache;
import com.josef.josefmobile.utils.UtilManager;
import com.josef.mobile.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Singleton
public class PostRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    }

    public void setPostRecyclerViewOnClickListener(PostRecyclerViewOnClickListener postRecyclerViewOnClickListener) {
        this.postRecyclerViewOnClickListener = postRecyclerViewOnClickListener;
    }

    public void setPosts(List<LocalCache> posts) {
        if (posts == null) posts = new ArrayList<>();
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
        return new PostViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        @BindView(R.id.cardview_name)
        TextView name;

        @BindView(R.id.cardview_tag)
        TextView tag;

        @BindView(R.id.image)
        ImageView imageView;

        @BindView(R.id.toggle)
        ToggleButton toggleButton;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            toggleButton.setOnCheckedChangeListener(this);
        }

        public void bind(final LocalCache localCache) {
            requestManager.load(localCache.getPng()).into(imageView);
            name.setText(localCache.getName());
            tag.setText(localCache.getTag());
        }

        @OnClick(R.id.image)
        public void onClick(View v) {
            postRecyclerViewOnClickListener.onClick(getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            postRecyclerViewOnClickListener.onChecked(isChecked, posts.get(getAdapterPosition()));
            postRecyclerViewOnClickListener.isFlagged(isChecked, posts.get(getAdapterPosition()));
        }
    }
}
