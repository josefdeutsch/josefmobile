package com.josef.mobile.vfree.data.ads;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
@Singleton
public final class AppAdsRequest implements AdsRequest {

    private static final String TAG = "AppAdsRequest";


    private static final String AD_APP_ID = "ca-app-pub-1259971437092628~9660989485";

    private static final String AD_UNIT_ID = "ca-app-pub-1259971437092628/1882765331";

    private static final String AD_TEST_UNIT = "ca-app-pub-3940256099942544/1033173712";

    private static final String AD_SAMPLE_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    private InterstitialAd interstitialAd;

    AdRequest adRequest;

    @NonNull
    private final Context context;

    @Inject
    public AppAdsRequest(@NonNull Context context
    ) {
        Log.d(TAG, "AppAdsRequest: ");
        this.context = context;
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: "+initializationStatus.toString());
            }
        });
        onAdsInterstitialLoaded();
    }

    @NonNull
    @Override
    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public void onAdsInterstitialLoaded( ) {
        adRequest = new AdRequest.Builder().build();
        Log.d(TAG, "onAdsInterstitialLoaded: ");  
        InterstitialAd.load(
                context,
                AD_TEST_UNIT,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Log.i(TAG, "onAdLoaded");

                        AppAdsRequest.this.interstitialAd = interstitialAd;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        AppAdsRequest.this.interstitialAd = null;

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.

                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                        AppAdsRequest.this.interstitialAd = null;

                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    }
                });
        Log.d(TAG, "onAdsInterstitialLoaded: ");
    }



}
