package com.josef.mobile.vfree.ui.main.profiles.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.R;
import com.josef.mobile.vfree.ui.main.about.model.About;
import com.josef.mobile.vfree.ui.main.post.PostRecyclerAdapter;
import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.ui.main.profiles.model.Profile;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.Url;

import static android.content.ContentValues.TAG;

public final class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {

    @Nullable
    private List<Profile> profile;

    @NonNull
    private final Context context;

    @NonNull
    private RequestManager requestManager;

    public void setProfileRecyclerViewOnClickListener(@NonNull ProfileRecyclerViewOnClickListener profileRecyclerViewOnClickListener) {
        this.profileRecyclerViewOnClickListener = profileRecyclerViewOnClickListener;
    }

    @NonNull
    private ProfileRecyclerViewOnClickListener profileRecyclerViewOnClickListener;

    public interface ProfileRecyclerViewOnClickListener {

        void onClick(@NonNull Profile profile);
    }

    @Inject
    public ProfileRecyclerViewAdapter(@NonNull Context context,
                                      @NonNull RequestManager requestManager) {
        this.context = context;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 final int position) {

        ((ProfileRecyclerViewAdapter.ViewHolder) holder).bind(profile.get(position));

    }

    @Override
    public int getItemCount() {
        if (profile == null) profile = new ArrayList<>();
        Log.d(TAG, "getItemCount: " + profile.size());
        return profile.size();
    }

    public void setAbouts(@Nullable List<Profile> profile) {
        this.profile = profile;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imagetext)
        TextView imagetext;
        @BindView(R.id.imagelogo)
        ImageView imagelogo;
        @BindView(R.id.imagebackground)
        ImageView imagebackground;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bind(final Profile profile) {
            requestManager.load(profile.getUrl_logo()).into(imagelogo);
            imagetext.setText(profile.getName());
        }
        @OnClick(R.id.imagebackground)
        public void onClick(View v) {
            profileRecyclerViewOnClickListener.onClick(profile.get(getAdapterPosition()));
        }
    }
}
