package com.josef.mobile;

import android.content.Context;

import androidx.core.widget.TextViewCompat;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.josef.josefmobile.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Test
    public void verify_if_share_button_is_visible(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<HomeActivity>() {
            @Override
            public void perform(HomeActivity activity) {

            }
        });
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_share_button_is_clickable(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<HomeActivity>() {
            @Override
            public void perform(HomeActivity activity) {

            }
        });
        onView(withId(R.id.fab)).check(matches(isClickable()));
    }

    @Test
    public void verify_if_share_button_is_clickable2(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<HomeActivity>() {
            @Override
            public void perform(HomeActivity activity) {

            }
        });
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        //onView(withId(R.id.fab)).check(matches(isClickable()));
    }

    @Test
    public void verify_if_share_button_is_clickable3(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<HomeActivity>() {
            @Override
            public void perform(HomeActivity activity) {

            }
        });
        onView(withId(R.id.bottom_app_bar)).check(matches(isDisplayed()));
       // onView(withId(R.string.app)).check(matches(withParent(withId(R.id.toolbar))));
        //onView(withId(R.id.fab)).check(matches(isClickable()));
    }

}