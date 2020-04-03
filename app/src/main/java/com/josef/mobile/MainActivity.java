package com.josef.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.view.View;

import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityAdapter;
import com.josef.mobile.components.MainActivityViewPagerAdapter;
import com.josef.mobile.model.MainActivityAdapterBody;

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
        myAdapter = new MainActivityViewPagerAdapter(this);
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);
        myViewPager2.setOffscreenPageLimit(3);

        final float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        myViewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
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
        });

        myViewPager2.setCurrentItem(1);
    }


    private void setupRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        final MainActivityAdapter simpleAdapter = new MainActivityAdapter(getList(new ArrayList()));
        mRecyclerView.setAdapter(simpleAdapter);
    }


    private ArrayList<MainActivityAdapterBody> getList(ArrayList<MainActivityAdapterBody> arrayList) {
        for (int i = 0; i <= 10 - 1; i++) {
            arrayList.add(new MainActivityAdapterBody("name :" + i, "description :" + i));
        }
        return arrayList;
    }

}
