package com.josef.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityAdapter;
import com.josef.mobile.components.MainActivityViewPagerAdapter;
import com.josef.mobile.components.MyAdapter;
import com.josef.mobile.model.MainActivityAdapterBody;

import java.util.ArrayList;

import static androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL;

public class MainActivity extends AppCompatActivity implements MainActivityViewPagerFragment.OnFragmentInteractionListener {

    private MainActivityViewPagerAdapter mAdapter;
    private ViewPager2 mViewPager2;
    ViewPager2 myViewPager2;
    private MyAdapter myAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        setupRecyclerView();

        //setupAdapter();
        //registerListener();


            // Initializing view pager
        myViewPager2 = findViewById(R.id.viewPager2);

        myAdapter = new MyAdapter(this);
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        myViewPager2.setAdapter(myAdapter);
        myViewPager2.setOffscreenPageLimit(3);

       final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
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
        /**myViewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (position < -1) {
                    page.setTranslationX(-myOffset);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setTranslationX(myOffset);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0);
                    page.setTranslationX(myOffset);
                }
            }
        });
**/

        /**myViewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (position < -1) {
                    page.setTranslationX(-myOffset);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setTranslationX(myOffset);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0);
                    page.setTranslationX(myOffset);
                }
            }
        });**/


    }



    private void registerListener() {
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
    }

    private void setupAdapter() {
        mViewPager2 = findViewById(R.id.viewPager2);
        mAdapter = new MainActivityViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        mViewPager2.setOrientation(ORIENTATION_HORIZONTAL);
        mViewPager2.setAdapter(mAdapter);
    }


    private void setupRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        final MainActivityAdapter simpleAdapter = new MainActivityAdapter(getList(new ArrayList()));
        mRecyclerView.setAdapter(simpleAdapter);
    }




    private ArrayList<MainActivityAdapterBody> getList(ArrayList<MainActivityAdapterBody> arrayList){
        for (int i = 0; i <= 10 -1 ; i++) {
            arrayList.add(new MainActivityAdapterBody("name :"+i,"description :"+i));
        }
        return arrayList;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
