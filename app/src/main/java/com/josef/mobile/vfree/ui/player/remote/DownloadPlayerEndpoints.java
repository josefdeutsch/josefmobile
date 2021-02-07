package com.josef.mobile.vfree.ui.player.remote;

import com.josef.mobile.vfree.data.local.db.model.LocalCache;
import com.josef.mobile.vfree.ui.main.Resource;

import io.reactivex.Flowable;

public interface DownloadPlayerEndpoints {

     Flowable<Resource<LocalCache>> observeEndpoints(int index);

}
