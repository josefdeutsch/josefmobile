package com.josef.mobile.vfree.ui.main.store;

import androidx.annotation.NonNull;

import com.josef.mobile.vfree.ui.auth.model.User;
import com.josef.mobile.vfree.ui.main.MainActivity;
import com.josef.mobile.vfree.ui.main.Resource;
import io.reactivex.rxjava3.core.Flowable;

public interface Credentials {

     @NonNull
     Flowable<Resource<User>> observeDataStore(@NonNull MainActivity activity);

}
