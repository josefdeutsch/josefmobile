package com.josef.josefmobile.data;

import com.josef.josefmobile.data.firebase.Firebase;
import com.josef.josefmobile.data.local.db.DbHelper;
import com.josef.josefmobile.data.local.prefs.PreferencesHelper;
import com.josef.josefmobile.data.remote.Endpoints;

public interface DataManager extends DbHelper, Endpoints, Firebase, PreferencesHelper {


}
