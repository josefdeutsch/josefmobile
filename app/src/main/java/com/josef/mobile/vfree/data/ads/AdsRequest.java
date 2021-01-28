package com.josef.mobile.vfree.data.ads;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.InterstitialAd;

public interface AdsRequest {

     void setInterstitialAd(@NonNull String id);

     void setOnInterstitialInstantiated(@NonNull OnAdsInstantiated onAdsInstantiated);

     @NonNull
     InterstitialAd getInterstitialAd();


}
