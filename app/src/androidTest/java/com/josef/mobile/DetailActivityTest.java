/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.josef.mobile;

import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.josef.josefmobile.R;
import com.josef.mobile.free.ArchiveActivity;
import com.josef.mobile.free.DetailActivity;
import com.josef.mobile.free.PresenterActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static com.josef.mobile.ViewPagerActions.scrollLeft;
import static com.josef.mobile.ViewPagerActions.scrollRight;
import static com.josef.mobile.ViewPagerActions.scrollToFirst;
import static com.josef.mobile.ViewPagerActions.scrollToLast;

/**
 * Integration tests for {@link ViewPagerActions}.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public final class DetailActivityTest {

    /*+ Pitfalls : ........... net.. ! ui - fragment -1!  ......................... **/

    @Rule
    public ActivityScenarioRule<DetailActivity> activityTestRule =
            new ActivityScenarioRule<>(DetailActivity.class);

    private static IdlingResource mIdlingResource;

    @Test
    public void verify_if_coordinatorlayout_is_displayed_and_child_of_parent(){
        //ActivityScenario<DetailActivity> scenario = ActivityScenario.launch(DetailActivity.class);
        onView(withId(R.id.bottom_app_bar_coord)).check(matches(withParent(withId(R.id.main_content))));
        onView(withId(R.id.bottom_app_bar_coord)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_bottomappbar_is_displayed_and_child_of_parent(){
        //ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        onView(withId(R.id.bottom_app_bar)).check(matches(withParent(withId(R.id.bottom_app_bar_coord))));
        onView(withId(R.id.bottom_app_bar)).check(matches(isDisplayed()));

    }

    @Test
    public void verify_if_fab_is_clickable_and_child_of_parent(){
        //  ActivityScenario<ContentActivity> scenario = ActivityScenario.launch(ContentActivity.class);
        onView(withId(R.id.fab)).check(matches(withParent(withId(R.id.bottom_app_bar_coord))));
        onView(withId(R.id.fab)).check(matches(isClickable()));
    }

    @Test
    public void verify_if_menuItem_app_bar_info_is_clickable_and_performs_action(){
        ActivityScenario<DetailActivity> scenario = ActivityScenario.launch(DetailActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<DetailActivity>() {
            @Override
            public void perform(final DetailActivity activity) {
                activity.loadIntersitialAds(new InterstitialAdsRequest() {
                    @Override
                    public void execute() {
                        Intent intent = new Intent(activity.getApplicationContext(), PresenterActivity.class);
                        activity.startActivity(intent);
                    }
                });
            }
        });
        onView(withId(R.id.presenter_container)).check(matches(isDisplayed()));
        Espresso.pressBack();
        onView(withId(R.id.app_bar_info)).check(matches(isClickable()));
        onView(withId(R.id.app_bar_info)).perform(click());
    }

    @Test
    public void verify_if_menuItem_app_bar_performs_action_return_source(){
        ActivityScenario<DetailActivity> scenario = ActivityScenario.launch(DetailActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<DetailActivity>() {
            @Override
            public void perform(final DetailActivity activity) {
                activity.loadIntersitialAds(new InterstitialAdsRequest() {
                    @Override
                    public void execute() {
                        Intent intent = new Intent(activity.getApplicationContext(), PresenterActivity.class);
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
        ActivityScenario<DetailActivity> scenario = ActivityScenario.launch(DetailActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<DetailActivity>() {
            @Override
            public void perform(final DetailActivity activity) {
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

    @Test
    public void verify_if_menuItem_app_bar_archieve_performs_action_return_source(){
        ActivityScenario<DetailActivity> scenario = ActivityScenario.launch(DetailActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<DetailActivity>() {
            @Override
            public void perform(final DetailActivity activity) {
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
    public void verify_if_appBar_is_displayed_and_child_of_parent(){

        onView(withId(R.id.appBar)).check(matches(withParent(withId(R.id.main_content))));
        onView(withId(R.id.appBar)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_collapsing_toolbar_is_displayed_and_child_of_parent(){

        onView(withId(R.id.collapsing_toolbar)).check(matches(withParent(withId(R.id.appBar))));
        onView(withId(R.id.collapsing_toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_toolbar_is_displayed_and_child_of_parent(){

        onView(withId(R.id.toolbar)).check(matches(withParent(withId(R.id.collapsing_toolbar))));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_photo_is_displayed_and_child_of_parent(){

        onView(withId(R.id.photo)).check(matches(withParent(withId(R.id.collapsing_toolbar))));
        onView(withId(R.id.photo)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_nestedscrollview_is_displayed_and_child_of_parent(){

        onView(withId(R.id.nested_scrollview)).check(matches(withParent(withId(R.id.main_content))));
        onView(withId(R.id.nested_scrollview)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_viewpager_is_displayed_and_child_of_parent(){

        onView(withId(R.id.detailviewpager)).check(matches(withParent(withId(R.id.nested_scrollview))));
        onView(withId(R.id.detailviewpager)).check(matches(isDisplayed()));
    }

    @Test
    public void scrollRightThenLeft() {
        testScrollRightThenLeft(false);
    }

    @Test
    public void scrollRightThenLeft_smooth() {
        testScrollRightThenLeft(true);
    }


    private static void testScrollRightThenLeft(boolean smoothScroll) {
        onPager()
                .check(matches(isShowingPage(0)))
                .perform(scrollRight(smoothScroll))
                .check(matches(isShowingPage(1)))
                .perform(scrollLeft(smoothScroll))
                .check(matches(isShowingPage(0)));
    }

    @Test
    public void scrollToLastThenFirst() {
        testScrollToLastThenFirst(false);
    }

    @Test
    public void scrollToLastThenFirst_smooth() {
        testScrollToLastThenFirst(true);
    }


    private static void testScrollToLastThenFirst(boolean smoothScroll) {
        onPager()
                .check(matches(isShowingPage(0)))
                .perform(scrollToLast(true)).check(matches(isShowingPage(2)))
                .perform(scrollToFirst(true)).check(matches(isShowingPage(0)));
    }

    @Test
    public void scrollToPage() {

        testScrollToPage(false);

    }

    @Test
    public void scrollToPage_smooth() {
        testScrollToPage(true);
    }

    @Test
    public void scrollToPage_back_scrollToPage_back() {

        for (int index = 1; index <= 3; index++) {
         activityTestRule.getScenario().onActivity(new ActivityScenario.ActivityAction<DetailActivity>() {
                @Override
                public void perform(DetailActivity activity) {

                 }
             });
         testScrollToPage(index, false);
         }
    }

    @Test
    public void scrollToPage_back_scrollToPage_back_smooth() {

        for (int index = 1; index <= 3; index++) {
            activityTestRule.getScenario().onActivity(new ActivityScenario.ActivityAction<DetailActivity>() {
                @Override
                public void perform(DetailActivity activity) {

                }
            });
            testScrollToPage(index, true);
        }
    }

    @Test
    public void press_back_toolbar_VISIBLE() {
       // intending(hasComponent(ContentActivity.class.getName())).respondWith(new Instrumentation.ActivityResult(resultCode, dataIntent));
       // activityTestRule.getActivity().startActivityForResult(new Intent(context,DummyActivity.class));
    }

    @Test
    public void press_back_data_delivered(){

    }


    private static void testScrollToPage(final int index, boolean smoothScroll) {
        onPager().perform(ViewPagerActions.scrollToPage(index, smoothScroll))
                .check(matches(isShowingPage(index)));
    }


    private static void testScrollToPage(boolean smoothScroll) {
        onPager()
                .check(matches(isShowingPage(0)))
                .perform(ViewPagerActions.scrollToPage(2, smoothScroll))
                .check(matches(isShowingPage(2)))
                .perform(ViewPagerActions.scrollToPage(1, smoothScroll))
                .check(matches(isShowingPage(1)));
    }


    private static ViewInteraction onPager() {
        return onView(withId(R.id.detailviewpager));
    }


    private static Matcher<? super View> isShowingPage(int index) {
        //material :gold, sculpture1 :abstract
        return ViewMatchers.hasDescendant(ViewMatchers.withText("uschi"));
    }


    private static Matcher<? super View> isShowingPageId(int index) {
        //material :gold, sculpture1 :abstract
        return ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.imgBanner));
    }

}