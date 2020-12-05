package com.josef.mobile.utils.net;


import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

public interface NetworkUtils {

    Network getActiveNetwork();

    NetworkCapabilities getNetworkCapabilities(Network currentNetwork);

    LinkProperties getLinkProperties(Network currentNetwork);

    ConnectivityManager getConnectivityManager();

    boolean isOnline();

}
