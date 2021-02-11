package com.josef.mobile.vfree.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.josef.mobile.vfree.utils.common.CommonUtils;
import com.josef.mobile.vfree.utils.dialog.DialogUtils;
import com.josef.mobile.vfree.utils.net.NetworkUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class AppUtilManager implements UtilManager {

    @NonNull
    private final DialogUtils dialogUtils;

    @NonNull
    private final NetworkUtils networkUtils;

    @NonNull
    private final CommonUtils commonUtils;

    @Inject
    public AppUtilManager(@NonNull DialogUtils dialogUtils,
                          @NonNull NetworkUtils networkUtils,
                          @NonNull CommonUtils commonUtils) {

        this.dialogUtils = dialogUtils;
        this.networkUtils = networkUtils;
        this.commonUtils = commonUtils;
    }

    @NonNull
    @Override
    public Gson getGson() {
        return dialogUtils.getGson();
    }

    @NonNull
    @Override
    public Dialog getDialog(@NonNull Activity activity) {
        return dialogUtils.getDialog(activity);
    }

    @Override
    public void showNoInternetConnection(@NonNull Activity activity) {
        dialogUtils.showNoInternetConnection(activity);
    }

    @Override
    public void hideNoInternetConnection() {
        dialogUtils.hideNoInternetConnection();

    }

    @Override
    public void showProgressbar(@NonNull Activity activity) {
        dialogUtils.showProgressbar(activity);
    }

    @Override
    public void hideProgressbar() {
        dialogUtils.hideProgressbar();
    }

    @Override
    public boolean validateEmail(@NonNull CharSequence email) {
        return dialogUtils.validateEmail(email);
    }

    @NonNull
    @Override
    public Network getActiveNetwork() {

        return networkUtils.getActiveNetwork();
    }

    @NonNull
    @Override
    public NetworkCapabilities getNetworkCapabilities(@NonNull Network currentNetwork) {
        return networkUtils.getNetworkCapabilities(currentNetwork);
    }

    @NonNull
    @Override
    public LinkProperties getLinkProperties(@NonNull Network currentNetwork) {
        return networkUtils.getLinkProperties(currentNetwork);
    }

    @NonNull
    @Override
    public ConnectivityManager getConnectivityManager() {
        return networkUtils.getConnectivityManager();
    }

    @Override
    public boolean isOnline() {
        return networkUtils.isOnline();
    }

    @NonNull
    @Override
    public int getScreenHeight(@NonNull Context context) {
        return commonUtils.getScreenHeight(context);
    }

    @NonNull
    @Override
    public int getScreenWidth(@NonNull Context context) {
        return commonUtils.getScreenWidth(context);
    }

    @NonNull
    @Override
    public int getStatusBarHeight(@NonNull Context context) {
        return commonUtils.getStatusBarHeight(context);
    }
}
