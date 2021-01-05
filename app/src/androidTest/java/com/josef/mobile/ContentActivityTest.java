package com.josef.mobile;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.josef.mobile.free.ui.ArchiveActivity;
import com.josef.mobile.vfree.utils.InterstitialAdsRequest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;

;

@RunWith(AndroidJUnit4.class)
public class ContentActivityTest {

    private IdlingResource mIdlingResource;

    @Test
    public void verify_if_coordinatorlayout_is_displayed_and_child_of_parent(){

        ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        onView(withId(R.id.bottom_app_bar_coord)).check(matches(withParent(withId(R.id.main_content))));
        onView(withId(R.id.bottom_app_bar_coord)).check(matches(isDisplayed()));

    }

    @Test
    public void verify_if_bottomappbar_is_displayed_and_child_of_parent(){
        ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        onView(withId(R.id.bottom_app_bar)).check(matches(withParent(withId(R.id.bottom_app_bar_coord))));
        onView(withId(R.id.bottom_app_bar)).check(matches(isDisplayed()));

    }

    @Test
    public void verify_if_fab_is_clickable_and_child_of_parent(){
        ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        onView(withId(R.id.fab)).check(matches(withParent(withId(R.id.bottom_app_bar_coord))));
        onView(withId(R.id.fab)).check(matches(isClickable()));
    }



    @Test
    public void verify_if_appBar_is_displayed_and_child_of_parent(){
        ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        onView(withId(R.id.appBar)).check(matches(withParent(withId(R.id.main_content))));
        onView(withId(R.id.appBar)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_collapsing_toolbar_is_displayed_and_child_of_parent(){
        ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        onView(withId(R.id.collapsing_toolbar)).check(matches(withParent(withId(R.id.appBar))));
        onView(withId(R.id.collapsing_toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_toolbar_is_displayed_and_child_of_parent(){
        ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        onView(withId(R.id.toolbar)).check(matches(withParent(withId(R.id.collapsing_toolbar))));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_photo_is_displayed_and_child_of_parent(){
        ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        onView(withId(R.id.photo)).check(matches(withParent(withId(R.id.collapsing_toolbar))));
        onView(withId(R.id.photo)).check(matches(isDisplayed()));
    }


    @Test
    public void verify_if_menuItem_app_bar_archieve_performs_action_return_source(){
        ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<ContentActivity>() {
            @Override
            public void perform(final ContentActivity activity) {
                activity.loadIntersitialAds(new InterstitialAdsRequest() {
                    @Override
                    public void execute() {
                        Intent intent = new Intent(activity.getApplicationContext(), ArchiveActivity.class);
                        activity.startActivity(intent);
                    }
                });
            }
        });
        Espresso.pressBack();
        onView(withId(R.id.main_content)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_menuItem_app_bar_archieve_is_clickable_and_performs_action(){
        ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<ContentActivity>() {
            @Override
            public void perform(final ContentActivity activity) {
                activity.loadIntersitialAds(new InterstitialAdsRequest() {
                    @Override
                    public void execute() {
                        Intent intent = new Intent(activity.getApplicationContext(), ArchiveActivity.class);
                        activity.startActivity(intent);
                    }
                });
            }
        });
        onView(withId(R.id.archieve_container)).check(matches(isDisplayed()));
        Espresso.pressBack();
        onView(withId(R.id.app_bar_archieve)).check(matches(isClickable()));
        onView(withId(R.id.app_bar_archieve)).perform(click());

    }

}