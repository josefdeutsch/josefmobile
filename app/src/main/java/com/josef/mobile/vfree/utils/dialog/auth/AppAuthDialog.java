package com.josef.mobile.vfree.utils.dialog.auth;

import android.app.Activity;
import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.josef.mobile.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class AppAuthDialog implements AuthDialog {

    @Nullable
    private Dialog progressBar;
    @NonNull
    private Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;

    @Inject
    public AppAuthDialog() {

    }

    @NonNull
    @Override
    public Dialog getDialog(@NonNull Activity activity) {
        Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        return dialog;
    }


    @Override
    public void showAuthProgressbar(@NonNull Activity activity) {
        if (progressBar == null) {
            progressBar = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            progressBar.setContentView(R.layout.progress_dialog);
            progressBar.show();
        }
    }

    @Override
    public void hideAuthProgressbar() {
        if (progressBar != null) {
            progressBar.hide();
            progressBar.dismiss();
            progressBar = null;
        }
    }

    @Override
    public void resetAuthProgressbar() {
        progressBar = null;
    }
}
