package com.josef.mobile.vfree.data;

import com.josef.mobile.vfree.data.ads.AdsRequest;
import com.josef.mobile.vfree.data.firebase.Firebase;
import com.josef.mobile.vfree.data.local.db.DbHelper;
import com.josef.mobile.vfree.data.local.prefs.PreferencesHelper;
import com.josef.mobile.vfree.data.remote.Endpoints;

public interface DataManager extends

        DbHelper,
        Endpoints,
        Firebase,
        PreferencesHelper,
        AdsRequest
{


}
