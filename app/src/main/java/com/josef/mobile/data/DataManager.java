package com.josef.mobile.data;

import com.josef.mobile.data.local.db.DbHelper;
import com.josef.mobile.data.local.prefs.PreferencesHelper;
import com.josef.mobile.data.remote.Endpoints;

public interface DataManager extends DbHelper, Endpoints, PreferencesHelper {

}
