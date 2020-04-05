package com.josef.mobile.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.josef.josefmobile.R;
import com.josef.mobile.model.MainActivityAdapterBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mValues;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    public MainActivityAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

      holder.itemView.setTag(position);
       /** GlideApp.with(mContext)
                .load(mValues.get(position).getDescription())
                .centerCrop()
                .into(holder.imageButton);**/
//       Picasso.get().load(mValues.get(position)).config(Bitmap.Config.ARGB_8888)
            //    .fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setListItems(List<String> arrayList) {
        this.mValues = arrayList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ViewPager2 myViewPager2;
        private MainActivityViewPagerAdapter myAdapter;
        public ImageView imageView;

        ViewHolder(View view) {
            super(view);

            myViewPager2 = view.findViewById(R.id.viewPager);
            myAdapter = new MainActivityViewPagerAdapter(mContext, getLists(new ArrayList()));
            myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            myViewPager2.setAdapter(myAdapter);
            myViewPager2.setOffscreenPageLimit(3);

            final float pageMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.pageMargin);
            final float pageOffset = mContext.getResources().getDimensionPixelOffset(R.dimen.offset);

            //https://proandroiddev.com/look-deep-into-viewpager2-13eb8e06e419
            myViewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float myOffset = position * -(2 * pageOffset + pageMargin);
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    if (position < -1) {
                        page.setTranslationX(-myOffset);
                        //page.setAlpha(scaleFactor);
                    } else if (position <= 1) {
                        page.setTranslationX(myOffset);
                        page.setScaleY(scaleFactor);

                    } else {
                        //page.setAlpha(scaleFactor);
                        page.setTranslationX(myOffset);
                    }
                }
            });
        }


    }

    private ArrayList<String> getLists(ArrayList<String> arrayList) {

            arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0001.png");
            arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0002.png");
            arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0003.png");
            arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
            arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020621.png");
            arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030621.png");
            arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010622.png");
            arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020622.png");
            arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030622.png");


        return arrayList;
    }








    private ArrayList<String> getList(ArrayList<String> arrayList) {

        arrayList.add("https://ya-webdesign.com/images250_/128x128-png-logos-2.png");
        arrayList.add("https://ya-webdesign.com/images250_/128x128-png-logos-2.png");
        arrayList.add("https://ya-webdesign.com/images250_/128x128-png-logos-2.png");


       // arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
       // arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020621.png");
       // arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030621.png");


        return arrayList;
    }
}



