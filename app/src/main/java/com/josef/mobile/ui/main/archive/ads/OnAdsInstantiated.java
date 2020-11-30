package com.josef.mobile.ui.main.archive.ads;

import com.google.android.gms.ads.LoadAdError;

public interface OnAdsInstantiated {

    void onSuccess();

    void onFailure(LoadAdError adError);

    void onAdClicked();

}
