package com.josef.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.josef.josefmobile.R;
import com.josef.mobile.free.ArchiveActivity;
import com.josef.mobile.free.DetailActivity;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.josef.mobile.Config.VIEWPAGERDETAILKEY;
import static com.josef.mobile.Config.VIEWPAGERMAINKEY;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    private IdlingResource mIdlingResource;

    @Test
    public void verify_if_article_is_Visible_matches_withText(){
            Bundle args = new Bundle();
            args.putInt(VIEWPAGERDETAILKEY,1);
            args.putInt(VIEWPAGERDETAILKEY,3);
            FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
            scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
                @Override
                public void perform(@NonNull HomeFragment fragment) {

                }
            });
            onView(withId(R.id.article_title)).check(matches(withText("hello")));
        }

    @Test
    public void verify_if_article_byline_is_Visible_matches_withText(){
            Bundle args = new Bundle();
            args.putInt(VIEWPAGERDETAILKEY,1);
            args.putInt(VIEWPAGERDETAILKEY,3);
            FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
            scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
                 @Override
                 public void perform(@NonNull HomeFragment fragment) {
                     mIdlingResource = fragment.getIdlingResource();
                     IdlingRegistry.getInstance().register(mIdlingResource);
            }
        });
        onView(withId(R.id.article_byline)).check(matches(withText("material :gold, sculpture2 :abstract")));
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void verify_if_toggleButton_is_clickable(){
            Bundle args = new Bundle();
            args.putInt(VIEWPAGERDETAILKEY,1);
            args.putInt(VIEWPAGERDETAILKEY,3);
            FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
            scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
                @Override
                public void perform(@NonNull HomeFragment fragment) {

                }
            });

            onView(withId(R.id.button_favorite)).check(matches(isClickable()));
    }

    @Test
    public void verify_if_toggleButton_is_clickable_unChecked(){
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERDETAILKEY,1);
        args.putInt(VIEWPAGERDETAILKEY,3);
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment fragment) {
            }
        });
        onView(withId(R.id.button_favorite)).check(matches(isClickable()));
        onView(withId(R.id.button_favorite)).check(matches(isNotChecked()));
    }

    @Test
    public void verify_if_toggleButton_is_isChecked_after_click(){
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERDETAILKEY,1);
        args.putInt(VIEWPAGERDETAILKEY,3);
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment fragment) {
            }
        });
        onView(withId(R.id.button_favorite)).perform(click());
        onView(withId(R.id.button_favorite)).check(matches(isChecked()));

    }

    @Test
    public void verify_if_toggleButton_is_unchecked_after_2xClick(){
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERDETAILKEY,1);
        args.putInt(VIEWPAGERDETAILKEY,3);
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment fragment) {
            }
        });
        onView(withId(R.id.button_favorite)).perform(click());
        onView(withId(R.id.button_favorite)).perform(click());
        onView(withId(R.id.button_favorite)).check(matches(isNotChecked()));
    }

    @Test
    public void verify_if_toggleButton_button_produces_stringset(){
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERDETAILKEY,1);
        args.putInt(VIEWPAGERDETAILKEY,3);
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment fragment) {
                Context context = fragment.getContext();
                mIdlingResource = fragment.getIdlingResource();
                IdlingRegistry.getInstance().register(mIdlingResource);
                fragment.shareMetaData(1);
                fragment.buttonFavorite.performClick();
                ArrayList<String> arrayList = new ArrayList<>(AppPreferences.getName(context));
                String str = arrayList.get(0);
                String exp = "http://joseph3d.com/wp-content/uploads/2019/06/g0001.mp4";
                assertEquals(exp,str);
            }
        });
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void verify_if_imagview_is_visible(){
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERDETAILKEY,1);
        args.putInt(VIEWPAGERDETAILKEY,3);
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment fragment) {

            }
        });
        onView(withId(R.id.imgBanner)).check(matches(isDisplayed()));
    }

    @Test
    public void verify_if_imagview_is_clickable(){
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERDETAILKEY,1);
        args.putInt(VIEWPAGERDETAILKEY,3);
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment fragment) {

            }
        });
        onView(withId(R.id.imgBanner)).check(matches(isClickable()));
    }

    @Test
    public void verify_if_imagview_is_lounches_detailactivity(){
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERDETAILKEY,1);
        args.putInt(VIEWPAGERDETAILKEY,3);
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment fragment) {
                mIdlingResource = fragment.getIdlingResource();
                IdlingRegistry.getInstance().register(mIdlingResource);
            }
        });
        onView(withId(R.id.imgBanner)).perform(click());
        onView(withId(R.id.detailviewpager)).check(matches(isDisplayed()));
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void verify_if_menuItem_app_bar_archieve_is_clickable_and_performs_action(){
        Bundle args = new Bundle();
        args.putInt(VIEWPAGERDETAILKEY,1);
        args.putInt(VIEWPAGERDETAILKEY,3);
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class,args);
        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull final HomeFragment fragment) {
                fragment.loadIntersitialAds(new InterstitialAdsRequest() {
                    @Override
                    public void execute() {
                        Intent intent = new Intent(fragment.getContext(), DetailActivity.class);
                        intent.putExtra(VIEWPAGERMAINKEY,1);
                        intent.putExtra(VIEWPAGERDETAILKEY,3);
                        fragment.startActivity(intent);
                    }
                });
            }
        });
//Error performing 'single click - At Coordinates: 719, 887 and precision: 16, 16' on view 'Animations or transitions are enabled on the target device.
        onView(withId(R.id.detailviewpager)).check(matches(isDisplayed()));
        Espresso.pressBack();
        onView(withId(R.id.imgBanner)).check(matches(isClickable()));
        onView(withId(R.id.imgBanner)).perform(click());
    }
}