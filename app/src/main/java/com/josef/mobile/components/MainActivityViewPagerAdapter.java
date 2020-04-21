package com.josef.mobile.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.josef.josefmobile.R;
import com.josef.mobile.AppPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.josef.mobile.Config.APPPREFERENCE_DEFAULTVALUE;

public class MainActivityViewPagerAdapter extends RecyclerView.Adapter<MainActivityViewPagerAdapter.MyViewHolder> {

    private Context context;
    private JSONArray mValues;
    private ArrayList mShareValues;

    public MainActivityViewPagerAdapter(Context context, JSONArray arrayList) {
        mValues = arrayList;
        if (AppPreferences.getName(context)==null) {
            mShareValues = new ArrayList();
            mShareValues.add(APPPREFERENCE_DEFAULTVALUE);
        } else {
            mShareValues = new ArrayList<String>(AppPreferences.getName(context));
        }
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

        try {
            JSONObject container = mValues.getJSONObject(position);
            JSONObject metadata = (JSONObject) container.get("metadata");
            String name = (String) metadata.get("name");
            String png = (String) metadata.get("png");
            Picasso.get().load(png).config(Bitmap.Config.RGB_565)
                    .fit().centerCrop().into(holder.imageView);
            String url = (String) metadata.get("url");
            holder.bind(url, position);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setmValues(JSONArray arrayList){
        mValues = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues==null)  return 0;
        return mValues.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public ToggleButton buttonFavorite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.imgBanner);
            buttonFavorite = itemView.findViewById(R.id.button_favorite);
        }


        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(context, DetailActivity.class);
            //intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent);
        }

        void bind(final String metaData, final int position) {
            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
            scaleAnimation.setDuration(500);
            BounceInterpolator bounceInterpolator = new BounceInterpolator();
            scaleAnimation.setInterpolator(bounceInterpolator);
            ToggleButton buttonFavorite = itemView.findViewById(R.id.button_favorite);
            buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    compoundButton.startAnimation(scaleAnimation);
                    if (isChecked) {
                        AppPreferences.clearNameList(context);
                        mShareValues.add(metaData);
                        AppPreferences.setName(context, mShareValues);
                    } else {
                        AppPreferences.clearNameList(context);
                        mShareValues.remove(position);
                        AppPreferences.setName(context, mShareValues);
                    }
                }
            });
        }

    }
}
