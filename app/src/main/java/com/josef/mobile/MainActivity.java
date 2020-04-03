package com.josef.mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.net.Uri;
import android.os.Bundle;

import com.josef.josefmobile.R;
import com.josef.mobile.components.MainActivityAdapter;
import com.josef.mobile.components.MainActivityViewPagerAdapter;
import com.josef.mobile.free.DetailActivity;
import com.josef.mobile.model.MainActivityAdapterBody;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityViewPagerFragment.OnFragmentInteractionListener {

    private MainActivityViewPagerAdapter mAdapter;
    private ViewPager2 mViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecyclerView();
        setupAdapter();
        registerListener();
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
        mViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
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
