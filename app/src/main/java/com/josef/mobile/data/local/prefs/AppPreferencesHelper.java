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

package com.josef.mobile.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.josef.mobile.di.PreferenceInfo;

import javax.inject.Inject;

/**
 * Created by amitshekhar on 07/07/17.
 */

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String TAG = "AppPreferencesHelper";
    private static final String PREF_KEY_SPARSEARRAY_IDENTIFIER = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_STRING_SPARSEARRAY_IDENTIFIER = "PREF_STRING_ACCESS_TOKEN";
    private static final String ANOTHER_PREF_KEY_SPARSEARRAY_IDENTIFIER = "Anothersdkhfsdlfkds";
    private static final String POSITION_ID_HASH_MAP = "pos_id_hash_map";


    private final SharedPreferences mPrefs;
    private final Context context;


    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        this.context = context;
    }


    @Override
    public String getPositionToggleHashmap() {
        return mPrefs.getString(ANOTHER_PREF_KEY_SPARSEARRAY_IDENTIFIER, "uschi");
    }

    @Override
    public void setPositionToggleHashMap(String string) {
        mPrefs.edit().putString(ANOTHER_PREF_KEY_SPARSEARRAY_IDENTIFIER, string).commit();
    }

    @Override
    public String getPositionIdHashmap() {
        return mPrefs.getString(POSITION_ID_HASH_MAP, "simone");
    }

    @Override
    public void setPositionIdHashmap(String string) {
        mPrefs.edit().putString(POSITION_ID_HASH_MAP, string).commit();
    }

}
