package com.josef.mobile.ui.main.archive;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.R;
import com.josef.mobile.data.local.db.model.Archive;

import java.util.List;

public class ArchiveRecyclerViewAdapter extends RecyclerView.Adapter<ArchiveRecyclerViewAdapter.ViewHolder> {

    private List<Archive> mValues;
    private final RequestManager requestManager;
    private OnDeleteCallBack mDeleteCallBack;

    public ArchiveRecyclerViewAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;

    }

    public void setmDeleteCallBack(OnDeleteCallBack mDeleteCallBack) {
        this.mDeleteCallBack = mDeleteCallBack;
    }

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

    public void setListItems(List<Archive> arrayList) {
        this.mValues = arrayList;
        notifyDataSetChanged();
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
            requestManager.load(archive.getPng()).into(imageView);
            name.setText(archive.getName());
            tag.setText(archive.getTag());
        }

        private void onDelete(final Archive archive) {
            final ScaleAnimation scaleAnimation =
                    new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);

            scaleAnimation.setDuration(500);
            BounceInterpolator bounceInterpolator = new BounceInterpolator();
            scaleAnimation.setInterpolator(bounceInterpolator);

            mDelete.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                compoundButton.startAnimation(scaleAnimation);
                if (isChecked) {

                    mDeleteCallBack.delete(archive);

                    Handler handler = new Handler();
                    handler.postDelayed(() -> compoundButton.setChecked(false), 250);
                } else {

                }
            });
        }
    }
}



