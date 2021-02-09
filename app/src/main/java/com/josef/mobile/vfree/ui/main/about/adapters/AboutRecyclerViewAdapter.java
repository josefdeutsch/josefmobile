package com.josef.mobile.vfree.ui.main.about.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.josef.mobile.vfree.ui.main.about.model.About;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.embersoft.expandabletextview.ExpandableTextView;

import static android.content.ContentValues.TAG;

public class AboutRecyclerViewAdapter extends RecyclerView.Adapter<AboutRecyclerViewAdapter.ViewHolder> {

    private List<About> abouts;

    private final Context context;

    @Inject
    public AboutRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_item,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 final int position) {

        final About about = abouts.get(position);

        holder.header.setText(about.getHeader());
        holder.subheader.setText(about.getSubheader());
        holder.subheader2.setText(about.getSubheader2());

        holder.article.setText(about.getArticle());
        holder.desc.setText(about.getDesc());
        holder.desc.setOnStateChangeListener(isShrink -> {
            About contentAbout = abouts.get(position);
            contentAbout.setShrink(isShrink);
            abouts.set(position, contentAbout);
        });
        holder.desc.setText(about.getDesc());
        holder.desc.resetState(about.isShrink());

    }

    @Override
    public int getItemCount() {
        if (abouts == null) abouts = new ArrayList<>();
        Log.d(TAG, "getItemCount: "+abouts.size());
        return abouts.size();
    }

    public void setAbouts(List<About> abouts) {
        this.abouts = abouts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView header;
        TextView subheader;
        TextView subheader2;
        TextView article;
        ExpandableTextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.header);
            subheader = itemView.findViewById(R.id.subheader);
            subheader2 = itemView.findViewById(R.id.subheader2);
            article = itemView.findViewById(R.id.article);
            desc = itemView.findViewById(R.id.desc);
        }
    }
}
