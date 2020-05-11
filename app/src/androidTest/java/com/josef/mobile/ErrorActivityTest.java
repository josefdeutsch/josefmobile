package com.josef.mobile;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.josef.josefmobile.R;
import com.josef.mobile.ui.ErrorActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import static org.junit.Assert.*;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ErrorActivityTest {

    //https://riptutorial.com/android/example/12328/mockwebserver-example
    //https://github.com/square/okhttp/tree/master/mockwebserver
    //https://www.youtube.com/results?search_query=mockwebserver+android

    private static final String TAG = "ErrorActivityTest";
    @Test
    public void test_if_erroractivity_exsist() {
        ActivityScenario<ErrorActivity> scenario = ActivityScenario.launch(ErrorActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<ErrorActivity>() {
            @Override
            public void perform(ErrorActivity activity) {
                assertEquals("ErrorActivity",activity.TAG);
            }
        });
    }
    @Test
    public void test_if_erroractivity_is_init_badrequest() throws IOException {

        MockWebServer webServer = new MockWebServer();
        OkHttpClient client = new OkHttpClient();
        webServer.enqueue(new MockResponse().setResponseCode(404));

        HttpUrl base= webServer.url("api/key");
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),"hi there");
        final okhttp3.Request request = new okhttp3.Request.Builder().post(body).url(base).build();
        Response response = client.newCall(request).execute();

        if(response.code()>=300){
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, ErrorActivity.class);
            ActivityScenario.launch(result);
        }

        webServer.shutdown();
        onView(withId(R.id.errorView)).check(matches(isDisplayed()));
    }
}