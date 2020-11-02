package com.josef.mobile.utils.net;


import io.reactivex.Single;

public interface NetworkUtils {

    Single<Boolean> isInternet();
}
