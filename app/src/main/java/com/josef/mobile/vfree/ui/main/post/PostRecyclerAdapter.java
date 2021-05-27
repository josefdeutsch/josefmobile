package com.josef.mobile.vfree.ui.main.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.vfree.data.DataManager;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.utils.UtilManager;
import com.josef.mobile.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Singleton
public final class PostRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final RequestManager requestManager;
    @NonNull
    private final DataManager datamanager;
    @NonNull
    private final UtilManager utilManager;
    @NonNull
    private final Context context;
    @NonNull
    private PostRecyclerViewOnClickListener postRecyclerViewOnClickListener;
    @Nullable
    private List<LocalCache> posts = new ArrayList<>();

    @Inject
    public PostRecyclerAdapter(@NonNull RequestManager requestManager,
                               @NonNull DataManager datamanager,
                               @NonNull UtilManager utilManager,
                               @NonNull Context context) {
        this.requestManager = requestManager;
        this.datamanager = datamanager;
        this.utilManager = utilManager;
        this.context = context;
    }

    public void setPostRecyclerViewOnClickListener(@NonNull PostRecyclerViewOnClickListener postRecyclerViewOnClickListener) {
        this.postRecyclerViewOnClickListener = postRecyclerViewOnClickListener;
    }

    public void setPosts(@Nullable List<LocalCache> posts) {
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

        void onClick(@NonNull int position);

        void onChecked(@NonNull Boolean isChecked,
                       @NonNull LocalCache favourite);

        void isFlagged(@NonNull Boolean isChecked,
                       @NonNull LocalCache favourite);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        //    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        //    layoutParams.width = utilManager.getScreenWidth(context) / 2;
        //    view.setLayoutParams(layoutParams);
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
        public void onCheckedChanged(@NonNull CompoundButton buttonView, @NonNull boolean isChecked) {
            postRecyclerViewOnClickListener.onChecked(isChecked, posts.get(getAdapterPosition()));
            postRecyclerViewOnClickListener.isFlagged(isChecked, posts.get(getAdapterPosition()));
        }
    }
}
