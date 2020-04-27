package com.josef.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.josef.mobile.Config.SHAREMETADATA;
import static com.josef.mobile.Config.SHAREURL;

public class AppPreferences {
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEditor;

    public static Set<String> getName(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return mPrefs.getStringSet(SHAREURL, null);
    }

    public static void setName(Context ctx, ArrayList<String> value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        Set<String> set = new HashSet<>();
        set.addAll(value);
        mPrefsEditor.putStringSet(SHAREURL, set);
        mPrefsEditor.commit();
    }

    public static void clearNameList(Context ctx) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        mPrefsEditor = mPrefs.edit();
        Set<String> set = new HashSet<>();
        mPrefsEditor.putStringSet(SHAREURL, set);
        mPrefsEditor.commit();
    }
}