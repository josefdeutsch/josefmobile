package com.josef.mobile.vfree.ui.main.post.remote;

import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.ui.main.Resource;

import java.util.List;

import io.reactivex.Flowable;

public interface DownloadEndpoints {

     Flowable<Resource<List<LocalCache>>> getEndpoints(String index);
}
