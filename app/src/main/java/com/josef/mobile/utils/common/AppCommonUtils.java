package com.josef.mobile.utils.common;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.josef.mobile.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppCommonUtils implements CommonUtils {

    Dialog noNetwork;
    Dialog progressBar;
    final Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
    Matcher matcher;

    @Inject
    public AppCommonUtils() {

    }

    @Override
    public Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
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

    @Override
    public boolean validateEmail(CharSequence email) {
        if (email == null) return false;
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
