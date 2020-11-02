package com.josef.mobile.utils;

import android.app.Activity;
import android.app.Dialog;

import com.google.gson.Gson;
import com.josef.mobile.utils.common.CommonUtils;
import com.josef.mobile.utils.net.NetworkUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class AppUtilManager implements UtilManager {

    private CommonUtils commonUtils;
    private NetworkUtils networkUtils;

    @Inject
    public AppUtilManager(NetworkUtils networkUtils, CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
        this.networkUtils = networkUtils;
    }

    public AppUtilManager() {

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
    public Dialog getNoInternetConnection(Activity activity) {
        return commonUtils.getNoInternetConnection(activity);
    }

    @Override
    public Single<Boolean> isInternet() {
        return networkUtils.isInternet();
    }
}
