package com.josef.mobile;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.josef.josefmobile.R;
import com.josef.mobile.idlingres.EspressoIdlingResource;
import com.josef.mobile.ui.SplashActivity;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SplashActivityTest {

    private IdlingResource mIdlingResource;
    @Test
    public void press_button_verify_if_mainactivity_with_fragment_exist(){
        try (ActivityScenario<SplashActivity> scenario = ActivityScenario.launch(SplashActivity.class)) {
            scenario.onActivity(new ActivityScenario.ActivityAction<SplashActivity>() {
                @Override
                public void perform(SplashActivity activity) {
                    mIdlingResource = activity.getIdlingResource();
                    IdlingRegistry.getInstance().register(mIdlingResource);
                }
            });
            onView(withId(R.id.logo)).perform(click());
            IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
        }
    }

}