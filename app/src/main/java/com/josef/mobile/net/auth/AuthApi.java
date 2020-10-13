package com.josef.mobile.net.auth;


import io.reactivex.Flowable;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("_ah/api/echo/v1/echo?n=1")
    Flowable<com.josef.mobile.models.Change> getPostsFromUser();
}
