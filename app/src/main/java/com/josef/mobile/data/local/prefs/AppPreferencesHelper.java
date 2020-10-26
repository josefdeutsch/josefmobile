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
import android.util.Log;
import android.util.SparseBooleanArray;

import com.google.gson.Gson;
import com.josef.mobile.di.PreferenceInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by amitshekhar on 07/07/17.
 */

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String TAG = "AppPreferencesHelper";
    private static final String PREF_KEY_SPARSEARRAY_IDENTIFIER = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_STRING_SPARSEARRAY_IDENTIFIER = "PREF_STRING_ACCESS_TOKEN";
    private static final String ANOTHER_PREF_KEY_SPARSEARRAY_IDENTIFIER = "Anothersdkhfsdlfkds";


    private final SharedPreferences mPrefs;
    private final Context context;


    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        this.context = context;
    }


    @Override
    public String getHashString() {
        return mPrefs.getString(ANOTHER_PREF_KEY_SPARSEARRAY_IDENTIFIER, "uschi");
    }

    @Override
    public void setHashString(String string) {
        Log.d(TAG, "setParceableSparseBooleanArray: ");
        mPrefs.edit().putString(ANOTHER_PREF_KEY_SPARSEARRAY_IDENTIFIER, string).commit();
    }

    @Override
    public void setParceableSparseBooleanArray(SparseBooleanArray sparseBooleanArray) {
        Log.d(TAG, "setParceableSparseBooleanArray: ");
        Gson gson = new Gson();
        mPrefs.edit().putString(PREF_KEY_SPARSEARRAY_IDENTIFIER, gson.toJson(sparseBooleanArray)).commit();
    }

    @Override
    public SparseBooleanArray getSparseBooleanArrayParcelable() {
        Log.d(TAG, "getSparseBooleanArrayParcelable: ");
        SparseBooleanArray booleanArray;
        Gson gson = new Gson();
        booleanArray = gson.fromJson(mPrefs.getString(PREF_KEY_SPARSEARRAY_IDENTIFIER, ""), SparseBooleanArray.class);
        return booleanArray;
    }


    public Set<String> getList() {
        return mPrefs.getStringSet(PREF_KEY_SPARSEARRAY_IDENTIFIER, null);
    }

    public void setList(ArrayList<String> value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        Log.d(TAG, "setList: " + value.size());
        Set<String> set = new HashSet<>();
        set.addAll(value);
        editor.putStringSet(PREF_KEY_SPARSEARRAY_IDENTIFIER, set);
        editor.apply();
    }

    /** public static void clearNameList(Context ctx) {
     mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
     mPrefsEditor = mPrefs.edit();
     Set<String> set = new HashSet<>();
     mPrefsEditor.putStringSet(PREF_KEY_SPARSEARRAY_IDENTIFIER, set);
     mPrefsEditor.commit();
     }**/
}
