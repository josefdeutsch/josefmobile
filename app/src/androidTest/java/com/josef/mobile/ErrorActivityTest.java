package com.josef.mobile;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.josef.josefmobile.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import static org.junit.Assert.*;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ErrorActivityTest {

    @Test
public void hure(){
    try (ActivityScenario<SplashActivity> scenario = ActivityScenario.launch(SplashActivity.class)) {
        scenario.onActivity(new ActivityScenario.ActivityAction<SplashActivity>() {
            @Override
            public void perform(SplashActivity activity) {

            }
        });
    }
    MockWebServer webServer = new MockWebServer();
    webServer.enqueue(new MockResponse().setResponseCode(404));
    HttpUrl baseUrl = webServer.url("api/key");
    try {
//            webServer.start();
        if(getResponseCode(new OkHttpClient(),baseUrl)>=300){

        }
    } catch (IOException e) {
        e.printStackTrace();
    }

}
    final Dispatcher dispatcher = new Dispatcher() {
        @Override
        public MockResponse dispatch (RecordedRequest request) throws InterruptedException {
            return new MockResponse().setResponseCode(404);
        }
    };

    private int getResponseCode(OkHttpClient client,HttpUrl base) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),"hi there");
        okhttp3.Request request = new okhttp3.Request.Builder().post(body).url(base).build();
        Response response = client.newCall(request).execute();
        return response.code();
    }


    @Test
    public void fdfgdfg() {
        ActivityScenario<ErrorActivity> scenario = ActivityScenario.launch(ErrorActivity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<ErrorActivity>() {
            @Override
            public void perform(ErrorActivity activity) {
                //ErrorActivity
                assertEquals("ErrorActivity","ErrorActivity");
            }
        });

    }
}