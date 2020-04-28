package com.josef.mobile;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.josef.josefmobile.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;


@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    //https://developer.android.com/training/testing/espresso/recipes

    @Test
    public void verify_if_coordinatorlayout_is_displayed_and_child_of_parent(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.bottom_app_bar_coord)).check(matches(withParent(withId(R.id.main_content))));
        onView(withId(R.id.bottom_app_bar_coord)).check(matches(isDisplayed()));

    }

    @Test
    public void verify_if_bottomappbar_is_displayed_and_child_of_parent(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.bottom_app_bar)).check(matches(withParent(withId(R.id.bottom_app_bar_coord))));
        onView(withId(R.id.bottom_app_bar)).check(matches(isDisplayed()));

    }

    @Test
    public void verify_if_fab_is_clickable_and_child_of_parent(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.fab)).check(matches(withParent(withId(R.id.bottom_app_bar_coord))));
        onView(withId(R.id.fab)).check(matches(isClickable()));
    }

    @Test
    public void verify_if_menuItem_app_bar_info_is_clickable_and_performs_action(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.app_bar_info)).check(matches(isClickable()));
        onView(withId(R.id.app_bar_info)).perform(click());
        onView(withId(R.id.presenter_container)).check(matches(isDisplayed()));
    }
    @Test
    public void verify_if_menuItem_app_bar_performs_action_return_source(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.app_bar_info)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.main_content)).check(matches(isDisplayed()));
    }

       @Test
    public void verify_if_menuItem_app_bar_archieve_is_clickable_and_performs_action(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.app_bar_archieve)).check(matches(isClickable()));
        onView(withId(R.id.app_bar_archieve)).perform(click());
        onView(withId(R.id.archieve_container)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_menuItem_app_bar_archieve_performs_action_return_source(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.app_bar_archieve)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.main_content)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_appBar_is_displayed_and_child_of_parent(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.appBar)).check(matches(withParent(withId(R.id.main_content))));
        onView(withId(R.id.appBar)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_collapsing_toolbar_is_displayed_and_child_of_parent(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.collapsing_toolbar)).check(matches(withParent(withId(R.id.appBar))));
        onView(withId(R.id.collapsing_toolbar)).check(matches(isDisplayed()));
    }


    @Test
    public void verify_if_toolbar_is_displayed_and_child_of_parent(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.toolbar)).check(matches(withParent(withId(R.id.collapsing_toolbar))));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }


    @Test
    public void verify_if_photo_is_displayed_and_child_of_parent(){
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(HomeActivity.class);
        onView(withId(R.id.photo)).check(matches(withParent(withId(R.id.collapsing_toolbar))));
        onView(withId(R.id.photo)).check(matches(isDisplayed()));
    }

}