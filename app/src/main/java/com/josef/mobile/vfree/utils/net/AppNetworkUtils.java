package com.josef.mobile.vfree.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppNetworkUtils implements NetworkUtils {

    @NonNull
    private final Context context;

    @Inject
    public AppNetworkUtils(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Network getActiveNetwork() {
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        return connectivityManager.getActiveNetwork();
    }

    @NonNull
    @Override
    public NetworkCapabilities getNetworkCapabilities(Network currentNetwork) {
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        return connectivityManager.getNetworkCapabilities(currentNetwork);
    }

    @NonNull
    @Override
    public LinkProperties getLinkProperties(Network currentNetwork) {
        ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
        return connectivityManager.getLinkProperties(currentNetwork);
    }

    @NonNull
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

