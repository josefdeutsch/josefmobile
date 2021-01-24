package com.josef.mobile.vfree.ui.main.store;

import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.main.MainActivity;
import com.josef.mobile.vfree.ui.main.Resource;
import io.reactivex.rxjava3.core.Flowable;

public interface Credentials {

     Flowable<Resource<User>> observeDataStore(MainActivity activity);

}
