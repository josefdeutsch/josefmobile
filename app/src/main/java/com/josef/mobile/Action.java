package com.josef.mobile;

import androidx.annotation.Nullable;
import androidx.work.WorkInfo;

public interface Action {
    void performAction(@Nullable final String output, final int index);
}
