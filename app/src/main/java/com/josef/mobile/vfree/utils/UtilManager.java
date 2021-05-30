package com.josef.mobile.vfree.utils;

import com.josef.mobile.vfree.utils.common.CommonUtils;
import com.josef.mobile.vfree.utils.dialog.auth.AuthDialog;
import com.josef.mobile.vfree.utils.dialog.main.MainDialog;
import com.josef.mobile.vfree.utils.net.NetworkUtils;

public interface UtilManager extends

        MainDialog,
        AuthDialog,
        NetworkUtils,
        CommonUtils {

}
