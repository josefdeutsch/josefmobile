package com.josef.mobile.vfree.data.remote;

import com.josef.mobile.vfree.data.remote.model.Endpoint;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Endpoints {

    @POST
    Flowable<Endpoint> getEndpoints(@Url String url);
}
