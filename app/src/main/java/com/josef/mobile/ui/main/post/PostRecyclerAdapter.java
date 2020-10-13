package com.josef.mobile.ui.main.post;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.josef.mobile.models.Container;
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
    private List<Container> posts = new ArrayList<>();

    ChangeOnCheckedListener changeOnCheckedListener;

    public void setPosts(List<Container> posts, ChangeOnCheckedListener changeOnCheckedListener) {
        this.posts = posts;
        this.changeOnCheckedListener = changeOnCheckedListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_list_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PostViewHolder) holder).bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface ChangeOnCheckedListener {
        void onItemCheckedListener();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView imageView;
        ToggleButton toggleButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
            toggleButton = itemView.findViewById(R.id.toggle);
            //toggleButton.setChecked(true);


        }

        public void bind(final Container container) {
            //Log.d(TAG, "bind: "+container.getUrl());
            Picasso.get().load(container.getPng()).config(Bitmap.Config.ARGB_8888)
                    .fit().centerCrop().into(imageView);

            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    // Favourite favourite = new Favourite(container.getPng(), container.getUrl(), 0);
                    //  mFavouriteViewModel.insert(favourite);
                }
            });
        }
    }
}
