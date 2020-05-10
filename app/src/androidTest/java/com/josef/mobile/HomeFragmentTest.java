package com.josef.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.josef.josefmobile.R;
import com.josef.mobile.free.ui.ContentDetailFragment;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.WORKER_DOWNLOADID;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    private IdlingResource mIdlingResource;

    @Test
    public void verify_if_article_is_Visible_matches_withText() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 1);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {
                fragment.addActionToUI(new Action() {
                    @Override
                    public void performAction(String downloadid, int index) {

                    }
                });
                fragment.mArticle.setText("test");
            }
        });
        onView(withId(R.id.article_title)).check(matches(withText("test")));
    }

    @Test
    public void verify_if_article_byline_is_Visible_matches_withText() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 3);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {
                fragment.addActionToUI(new Action() {
                    @Override
                    public void performAction(String downloadid, int index) {

                    }
                });
                fragment.mArticleByLine.setText("test");
            }
        });
        onView(withId(R.id.article_byline)).check(matches(withText("test")));
    }

    @Test
    public void verify_if_toggleButton_is_clickable() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 3);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {

            }
        });

        onView(withId(R.id.button_favorite)).check(matches(isClickable()));
    }

    @Test
    public void verify_if_toggleButton_is_clickable_unChecked() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 3);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {
            }
        });
        onView(withId(R.id.button_favorite)).check(matches(isClickable()));
        onView(withId(R.id.button_favorite)).check(matches(isNotChecked()));
    }

    @Test
    public void verify_if_toggleButton_is_isChecked_after_click() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 3);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {
            }
        });
        onView(withId(R.id.button_favorite)).perform(click());
        onView(withId(R.id.button_favorite)).check(matches(isChecked()));

    }

    @Test
    public void verify_if_toggleButton_is_unchecked_after_2xClick() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 3);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {
            }
        });
        onView(withId(R.id.button_favorite)).perform(click());
        onView(withId(R.id.button_favorite)).perform(click());
        onView(withId(R.id.button_favorite)).check(matches(isNotChecked()));
    }

    @Test
    public void verify_if_toggleButton2_is_clickable_unChecked() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 3);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {
            }
        });
        onView(withId(R.id.button_favorite2)).check(matches(isClickable()));
        onView(withId(R.id.button_favorite2)).check(matches(isNotChecked()));
    }

    @Test
    public void verify_if_toggleButton2_is_isChecked_after_click() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 3);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {
            }
        });
        onView(withId(R.id.button_favorite2)).perform(click());
        onView(withId(R.id.button_favorite2)).check(matches(isChecked()));

    }

    @Test
    public void verify_if_toggleButton2_is_unchecked_after_2xClick() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 3);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {
            }
        });
        onView(withId(R.id.button_favorite2)).perform(click());
        onView(withId(R.id.button_favorite2)).perform(click());
        onView(withId(R.id.button_favorite2)).check(matches(isNotChecked()));
    }

    @Test
    public void verify_if_simpleExoPlayer_is_displayed() {
        Bundle args = new Bundle();
        args.putString(WORKER_DOWNLOADID, "sample");
        args.putInt(VIEWPAGERDETAILKEY, 3);
        FragmentScenario<ContentDetailFragment> scenario = FragmentScenario.launchInContainer(ContentDetailFragment.class, args);
        scenario.onFragment(new FragmentScenario.FragmentAction<ContentDetailFragment>() {
            @Override
            public void perform(@NonNull ContentDetailFragment fragment) {
            }
        });
        onView(withId(R.id.exoplayer)).check(matches(isDisplayed()));
    }
}