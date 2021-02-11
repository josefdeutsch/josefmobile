package com.josef.mobile.vfree.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public interface DialogUtils {

    @NonNull Dialog getDialog(@NonNull Activity activity);

    void showNoInternetConnection(@NonNull Activity activity);

    void hideNoInternetConnection();

    void showProgressbar(@NonNull Activity activity);

    void hideProgressbar();

    boolean validateEmail(@NonNull CharSequence email);

    @NonNull Gson getGson();

}
