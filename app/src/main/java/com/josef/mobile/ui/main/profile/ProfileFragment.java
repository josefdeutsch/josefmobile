package com.josef.mobile.ui.main.profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.josef.mobile.R;
import com.josef.mobile.ui.base.BaseFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class ProfileFragment extends BaseFragment {

    private static final String TAG = "ProfileFragment";
    ViewPager2 viewPager2;

    TabLayout tabLayout;

    @Inject
    ViewPagerAdapter viewPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> strings = new ArrayList<>();

        strings.add("");
        strings.add("Digital art is true");
        strings.add("Choose artworks");
        strings.add("Sync account");
        strings.add("Press play!");

        String file = "http://joseph3d.com/wp-content/uploads/2020/11/LogoAnimatedBlack.gif";

        ArrayList<String> uri = new ArrayList<>();
        for (int i = 0; i <= 6 - 1; i++) {
            uri.add(file);
        }

        viewPager2 = view.findViewById(R.id.viewPager2);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        //   viewPager2.setPageTransformer(new DepthPageTransformer());
        // view.setTranslationX(-1 * view.getWidth() * 1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                //TextView textView = viewPager2.findViewWithTag(R.id.article);
                // fadeInAnimation(textView);
            }
        });

        viewPager2.setAdapter(viewPagerAdapter);
        viewPagerAdapter.setArrayList(strings, uri);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {

        }).attach();
    }

    void fadeOutAnimation(View viewToFadeOut) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(viewToFadeOut, "alpha", 1f, 0f);

        fadeOut.setDuration(1000);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewToFadeOut.setVisibility(View.GONE);
            }
        });

        fadeOut.start();
    }

    void fadeInAnimation(View viewToFadeIn) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(viewToFadeIn, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);

        fadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                viewToFadeIn.setVisibility(View.VISIBLE);
                viewToFadeIn.setAlpha(0);
            }
        });

        fadeIn.start();
    }


}
