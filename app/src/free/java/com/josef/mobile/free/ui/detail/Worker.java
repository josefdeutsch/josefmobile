package com.josef.mobile.free.ui.detail;

import android.widget.ProgressBar;

public interface Worker {

     void execute(final String input, final int index, final int query) throws Exception;
}

