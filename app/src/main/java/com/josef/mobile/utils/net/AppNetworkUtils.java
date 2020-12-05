package com.josef.mobile.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppNetworkUtils implements NetworkUtils {

    private final Context context;

    @Inject
    public AppNetworkUtils(Context context) {
        this.context = context;
    }

    @Override
    public Network getActiveNetwork() {
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        return connectivityManager.getActiveNetwork();
    }

    @Override
    public NetworkCapabilities getNetworkCapabilities(Network currentNetwork) {
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        return connectivityManager.getNetworkCapabilities(currentNetwork);
    }

    @Override
    public LinkProperties getLinkProperties(Network currentNetwork) {
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        return connectivityManager.getLinkProperties(currentNetwork);
    }

    @Override
    public ConnectivityManager getConnectivityManager() {
        return context.getSystemService(ConnectivityManager.class);
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}



