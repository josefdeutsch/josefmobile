package com.josef.mobile.vfree.ui.main.about.remote;

import com.josef.mobile.vfree.ui.main.Resource;
import com.josef.mobile.vfree.ui.main.about.model.About;

import java.util.List;

import io.reactivex.Flowable;

public interface DownloadAboutEndpoints {

    Flowable<Resource<List<About>>> getEndpoints(String index);
}
