package com.josef.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityAdapter;
import com.josef.mobile.components.MainActivityViewPagerAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewPagerAdapter mAdapter;
    private ViewPager2 mViewPager2;
    ViewPager2 myViewPager2;
    private MainActivityViewPagerAdapter myAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    BottomAppBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the navigation click by showing a BottomDrawer etc.
            }
        });
        bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

        setupRecyclerView();
        final NestedScrollView scrollView = findViewById(R.id.nested_scrollview);

        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getScrollY() == 0) {
                            bar.performShow();
                        } else if (scrollView.getChildAt(0).getBottom()
                                <= (scrollView.getHeight() + scrollView.getScrollY())) {
                            bar.performHide();
                        } else {
                            //scroll view is not at bottom
                        }
                    }
                });


    }

    private void setupScrollView(final String data, final NestedScrollView scrollView) {
        if (scrollView != null) {
            scrollView.getViewTreeObserver()
                    .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {


                        @Override
                        public void onScrollChanged() {
                            if (scrollView.getChildAt(0).getBottom()
                                    <= (scrollView.getHeight() + scrollView.getScrollY())) {
                                bar.performHide();
                            } else {
                                bar.performShow();
                            }
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private static final String TAG = "MainActivity";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
        } else if (item.getItemId() == R.id.app_bar_mail) {
            Log.d(TAG, "onOptionsItemSelected: ");
            bar.performHide();

        } else if (item.getItemId() == R.id.app_bar_delete) {
            bar.performShow();

        } else if (item.getItemId() == R.id.app_bar_archieve) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        final MainActivityAdapter simpleAdapter = new MainActivityAdapter(getApplicationContext(), getList(new ArrayList()));
        mRecyclerView.setAdapter(simpleAdapter);

    }

    private void setupViewPager() {


        /**  myViewPager2 = findViewById(R.id.viewPager);
         myAdapter = new MainActivityViewPagerAdapter(this, getList(new ArrayList()));
         myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
         myViewPager2.setAdapter(myAdapter);
         myViewPager2.setOffscreenPageLimit(3);

         final float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
         final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

         //https://proandroiddev.com/look-deep-into-viewpager2-13eb8e06e419
         myViewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
        @Override public void transformPage(@NonNull View page, float position) {
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

         myViewPager2 = findViewById(R.id.viewPager2);
         myAdapter = new MainActivityViewPagerAdapter(this, getList2(new ArrayList()));
         myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
         myViewPager2.setAdapter(myAdapter);
         myViewPager2.setOffscreenPageLimit(3);

         //https://proandroiddev.com/look-deep-into-viewpager2-13eb8e06e419
         myViewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
        @Override public void transformPage(@NonNull View page, float position) {
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
         myViewPager2 = findViewById(R.id.viewPager3);
         myAdapter = new MainActivityViewPagerAdapter(this, getList3(new ArrayList()));
         myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
         myViewPager2.setAdapter(myAdapter);
         myViewPager2.setOffscreenPageLimit(3);

         //https://proandroiddev.com/look-deep-into-viewpager2-13eb8e06e419
         myViewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
        @Override public void transformPage(@NonNull View page, float position) {
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


         // myViewPager2.setCurrentItem(1);
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

    }

    private ArrayList<String> getList(ArrayList<String> arrayList) {

        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0001.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0002.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/0003.png");


        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030621.png");


        return arrayList;
    }

    private ArrayList<String> getList2(ArrayList<String> arrayList) {

        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030621.png");


        return arrayList;
    }

    private ArrayList<String> getList3(ArrayList<String> arrayList) {

        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00020621.png");
        arrayList.add("http://joseph3d.com/wp-content/uploads/2019/06/00030621.png");


        return arrayList;
    }


}
