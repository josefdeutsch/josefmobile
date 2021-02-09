package com.josef.mobile.vfree.ui.player.remote;

import com.josef.mobile.vfree.ui.main.post.model.LocalCache;
import com.josef.mobile.vfree.ui.main.Resource;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public interface DownloadPlayerEndpoints {

     Flowable<Resource<LocalCache>> observeEndpoints(@NonNull int index,
                                                     @NonNull String endpoints);

}
