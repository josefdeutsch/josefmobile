package com.josef.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityAdapter;
import com.josef.mobile.components.MainActivityViewPagerAdapter;
import com.josef.mobile.model.MainActivityAdapterBody;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewPagerAdapter mAdapter;
    private ViewPager2 mViewPager2;
    ViewPager2 myViewPager2;
    private MainActivityViewPagerAdapter myAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setupRecyclerView();

        myViewPager2 = findViewById(R.id.viewPager2);
        myAdapter = new MainActivityViewPagerAdapter(this, getList(new ArrayList()));
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);
        myViewPager2.setOffscreenPageLimit(3);

        final float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        /** myViewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
        @Override public void transformPage(@NonNull View page, float position) {
        float myOffset = position * -(2 * pageOffset + pageMargin);
        if (myViewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
        if (ViewCompat.getLayoutDirection(myViewPager2) == ViewCompat.LAYOUT_DIRECTION_RTL) {
        page.setTranslationX(-myOffset);
        } else {
        page.setTranslationX(myOffset);
        }
        } else {
        page.setTranslationY(myOffset);
        }
        }
        });**/
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

        myViewPager2.setCurrentItem(1);
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        final MainActivityAdapter simpleAdapter = new MainActivityAdapter(getApplicationContext(), getList(new ArrayList()));
        mRecyclerView.setAdapter(simpleAdapter);

    }


    private ArrayList<String> getList(ArrayList<String> arrayList) {

        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0001.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0002.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0003.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0004.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0005.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0006.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0007.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0008.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0009.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0010.png");

        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00040621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00050621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00060621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00070621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00080621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00090621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00100621.png");


        return arrayList;
    }

}
