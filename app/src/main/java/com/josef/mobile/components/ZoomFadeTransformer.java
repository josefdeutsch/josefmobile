package com.josef.mobile.components;

import android.view.View;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

public class ZoomFadeTransformer implements ViewPager2.PageTransformer {

    private float offset = -1;
    private float paddingLeft;
    private float minScale;
    private float minAlpha;

    public ZoomFadeTransformer(float paddingLeft, float minScale, float minAlpha) {
        this.paddingLeft = paddingLeft;
        this.minAlpha = minAlpha;
        this.minScale = minScale;
    }

    @Override
    public void transformPage(View page, float position) {
        if (offset == -1) {
            offset = paddingLeft / page.getMeasuredWidth();
        }
        if (position < -1) {
            page.setAlpha(0);
        } else if (position <= 1) {
            float scaleFactor = Math.max(minScale, 1 - Math.abs(position - offset));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            float alphaFactor = Math.max(minAlpha, 1 - Math.abs(position - offset));
            page.setAlpha(alphaFactor);
        } else {
            page.setAlpha(0);
        }
    }
}
