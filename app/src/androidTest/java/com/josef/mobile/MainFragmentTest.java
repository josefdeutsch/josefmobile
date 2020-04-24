package com.josef.mobile;

import android.os.Bundle;


import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.viewpager.widget.ViewPager;

import com.josef.josefmobile.R;

import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;
import static org.hamcrest.core.AllOf.allOf;
import static androidx.test.espresso.action.ViewActions.*;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.viewpager.widget.PagerTitleStrip;
import androidx.viewpager.widget.ViewPager;
import org.hamcrest.Matcher;

@RunWith(AndroidJUnit4.class)
public class MainFragmentTest {


    @Rule
    public ActivityScenarioRule<MainActivity> activityTestRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Test
    public void lounch_mainfragment_verify_if_one_viewpager_exist() {
        Bundle args = new Bundle();
        args.putInt(VIEWPAGER_AMOUNT,1);
        FragmentScenario<MainFragment> fragmentScenario =
                FragmentScenario.launchInContainer(MainFragment.class, args);
        fragmentScenario.onFragment(new FragmentScenario.FragmentAction<MainFragment>() {
            @Override
            public void perform(MainFragment fragment) {
                mIdlingResource = fragment.getIdlingResource();
                IdlingRegistry.getInstance().register(mIdlingResource);
                }
        });
        onView(allOf(withId(R.id.viewPager), isDisplayed())).perform(click());
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }


}