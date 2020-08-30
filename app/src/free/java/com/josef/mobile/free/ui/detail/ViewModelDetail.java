package com.josef.mobile.free.ui.detail;

import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewModelDetail extends ViewModel {

    public static final String JSON_METADATA = "metadata";
    public static final String JSON_NAME = "name";
    public static final String JSON_PNG = "png";
    public static final String JSON_URL = "url";

    public static final String STATE_RESUME_WINDOW = "com.josef.mobile.free.ui.detail.ContentDetailFragment.resumeWindow";
    public static final String STATE_RESUME_POSITION = "com.josef.mobile.free.ui.detail.ContentDetailFragment.resumePosition";
    public static final String STATE_BOOLEAN_VALUE = "com.josef.mobile.free.ui.detail.ContentDetailFragment.value";


    protected String getJsonPng(String output, int index) throws JSONException {
        JSONArray input = new JSONArray(output);
        JSONObject container = input.getJSONObject(index);
        JSONObject metadata = (JSONObject) container.get(JSON_METADATA);
        String png = (String) metadata.get(JSON_PNG);
        return png;
    }

    protected String getJsonUrl(String output, int index) throws JSONException {
        JSONArray input = new JSONArray(output);
        JSONObject container = null;
        container = input.getJSONObject(index);
        JSONObject metadata = (JSONObject) container.get(JSON_METADATA);
        String url = (String) metadata.get(JSON_URL);
        return url;
    }

    protected String getJsonName(String output, int index) throws JSONException {
        JSONArray txt = new JSONArray(output);
        JSONObject container = txt.getJSONObject(index);
        JSONObject metadata = (JSONObject) container.get(JSON_METADATA);
        String name = (String) metadata.get(JSON_NAME);
        return name;
    }
    protected ScaleAnimation getScaleAnimation(){

        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        return scaleAnimation;
    }



}
