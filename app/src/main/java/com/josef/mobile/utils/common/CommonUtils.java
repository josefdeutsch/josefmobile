package com.josef.mobile.utils.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.gson.Gson;

public interface CommonUtils {

    Gson getGson();

    Dialog getDialog(Activity activity);

    void showNoInternetConnection(Activity activity);

    void hideNoInternetConnection();

    void showProgressbar(Activity activity);

    void hideProgressbar();

    boolean validateEmail(CharSequence email);

    int getScreenHeight(Context context);

    int getScreenWidth(Context context);

    int getStatusBarHeight(Context context);


}
