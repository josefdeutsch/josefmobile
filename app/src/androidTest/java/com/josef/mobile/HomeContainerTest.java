package com.josef.mobile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.josef.josefmobile.R;
import com.josef.mobile.free.DetailActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.josef.mobile.Config.VIEWPAGERMAINKEY;
import static com.josef.mobile.ViewPagerActions.scrollLeft;
import static com.josef.mobile.ViewPagerActions.scrollRight;
import static com.josef.mobile.ViewPagerActions.scrollToFirst;
import static com.josef.mobile.ViewPagerActions.scrollToLast;


@RunWith(AndroidJUnit4.class)
public class HomeContainerTest {

    private IdlingResource mIdlingResource;

    @Test
    public void verfiy_viewpager_is_visible(){
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERMAINKEY,1);
        FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
            @Override
            public void perform(@NonNull HomeContainer fragment) {

            }
        });
        onView(withId(R.id.viewidpager)).check(matches(isDisplayed()));
    }

    @Test
    public void scrollRightThenLeft() {
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERMAINKEY,1);
        FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
            @Override
            public void perform(@NonNull HomeContainer fragment) {

            }
        });
        testScrollRightThenLeft(false);
    }

    @Test
    public void scrollRightThenLeft_smooth() {
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERMAINKEY,1);
        FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
            @Override
            public void perform(@NonNull HomeContainer fragment) {

            }
        });
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
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERMAINKEY,1);
        FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
            @Override
            public void perform(@NonNull HomeContainer fragment) {

            }
        });
        testScrollToLastThenFirst(false);
    }

    @Test
    public void scrollToLastThenFirst_smooth() {
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERMAINKEY,1);
        FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
            @Override
            public void perform(@NonNull HomeContainer fragment) {

            }
        });
        testScrollToLastThenFirst(true);
    }

    private static void testScrollToLastThenFirst(boolean smoothScroll) {
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERMAINKEY,1);
        FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
            @Override
            public void perform(@NonNull HomeContainer fragment) {

            }
        });
        onPager()
                .check(matches(isShowingPage(0)))
                .perform(scrollToLast(smoothScroll))
                .check(matches(isShowingPage(2)))
                .perform(scrollToFirst(smoothScroll))
                .check(matches(isShowingPage(0)));
    }

    @Test
    public void scrollToPage() {
        Bundle args = new Bundle();
        FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
            @Override
            public void perform(@NonNull HomeContainer fragment) {

            }
        });
        testScrollToPage(false);
    }

    @Test
    public void scrollToPage_smooth() {
        Bundle args = new Bundle();
        FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
            @Override
            public void perform(@NonNull HomeContainer fragment) {

            }
        });
        testScrollToPage(true);
    }

    @Test
    public void scrollToPage_back_scrollToPage_back() {

        for (int index = 1; index <= 3; index++) {
            Bundle args = new Bundle();
            FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
            scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
                @Override
                public void perform(@NonNull HomeContainer fragment) {

                }
            });
            testScrollToPage(index, false);

        }
    }

    @Test
    public void scrollToPage_back_scrollToPage_back_smooth() {

        for (int index = 1; index <= 3; index++) {
            Bundle args = new Bundle();
            FragmentScenario<HomeContainer> scenario = FragmentScenario.launchInContainer(HomeContainer.class,args);
            scenario.onFragment(new FragmentScenario.FragmentAction<HomeContainer>() {
                @Override
                public void perform(@NonNull HomeContainer fragment) {

                }
            });
            testScrollToPage(index, true);
        }
    }

    private static void testScrollToPage(boolean smoothScroll) {
        onPager()
                .check(matches(isShowingPage(0)))
                .perform(ViewPagerActions.scrollToPage(2, smoothScroll))
                .check(matches(isShowingPage(2)))
                .perform(ViewPagerActions.scrollToPage(1, smoothScroll))
                .check(matches(isShowingPage(1)));
    }

    private static void testScrollToPage(final int index,boolean smoothScroll) {
        onPager().perform(ViewPagerActions.scrollToPage(index, smoothScroll))
                .check(matches(isShowingPage(index)));
    }

    private static ViewInteraction onPager() {
        return onView(withId(R.id.viewidpager));
    }

    private static Matcher<? super View> isShowingPage(int index) {
        return ViewMatchers.hasDescendant(ViewMatchers.withText("hello"));
    }

}