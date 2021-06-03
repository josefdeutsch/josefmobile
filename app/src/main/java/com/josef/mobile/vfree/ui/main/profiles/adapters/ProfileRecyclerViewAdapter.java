package com.josef.mobile.vfree.ui.main.profiles.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.josef.mobile.vfree.ui.main.about.model.About;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public final class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {

    @Nullable
    private List<About> abouts;

    @NonNull
    private final Context context;

    @Inject
    public ProfileRecyclerViewAdapter(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 final int position) {

        final About about = abouts.get(position);

        holder.header.setText(about.getHeader());
        holder.subheader.setText(about.getSubheader());
        holder.article.setText(about.getArticle());


    }

    @Override
    public int getItemCount() {
        if (abouts == null) abouts = new ArrayList<>();
        Log.d(TAG, "getItemCount: " + abouts.size());
        return abouts.size();
    }

    public void setAbouts(@Nullable List<About> abouts) {
        this.abouts = abouts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.header)
        TextView header;
        @BindView(R.id.subheader)
        TextView subheader;
        @BindView(R.id.article)
        TextView article;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
