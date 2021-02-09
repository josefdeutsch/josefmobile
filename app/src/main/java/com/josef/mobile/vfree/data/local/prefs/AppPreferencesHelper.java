/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.josef.mobile.vfree.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.josef.mobile.vfree.di.PreferenceInfo;

import javax.inject.Inject;

/**
 * Created by amitshekhar on 07/07/17.
 */

public final class AppPreferencesHelper implements PreferencesHelper {

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@NonNull Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @NonNull
    public String getHashMapArchiveIndicator() {
        return mPrefs.getString(ARCHIVE_INDICATOR, ARCHIVE_EMPTY);
    }

    @Override
    public void setHashMapArchiveIndicator(@NonNull String string) {
        mPrefs.edit().putString(ARCHIVE_INDICATOR, string).commit();
    }

    @Override
    public void clearHashmapIndicator() {

        mPrefs.edit().remove(ARCHIVE_INDICATOR).commit();
    }

}
