package com.josef.mobile.vfree.utils.dialog.main;

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
public final class AppMainDialog implements MainDialog {

    @Nullable
    private Dialog noNetwork;
    @Nullable
    private Dialog progressBar;
    @NonNull
    private Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
    @NonNull
    private Matcher matcher;

    @Inject
    public AppMainDialog() {

    }

    @NonNull
    @Override
    public Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @NonNull
    @Override
    public Dialog getDialog(@NonNull Activity activity) {
        Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        return dialog;
    }


    @Override
    public void showProgressbar(@NonNull Activity activity) {
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
    public void showNoInternetConnection(@NonNull Activity activity) {
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
    public boolean validateEmail(@NonNull CharSequence email) {
        if (email == null) return false;
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
