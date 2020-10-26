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


import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by amitshekhar on 07/07/17.
 */

public interface PreferencesHelper {


    void setParceableSparseBooleanArray(SparseBooleanArray sparseBooleanArray);

    SparseBooleanArray getSparseBooleanArrayParcelable();

    String getPrefStringSparsearrayIdentifier();

    void setPrefStringSparsearrayIdentifier(String string);

    public Set<String> getList();

    public void setList(ArrayList<String> value);

}
