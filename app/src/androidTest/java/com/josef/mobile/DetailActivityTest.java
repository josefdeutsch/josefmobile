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
import com.josef.mobile.free.DetailActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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

    /*+ Pitfalls : ...........net ! ui - fragment -1!  ......................... **/

    @Rule
    public ActivityScenarioRule<DetailActivity> activityTestRule =
            new ActivityScenarioRule<>(DetailActivity.class);

    private static IdlingResource mIdlingResource;

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
    public void scrollToPage_with_NetworkAccess() {

        for (int index = 1; index <= 3; index++) {
            activityTestRule.getScenario().onActivity(new ActivityScenario.ActivityAction<DetailActivity>() {
                @Override
                public void perform(DetailActivity activity) {
                    mIdlingResource = activity.getIdlingResource();
                    IdlingRegistry.getInstance().register(mIdlingResource);
                }
            });
            testScrollToPage(index, false);
            IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
        }
    }

    @Test
    public void scrollToPage_smooth_with_NetworkAccess() {

        for (int index = 1; index <= 3; index++) {
            activityTestRule.getScenario().onActivity(new ActivityScenario.ActivityAction<DetailActivity>() {
                @Override
                public void perform(DetailActivity activity) {
                    mIdlingResource = activity.getIdlingResource();
                    IdlingRegistry.getInstance().register(mIdlingResource);
                }
            });
            testScrollToPage(index, true);
            IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
        }
    }

    @Test
    public void press_back_toolbar_VISIBLE() {
       // intending(hasComponent(HomeActivity.class.getName())).respondWith(new Instrumentation.ActivityResult(resultCode, dataIntent));
       // activityTestRule.getActivity().startActivityForResult(new Intent(context,DummyActivity.class));
    }

    @Test
    public void press_back_data_delivered(){

    }

    private static void testScrollToPage(final int index, boolean smoothScroll) {
        onPager().perform(ViewPagerActions.scrollToPage(index, smoothScroll))
                .check(matches(isShowingPage(index)));
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