package com.josef.mobile.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.josef.josefmobile.R;
import com.josef.mobile.model.MetaData;

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
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setListItems(List<String> arrayList) {
        this.mValues = arrayList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewPager2 myViewPager2;
        private MainActivityViewPagerAdapter myAdapter;
        public ImageView imageView;

        ViewHolder(View view) {
            super(view);

            ArrayList<MetaData> arrayList = new ArrayList();

            for (int i = 0; i <= 10; i++) {
                MetaData employee = new MetaData();
                arrayList.add(employee);
            }

            arrayList.get(0).setName("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
            arrayList.get(1).setName("http://joseph3d.com/wp-content/uploads/2019/06/00020621.png");
            arrayList.get(2).setName("http://joseph3d.com/wp-content/uploads/2019/06/00030621.png");


            arrayList.get(3).setName("http://joseph3d.com/wp-content/uploads/2019/06/0001.png");
            arrayList.get(4).setName("http://joseph3d.com/wp-content/uploads/2019/06/0002.png");
            arrayList.get(5).setName("http://joseph3d.com/wp-content/uploads/2019/06/0003.png");

            arrayList.get(6).setName("http://joseph3d.com/wp-content/uploads/2019/06/00010622.png");
            arrayList.get(7).setName("http://joseph3d.com/wp-content/uploads/2019/06/00020622.png");
            arrayList.get(8).setName("http://joseph3d.com/wp-content/uploads/2019/06/00030622.png");

            myViewPager2 = view.findViewById(R.id.viewPager);
            myAdapter = new MainActivityViewPagerAdapter(mContext, arrayList);
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
}



