package com.josef.mobile.vfree.data.ads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppAdsRequest implements AdsRequest {

    private static final String TAG = "AppAdsRequest";

    public InterstitialAd getInterstitialAd() {
        return mInterstitialAd;
    }

    private InterstitialAd mInterstitialAd;

    private OnAdsInstantiated onAdsInstantiated;

    @NonNull
    private final Context context;

    @Inject
    public AppAdsRequest(@NonNull Context context
    ) {
        this.context = context;
    }

    @Override
    public void setInterstitialAd(@NonNull String id) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(id);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //InterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                onAdsInstantiated.onFailure(adError);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
                onAdsInstantiated.onAdClicked();
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
                onAdsInstantiated.onSuccess();
                requestNewInterstitial();
            }
        });

    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public void requestNewInterstitial() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                mInterstitialAd.loadAd(adRequest);
            }
        }, 300);
    }
    public void setOnAdsInstantiated(OnAdsInstantiated onAdsInstantiated){
        this.onAdsInstantiated = onAdsInstantiated;
    };

}
