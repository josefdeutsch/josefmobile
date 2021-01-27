package com.josef.mobile.vfree.data.ads;

import com.google.android.gms.ads.InterstitialAd;

public interface AdsRequest {

     void setInterstitialAd(String id);

     void setOnAdsInstantiated(OnAdsInstantiated onAdsInstantiated);

     InterstitialAd getInterstitialAd();
}
