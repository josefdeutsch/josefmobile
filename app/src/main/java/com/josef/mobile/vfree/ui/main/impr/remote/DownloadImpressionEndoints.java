package com.josef.mobile.vfree.ui.main.impr.remote;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.impr.model.Impression;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public interface DownloadImpressionEndoints {

    public Flowable<Resource<List<Impression>>> getEndpoints(@NonNull String index);
}
