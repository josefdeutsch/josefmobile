package com.josef.mobile.ui.main.archive.ads;

import android.content.Context;
import android.os.Handler;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.josef.mobile.utils.UtilManager;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class AppAdsRequest implements InterstitialAdsRequest {

    private final Context context;
    private final UtilManager utilManager;
    private InterstitialAd mInterstitialAd;

    @Inject
    public AppAdsRequest(Context context, UtilManager utilManager) {
        this.context = context;
        this.utilManager = utilManager;
    }

    @Override
    public void execute(OnAdsInstantiated onAdsInstantiated) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                new Handler().postDelayed(() -> utilManager.hideProgressbar(), 1000);
                mInterstitialAd.show();
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
            }
        });
    }
}
