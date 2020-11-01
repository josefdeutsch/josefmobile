package com.josef.mobile.utils.common;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppCommonUtils implements CommonUtils {

    Gson gson;
    Dialog dialog;

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
        if (dialog == null)
            dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        return dialog;
    }
}
