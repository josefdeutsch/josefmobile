package com.josef.mobile.ui.main.archive;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.josef.mobile.data.Favourite;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArchiveRecyclerViewAdapter extends RecyclerView.Adapter<ArchiveRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private final OnDeleteCallBack mDeleteCallBack;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };
    private List<Favourite> mValues;

    public ArchiveRecyclerViewAdapter(Context context, List<Favourite> items, OnDeleteCallBack deleteCallBack) {
        mContext = context;
        mValues = items;
        mDeleteCallBack = deleteCallBack;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archive_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.onDelete(mValues.get(position));
        Picasso.get().load(mValues.get(position).getDescription()).config(Bitmap.Config.ARGB_8888)
                .fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (mValues == null) return 0;
        return mValues.size();
    }

    public void setListItems(List<Favourite> arrayList) {
        this.mValues = arrayList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        mValues.remove(position);
        notifyDataSetChanged();

    }

    public Context getContext() {
        return mContext;
    }

    public interface OnDeleteCallBack {
        void delete(Favourite note);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ToggleButton mDelete;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            mDelete = view.findViewById(R.id.button_delete);

        }

        private void onDelete(final Favourite note) {
            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
            scaleAnimation.setDuration(500);
            BounceInterpolator bounceInterpolator = new BounceInterpolator();
            scaleAnimation.setInterpolator(bounceInterpolator);

            mDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {
                    compoundButton.startAnimation(scaleAnimation);
                    if (isChecked) {
                        mDeleteCallBack.delete(note);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                compoundButton.setChecked(false);
                            }
                        }, 3000);
                    } else {

                    }
                }
            });
        }
    }
}



