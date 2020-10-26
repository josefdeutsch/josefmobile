package com.josef.mobile.ui.main.post;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
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

    SparseBooleanArray sparseArray;
    ArrayList<String> identifer;

    private List<Container> posts = new ArrayList<>();

    public PostRecyclerAdapter(Context context, RequestManager requestManager, PreferencesHelper preferencesHelper, PostRecyclerViewOnClickListener postRecyclerViewOnClickListener) {
        this.context = context;
        this.requestManager = requestManager;
        this.postRecyclerViewOnClickListener = postRecyclerViewOnClickListener;
        this.preferencesHelper = preferencesHelper;
        sparseArray = new SparseBooleanArray();
        //  identifer = getIdentifer();
        Log.d(TAG, "PostRecyclerAdapter: onCreate");

        HashMap<Integer, Boolean> map = new HashMap<>();
        for (int i = 0; i <= 50 - 1; i++) {
            map.put(i, false);
        }

        Type sparseArrayType = new TypeToken<HashMap<Integer, Boolean>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .create();

        String string = gson.toJson(map);
        Log.d(TAG, "PostRecyclerAdapter: " + string);
        HashMap<Integer, Boolean> map1 = gson.fromJson(string, sparseArrayType);
        Log.d(TAG, "PostRecyclerAdapter: " + map1.size());

    }
    // https://gist.github.com/dmarcato/6455221


    @Override
    public void onViewRecycled(final RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        Log.d(TAG, "onDetachedFromRecyclerView: onDetach");
//        Log.d(TAG, "onDetachedFromRecyclerView: " + preferencesHelper.getSparseBooleanArrayParcelable());
    }


    public void setPosts(List<Container> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            ((PostViewHolder) holder).toggleButton.setChecked(sparseArray.get(position));
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
            sparseArray.put(getAdapterPosition(), isChecked);
            postRecyclerViewOnClickListener.onChecked(isChecked, posts.get(getAdapterPosition()));
        }
    }
}
