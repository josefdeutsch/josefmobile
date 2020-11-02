package com.josef.mobile.utils.common;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;

public interface CommonUtils {

    Gson getGson();

    Dialog getDialog(Activity activity);

    void showNoInternetConnection(Activity activity);

    void hideNoInternetConnection();

    void showProgressbar(Activity activity);

    void hideProgressbar();

}
