package com.josef.mobile.vfree.utils.common;

import android.content.Context;

import androidx.annotation.NonNull;

public interface CommonUtils {

    @NonNull
     int getScreenHeight(@NonNull Context context);

    @NonNull
     int getScreenWidth(@NonNull Context context);

    @NonNull
     int getStatusBarHeight(@NonNull Context context);

}
