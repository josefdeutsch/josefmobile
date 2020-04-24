package com.josef.mobile;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.josef.mobile.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public int index = 1;

    protected Intent getActivityIntent() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent result = new Intent(targetContext, MainActivity.class);
        result.putExtra(VIEWPAGER_AMOUNT, index);
        return result;
    }

    @Test
    public void lounch_mainactivity_verify_if_inputdata_machtes() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(getActivityIntent());
        scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                assertEquals(activity.index,index);
            }
        });
    }
}