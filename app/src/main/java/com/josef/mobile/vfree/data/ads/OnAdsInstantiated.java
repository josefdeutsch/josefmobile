package com.josef.mobile.vfree.data.ads;

import com.google.android.gms.ads.LoadAdError;

import io.reactivex.annotations.NonNull;

public interface OnAdsInstantiated {

    void onSuccess();

    void onFailure(@NonNull LoadAdError adError);

    void onAdClicked();

}
