package com.josef.mobile.components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.josef.josefmobile.R;
import com.josef.mobile.free.DetailActivity;
import com.josef.mobile.model.MetaData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivityViewPagerAdapter extends RecyclerView.Adapter<MainActivityViewPagerAdapter.MyViewHolder> {

    private Context context;
    private List<MetaData> mValues;

    public MainActivityViewPagerAdapter(Context context, ArrayList<MetaData> arrayList) {
        mValues = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mainactiviy_viewpagerfragment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mValues.get(position).getName()).config(Bitmap.Config.RGB_565)
                .fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private ImageButton selectionView;

        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.imgBanner);
            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
            scaleAnimation.setDuration(500);
            BounceInterpolator bounceInterpolator = new BounceInterpolator();
            scaleAnimation.setInterpolator(bounceInterpolator);
            ToggleButton buttonFavorite = itemView.findViewById(R.id.button_favorite);
            buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    compoundButton.startAnimation(scaleAnimation);
                }



                //   selectionView = itemView.findViewById(R.id.selectionView);
            });
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        void bind(final MetaData meta) {
            selectionView.setVisibility(meta.isChecked() ? View.VISIBLE : View.GONE);
            selectionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    meta.setChecked(!meta.isChecked());
                    selectionView.setVisibility(meta.isChecked() ? View.VISIBLE : View.GONE);
                }
            });
        }
    }
}