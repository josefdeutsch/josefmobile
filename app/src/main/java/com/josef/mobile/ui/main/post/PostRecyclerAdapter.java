package com.josef.mobile.ui.main.post;

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
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.josef.mobile.R;
import com.josef.mobile.data.DataManager;
import com.josef.mobile.ui.main.post.model.Container;
import com.josef.mobile.utils.UtilManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PostRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PostRecyclerAdapter";

    private final RequestManager requestManager;
    private final DataManager datamanager;
    private final UtilManager utilManager;


    private PostRecyclerViewOnClickListener postRecyclerViewOnClickListener;
    HashMap<Integer, Boolean> map;
    private List<Container> posts = new ArrayList<>();

    @Inject
    public PostRecyclerAdapter(RequestManager requestManager, DataManager datamanager, UtilManager utilManager) {
        this.requestManager = requestManager;
        this.datamanager = datamanager;
        this.utilManager = utilManager;

    }

    public void setPostRecyclerViewOnClickListener(PostRecyclerViewOnClickListener postRecyclerViewOnClickListener) {
        this.postRecyclerViewOnClickListener = postRecyclerViewOnClickListener;
    }


    @Override
    public void onViewRecycled(final RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (datamanager.getPositionToggleHashmap().equals("uschi")) {
            map = new HashMap<>();
            for (int i = 0; i <= 50 - 1; i++) {
                map.put(i, false);
            }
        } else {
            Type type = new TypeToken<HashMap<Integer, Boolean>>() {
            }.getType();
            map = supplyHashMapToPrefs(type);
        }

    }

    private <K, V> K supplyHashMapToPrefs(Type token) {
        Gson gson = utilManager.getGson();
        String stringmap = datamanager.getPositionToggleHashmap();
        return gson.fromJson(stringmap, token);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Gson gson = utilManager.getGson();
        String string = gson.toJson(map);
        datamanager.setPositionToggleHashMap(string);
    }


    public void setPosts(List<Container> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            ((PostViewHolder) holder).toggleButton.setChecked(map.get(position));
        }
        ((PostViewHolder) holder).bind(posts.get(position));
    }

    public interface PostRecyclerViewOnClickListener {

        void onClick(int position);

        void onChecked(int position, Boolean isChecked, Container favourite);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_list_item, parent, false);
        return new PostViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        TextView title;
        ImageView imageView;
        ToggleButton toggleButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
            toggleButton = itemView.findViewById(R.id.toggle);
            toggleButton.setOnCheckedChangeListener(this);
            imageView.setOnClickListener(this);
        }

        public void bind(final Container container) {
            requestManager.load(container.getPng()).into(imageView);
        }

        @Override
        public void onClick(View v) {
            postRecyclerViewOnClickListener.onClick(getAdapterPosition());
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            map.put(getAdapterPosition(), isChecked);
            postRecyclerViewOnClickListener.onChecked(getAdapterPosition(), isChecked, posts.get(getAdapterPosition()));
        }
    }
}
