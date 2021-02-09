package com.josef.mobile.vfree.ui.splash;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.RequestManager;
import com.josef.mobile.vfree.ui.auth.AuthActivity;
import com.josef.mobile.vfree.ui.base.BaseActivity;
import com.josef.mobile.vfree.viewmodels.ViewModelProviderFactory;
import com.josef.mobile.R;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SplashActivity extends BaseActivity {


    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @BindView(R.id.animated_gif)
    ImageView animatedGif;

    @NonNull private SplashViewModel viewModel;

    @NonNull private static final String interstitialAdId =
            "ca-app-pub-3940256099942544/1033173712";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this, providerFactory)
                .get(SplashViewModel.class);
        viewModel.initiateInsterstitialAds(interstitialAdId);
      //  viewModel.supplyAnimatedGif(animatedGif);
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
        },5000);

    }

    //requestManager.load(profileList.get(position).getUrl()).into(animatedGif);

    @Override
    public void subscribeToSessionManager() {

    }
}