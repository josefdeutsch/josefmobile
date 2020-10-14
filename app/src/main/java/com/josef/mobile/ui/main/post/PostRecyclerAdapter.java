package com.josef.mobile.ui.main.post;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;
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
import com.josef.mobile.data.Favourite;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PostRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "PostRecyclerAdapter";
    @Inject
    Drawable logo;
    @Inject
    RequestManager requestManager;
    PostRecyclerViewOnClickListener postRecyclerViewOnClickListener;
    Context context;
    SparseArray<Boolean> sparseArray;
    private List<Favourite> posts = new ArrayList<>();

    public PostRecyclerAdapter(Context context, PostRecyclerViewOnClickListener postRecyclerViewOnClickListener) {
        this.context = context;
        this.postRecyclerViewOnClickListener = postRecyclerViewOnClickListener;
        sparseArray = new SparseArray<>();
        for (int i = 0; i <= 200; i++) {
            sparseArray.put(i, false);
        }

    }

    interface PostRecyclerViewOnClickListener {
        void onClick(Favourite favourite);
    }

    public void setPosts(List<Favourite> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(final RecyclerView.ViewHolder holder) {
        // ((PostViewHolder) holder).toggleButton.setOnCheckedChangeListener(null);
        Log.d(TAG, "onViewRecycled: " + "hello");

        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        if (position != RecyclerView.NO_POSITION) {
            if (sparseArray.get(position).booleanValue()) {
                ((PostViewHolder) holder).toggleButton.setChecked(true);
            } else {
                ((PostViewHolder) holder).toggleButton.setChecked(false);
            }
        }

        ((PostViewHolder) holder).bind(posts.get(position));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_list_item, parent, false);
        return new PostViewHolder(view);
    }

    interface OnCheckedListener {
        void onChecked(boolean isChecked, Favourite container);
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


        // int getAdapterPosition???

        public void bind(final Favourite container) {
            Log.d(TAG, "bind: " + container.getTitle());
            Picasso.get().load(container.getDescription()).config(Bitmap.Config.ARGB_8888)
                    .fit().centerCrop().into(imageView);
        }

        @Override
        public void onClick(View v) {
            postRecyclerViewOnClickListener.onClick(posts.get(getAdapterPosition()));
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                sparseArray.put(getAdapterPosition(), true);
            } else {
                sparseArray.put(getAdapterPosition(), false);
            }

        }
    }
}
