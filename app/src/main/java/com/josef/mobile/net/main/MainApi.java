package com.josef.mobile.net.main;


import com.google.firebase.database.core.view.Change;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MainApi {
    //"/storage/browser/capstone-272303.appspot.com"
    // /posts?userId=1/
    @GET("posts")
    Flowable<List<Change>> getPostsFromUser(
            @Query("userId") int id
    );

    @POST("_ah/api/echo/v1/echo?n=1")
    Flowable<com.josef.mobile.models.Change> getPostsFromUser();
}
