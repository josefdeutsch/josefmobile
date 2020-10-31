package com.josef.mobile.utils;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;

public interface CommonUtils {

    Gson getGson();

    Dialog getDialog(Activity activity);


}
