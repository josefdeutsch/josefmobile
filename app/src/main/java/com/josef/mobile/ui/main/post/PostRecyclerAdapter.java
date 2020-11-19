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
    private final Context context;


    private PostRecyclerViewOnClickListener postRecyclerViewOnClickListener;
    HashMap<Integer, Boolean> map;

    private List<Container> posts = new ArrayList<>();

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


    @Override
    public void onViewRecycled(final RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (datamanager.getHashString().equals("uschi")) {
            map = new HashMap<>();
            for (int i = 0; i <= 150 - 1; i++) {
                map.put(i, false);
            }
        } else {
            Type sparseArrayType = new TypeToken<HashMap<Integer, Boolean>>() {
            }.getType();
            Gson gson = utilManager.getGson();
            String stringmap = datamanager.getHashString();
            map = gson.fromJson(stringmap, sparseArrayType);
            Log.d(TAG, "onAttachedToRecyclerView: " + map.toString());
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Gson gson = utilManager.getGson();
        String string = gson.toJson(map);
        datamanager.setHashString(string);
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

        public void bind(final Container container) {
            requestManager.load(container.getPng()).into(imageView);
            name.setText(container.getName());
            tag.setText(container.getTag());
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
