package com.josef.josefmobile.data.remote;

import com.josef.josefmobile.data.remote.model.Endpoint;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Endpoints {

    @POST
    Flowable<Endpoint> getChange(@Url String url);
}
