package com.josef.mobile.utils.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

import static android.content.ContentValues.TAG;

@Singleton
public class AppNetworkUtils extends LiveData<Boolean> implements NetworkUtils {

    private final Context context;
    private ConnectivityManager connectivityManager;

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (isNetworkAvailable()) {
                postValue(true);
                Log.d(TAG, "onReceive: " + true);
            } else {
                postValue(false);
                Log.d(TAG, "onReceive: " + false);
            }
        }
    };


    @Inject
    public AppNetworkUtils(Context context) {
        this.context = context;
    }

    AppNetworkUtils appNetworkUtils;

    @Override
    protected void onActive() {
        super.onActive();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(networkReceiver, intentFilter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        context.unregisterReceiver(networkReceiver);
    }

    private boolean isNetworkAvailable() {
        if (connectivityManager == null)
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }

    public Single<Boolean> isInternet() {
        AppNetworkUtils appNetworkUtils = new AppNetworkUtils(context);
        return null;

    }

    private Single<Boolean> buildObserver() {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}


