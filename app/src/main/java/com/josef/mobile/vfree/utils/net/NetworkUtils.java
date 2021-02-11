package com.josef.mobile.vfree.utils.net;


import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

import androidx.annotation.NonNull;

public interface NetworkUtils {

    @NonNull
    Network getActiveNetwork();

    @NonNull
    NetworkCapabilities getNetworkCapabilities(@NonNull Network currentNetwork);

    @NonNull
    LinkProperties getLinkProperties(@NonNull Network currentNetwork);

    @NonNull
    ConnectivityManager getConnectivityManager();

    @NonNull
    boolean isOnline();

}
