package com.josef.mobile.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

import com.google.gson.Gson;
import com.josef.mobile.utils.common.CommonUtils;
import com.josef.mobile.utils.net.NetworkUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppUtilManager implements UtilManager {

    private final CommonUtils commonUtils;
    private final NetworkUtils networkUtils;

    @Inject
    public AppUtilManager(NetworkUtils networkUtils, CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
        this.networkUtils = networkUtils;
    }

    @Override
    public Gson getGson() {
        return commonUtils.getGson();
    }

    @Override
    public Dialog getDialog(Activity activity) {
        return commonUtils.getDialog(activity);
    }

    @Override
    public void showNoInternetConnection(Activity activity) {
        commonUtils.showNoInternetConnection(activity);
    }

    @Override
    public void hideNoInternetConnection() {
        commonUtils.hideNoInternetConnection();

    }

    @Override
    public void showProgressbar(Activity activity) {
        commonUtils.showProgressbar(activity);
    }

    @Override
    public void hideProgressbar() {
        commonUtils.hideProgressbar();
    }

    @Override
    public boolean validateEmail(CharSequence email) {
        return commonUtils.validateEmail(email);
    }

    @Override
    public int getScreenHeight(Context context) {
        return commonUtils.getScreenHeight(context);
    }

    @Override
    public int getScreenWidth(Context context) {
        return commonUtils.getScreenWidth(context);
    }

    @Override
    public int getStatusBarHeight(Context context) {
        return commonUtils.getStatusBarHeight(context);
    }


    @Override
    public Network getActiveNetwork() {
        return networkUtils.getActiveNetwork();
    }

    @Override
    public NetworkCapabilities getNetworkCapabilities(Network currentNetwork) {
        return networkUtils.getNetworkCapabilities(currentNetwork);
    }

    @Override
    public LinkProperties getLinkProperties(Network currentNetwork) {
        return networkUtils.getLinkProperties(currentNetwork);
    }

    @Override
    public ConnectivityManager getConnectivityManager() {
        return networkUtils.getConnectivityManager();
    }

    @Override
    public boolean isOnline() {
        return networkUtils.isOnline();
    }
}
