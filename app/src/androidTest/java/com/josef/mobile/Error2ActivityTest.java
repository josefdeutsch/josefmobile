package com.josef.mobile;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
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
@RunWith(AndroidJUnit4.class)
public class Error2ActivityTest {

    @Test
    public void fdfgdfg() {
        ActivityScenario<Error2Activity> scenario = ActivityScenario.launch(Error2Activity.class);
        scenario.onActivity(new ActivityScenario.ActivityAction<Error2Activity>() {
            @Override
            public void perform(Error2Activity activity) {
                //ErrorActivity
                assertEquals("ErrorActivity2",activity.TAG);
            }
        });

    }
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
                ActivityScenario<Error2Activity> scenario = ActivityScenario.launch(Error2Activity.class);
                scenario.onActivity(new ActivityScenario.ActivityAction<Error2Activity>() {
                    @Override
                    public void perform(Error2Activity activity) {
                        //ErrorActivity
                        assertEquals("ErrorActivity2",activity.TAG);
                    }
                });
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



}