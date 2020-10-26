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
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.josef.mobile.R;
import com.josef.mobile.data.local.prefs.PreferencesHelper;
import com.josef.mobile.ui.main.post.model.Container;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PostRecyclerAdapter";

    private final RequestManager requestManager;
    private final PostRecyclerViewOnClickListener postRecyclerViewOnClickListener;
    private final Context context;
    private final PreferencesHelper preferencesHelper;

    HashMap<Integer, Boolean> map;

    private List<Container> posts = new ArrayList<>();

    public PostRecyclerAdapter(Context context, RequestManager requestManager, PreferencesHelper preferencesHelper, PostRecyclerViewOnClickListener postRecyclerViewOnClickListener) {
        this.context = context;
        this.requestManager = requestManager;
        this.postRecyclerViewOnClickListener = postRecyclerViewOnClickListener;
        this.preferencesHelper = preferencesHelper;
        map = getMap(map);
        Log.d(TAG, "PostRecyclerAdapter: ");

    }

    private HashMap<Integer, Boolean> getMap(HashMap<Integer, Boolean> hashmap) {
        HashMap<Integer, Boolean> map;
        if (preferencesHelper.getHashString().equals("uschi")) {
            Log.d(TAG, "getMap: ");
            map = new HashMap<>();
            for (int i = 0; i <= 150 - 1; i++) {
                map.put(i, false);
            }
        } else {
            Type sparseArrayType = new TypeToken<HashMap<Integer, Boolean>>() {
            }.getType();
            Gson gson = new GsonBuilder()
                    .create();
            String stringmap = preferencesHelper.getHashString();
            Log.d(TAG, "onsuccuess" + stringmap);
            map = gson.fromJson(stringmap, sparseArrayType);
        }
        return map;
    }

    @Override
    public void onViewRecycled(final RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Gson gson = new GsonBuilder()
                .create();
        String string = gson.toJson(map);
        Log.d(TAG, "onDetachedFromRecyclerView: " + string);
        preferencesHelper.setHashString(string);
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

    interface PostRecyclerViewOnClickListener {

        void onClick(Container favourite);

        void onChecked(Boolean isChecked, Container favourite);
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
            postRecyclerViewOnClickListener.onClick(posts.get(getAdapterPosition()));
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            map.put(getAdapterPosition(), isChecked);
            postRecyclerViewOnClickListener.onChecked(isChecked, posts.get(getAdapterPosition()));
        }
    }
}
