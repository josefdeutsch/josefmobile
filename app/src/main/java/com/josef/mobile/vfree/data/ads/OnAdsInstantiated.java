package com.josef.mobile.vfree.data.ads;

import com.google.android.gms.ads.LoadAdError;

public interface OnAdsInstantiated {

    void onSuccess();

    void onFailure(LoadAdError adError);

    void onAdClicked();

    void onAdLoaded();

}
