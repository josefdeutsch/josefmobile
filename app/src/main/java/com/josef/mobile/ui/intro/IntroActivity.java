package com.josef.mobile.ui.intro;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.RequestManager;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.josef.mobile.R;
import com.josef.mobile.models.Change;
import com.josef.mobile.models.ContainerOnBoard;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class IntroActivity extends DaggerAppCompatActivity {

    private static final String TAG = "AuthActivity";

    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    Drawable logo;
    @Inject
    RequestManager requestManager;

    ViewPager2 viewPager2;
    IntroRecyclerAdapter recyclerAdapter;
    MaterialButton nextButton;
    MaterialButton skipButton;
    private IntroViewModel viewModel;

    int last;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        nextButton = findViewById(R.id.buttonPanel);
        skipButton = findViewById(R.id.textButton);
        recyclerAdapter = new IntroRecyclerAdapter(this);
        viewModel = new ViewModelProvider(this, providerFactory).get(IntroViewModel.class);
        viewPager2 = findViewById(R.id.viewpager);

        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setAdapter(recyclerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e("Selected_Page", String.valueOf(position));
                last = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        subscribeObservers();

    }



    private void subscribeObservers() {
        viewModel.observePosts().observe(this, new Observer<Resource<Change>>() {
            @Override
            public void onChanged(Resource<Change> changeResource) {
                if (changeResource != null) {
                    switch (changeResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: PostsFragment: LOADING...");
                            break;
                        }

                        case SUCCESS: {
                            Gson gson = new Gson();
                            Type userListType = new TypeToken<ArrayList<ContainerOnBoard>>() {
                            }.getType();
                            ArrayList<ContainerOnBoard> userArray = gson.fromJson(changeResource.data.message, userListType);
                            recyclerAdapter.setIntro(userArray);
                            break;
                        }

                        case ERROR: {
                            Log.d(TAG, "onChanged: PostsFragment: ERROR... ");
                            break;
                        }
                    }
                }
            }
        });
    }


    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.buttonPanel) {

            int current = viewPager2.getCurrentItem() + 1;
            viewPager2.setCurrentItem(current, true);
            if (recyclerAdapter.getItemCount() == current)
                startActivity(new Intent(IntroActivity.this, MainActivity.class));

        } else if (i == R.id.textButton) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        }
    }

}