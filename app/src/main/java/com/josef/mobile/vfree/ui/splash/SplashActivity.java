package com.josef.mobile.vfree.ui.splash;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.josef.mobile.vfree.ui.auth.AuthActivity;
import com.josef.mobile.vfree.ui.base.BaseActivity;
import com.josef.mobile.vfree.viewmodels.ViewModelProviderFactory;
import com.josef.mobile.R;


import javax.inject.Inject;


public class SplashActivity extends BaseActivity {


    @Inject
    ViewModelProviderFactory providerFactory;


    @NonNull private SplashViewModel viewModel;

    @NonNull private static final String interstitialAdId =
            "ca-app-pub-3940256099942544/1033173712";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        viewModel = new ViewModelProvider(this, providerFactory)
                .get(SplashViewModel.class);

        viewModel.initiateInsterstitialAds(interstitialAdId);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = ActivityOptionsCompat
                        .makeCustomAnimation(SplashActivity.this,
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                startActivity(new Intent(SplashActivity.this, AuthActivity.class),
                        bundle);
                finishAfterTransition();
            }
        },3000);
    }

    @Override
    public void subscribeToSessionManager() {

    }
}