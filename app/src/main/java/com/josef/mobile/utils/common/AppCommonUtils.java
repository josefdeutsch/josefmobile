package com.josef.mobile.utils.common;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.josef.mobile.R;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppCommonUtils implements CommonUtils {

    Gson gson;
    Dialog noNetwork;
    Dialog progressBar;

    @Inject
    public AppCommonUtils() {

    }

    @Override
    public Gson getGson() {
        if (gson == null) gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson;
    }

    @Override
    public Dialog getDialog(Activity activity) {
        Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        return dialog;
    }


    @Override
    public void showProgressbar(Activity activity) {
        if (progressBar == null) {
            progressBar = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            progressBar.setContentView(R.layout.progress_dialog);
            progressBar.show();
        }
    }

    @Override
    public void hideProgressbar() {
        if (progressBar != null) {
            progressBar.hide();
            progressBar.dismiss();
            progressBar = null;
        }
    }


    @Override
    public void showNoInternetConnection(Activity activity) {
        if (noNetwork == null) {
            noNetwork = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            noNetwork.setContentView(R.layout.nowifi_dialog);
            noNetwork.show();
        }
    }

    @Override
    public void hideNoInternetConnection() {
        if (noNetwork != null) {
            noNetwork.hide();
            noNetwork.dismiss();
            noNetwork = null;
        }
    }

}
