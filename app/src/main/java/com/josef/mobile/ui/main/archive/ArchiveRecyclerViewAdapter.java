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
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.josef.mobile.R;
import com.josef.mobile.data.local.db.model.Archive;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArchiveRecyclerViewAdapter extends RecyclerView.Adapter<ArchiveRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private List<Archive> mValues;
    private final OnDeleteCallBack mDeleteCallBack;

    public ArchiveRecyclerViewAdapter(Context context, OnDeleteCallBack deleteCallBack) {
        mContext = context;
        mDeleteCallBack = deleteCallBack;

    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    @Override
    public int getItemCount() {
        if (mValues == null) return 0;
        return mValues.size();
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
        holder.onBind(mValues.get(position));
    }

    public void deleteTask(int position) {
        mValues.remove(position);
        notifyDataSetChanged();
    }

    public void setListItems(List<Archive> arrayList) {
        this.mValues = arrayList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return mContext;
    }

    public interface OnDeleteCallBack {
        void delete(Archive archive);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView tag;
        ImageView imageView;
        ToggleButton mDelete;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            mDelete = view.findViewById(R.id.button_delete);
            name = view.findViewById(R.id.cardview_name);
            tag = view.findViewById(R.id.cardview_tag);

        }

        public void onBind(Archive archive) {
            Picasso.get().load(archive.getPng()).config(Bitmap.Config.ARGB_8888)
                    .fit().centerCrop().into(imageView);

            name.setText(archive.getName());
            tag.setText(archive.getTag());
        }

        private void onDelete(final Archive archive) {
            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
            scaleAnimation.setDuration(500);
            BounceInterpolator bounceInterpolator = new BounceInterpolator();
            scaleAnimation.setInterpolator(bounceInterpolator);

            mDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {
                    compoundButton.startAnimation(scaleAnimation);
                    if (isChecked) {
                        mDeleteCallBack.delete(archive);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                compoundButton.setChecked(false);
                            }
                        }, 250);
                    } else {

                    }
                }
            });
        }
    }
}



