package com.josef.mobile.vfree.utils.dialog.auth;

import android.app.Activity;
import android.app.Dialog;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public interface AuthDialog {

    @NonNull Dialog getDialog(@NonNull Activity activity);

    void showAuthProgressbar(@NonNull Activity activity);

    void hideAuthProgressbar();

    void resetAuthProgressbar();



}
