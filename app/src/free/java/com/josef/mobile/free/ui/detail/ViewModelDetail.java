package com.josef.mobile.free.ui.detail;

import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.josef.mobile.free.ui.content.ViewModelContent.JSON_COLORS;
import static com.josef.mobile.free.ui.content.ViewModelContent.JSON_ENPOINTS;
import static com.josef.mobile.free.ui.content.ViewModelContent.JSON_METADATA;
import static com.josef.mobile.free.ui.content.ViewModelContent.JSON_SUBHEADER;
import static com.josef.mobile.free.ui.content.ViewModelContent.JSON_PNG;
import static com.josef.mobile.free.ui.content.ViewModelContent.JSON_URL;

public class ViewModelDetail extends ViewModel {

    public static final String QUERY_PARAM = "queryparam";
    private static final String SERVER_REQUEST = "http://joseph3d.com/wp-content/uploads/";
    public static final String STATE_RESUME_WINDOW = "com.josef.mobile.free.ui.detail.ContentDetailFragment.resumeWindow";
    public static final String STATE_RESUME_POSITION = "com.josef.mobile.free.ui.detail.ContentDetailFragment.resumePosition";
    public static final String STATE_BOOLEAN_VALUE = "com.josef.mobile.free.ui.detail.ContentDetailFragment.value";
    protected static final int DIALOG_FRAGMENT = 1;

    protected int getIndex(int index) {
        return index;
    }

    public ArrayList<String> getColors(String output) throws JSONException {
        ArrayList<String> colors = new ArrayList<>();
        JSONObject object = new JSONObject(output);
        JSONArray input = object.getJSONArray(JSON_COLORS);
        for (int i = 0; i < input.length(); i++) {
            JSONObject object1 = input.getJSONObject(i);
            String string = object1.optString("color");
            colors.add(string);
        }
        return colors;
    }

    protected String getThumbnail(String output, int index, final int query) throws JSONException {
        String url = SERVER_REQUEST;
        JSONObject object = new JSONObject(output);
        String string = object.optString("blazon");
        url = url + string;
        return url;
    }

    protected String getJsonPng(String output, int index, final int query) throws JSONException {
        String url = SERVER_REQUEST;

        JSONObject object = new JSONObject(output);
        JSONArray input = object.getJSONArray(JSON_ENPOINTS);
        JSONObject container = input.getJSONObject(index);
        JSONObject metadata = (JSONObject) container.get(JSON_METADATA);
        String png = (String) metadata.get(JSON_PNG);
        String number = String.valueOf(query) + ".png";
        png = removeLastChar(png);
        url = url + png + number;

        return url;
    }

    protected String getJsonUrl(String output, int index) throws JSONException {
        String url = SERVER_REQUEST;

        JSONObject object = new JSONObject(output);
        JSONArray input = object.getJSONArray(JSON_ENPOINTS);
        JSONObject container = null;
        container = input.getJSONObject(index);
        JSONObject metadata = (JSONObject) container.get(JSON_METADATA);
        String urls = (String) metadata.get(JSON_URL);
        url = url + urls;

        return url;
    }

    protected String getJsonName(String output, int index) throws JSONException {
        JSONObject object = new JSONObject(output);
        JSONArray input = object.getJSONArray(JSON_ENPOINTS);
        JSONObject container = input.getJSONObject(index);
        JSONObject metadata = (JSONObject) container.get(JSON_METADATA);
        String name = (String) metadata.get(JSON_SUBHEADER);
        return name;
    }

    protected ScaleAnimation getScaleAnimation() {
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        return scaleAnimation;
    }

    private static String removeLastChar(String s) {
        return (s == null || s.length() == 0) ? null : (s.substring(0, s.length() - 5));
    }


}
