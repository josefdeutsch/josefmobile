package com.josef.mobile.utils;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppUtil implements Util {

    Gson gson;

    @Inject
    public AppUtil(Gson gson) {
        this.gson = gson;
    }

    public Gson getGson() {
        return gson;
    }
}
