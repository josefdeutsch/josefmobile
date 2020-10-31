package com.josef.mobile.utils;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppCommonUtils implements CommonUtils {

    Gson gson;

    @Inject
    public AppCommonUtils(Gson gson) {
        this.gson = gson;
    }

    public Gson getGson() {
        return gson;
    }
}
