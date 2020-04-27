package com.josef.mobile;

import android.content.Context;
import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
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
