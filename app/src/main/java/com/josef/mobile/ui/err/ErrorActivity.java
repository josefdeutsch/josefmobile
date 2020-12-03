package com.josef.mobile.ui.err;

import android.content.Intent;
import android.os.Bundle;

import com.josef.mobile.R;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.base.BaseActivity;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.splash.SplashActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.josef.mobile.utils.work.Worker.WORKREQUEST_INDICATOR;

public class ErrorActivity extends BaseActivity {

    private static final String SPLASH_ACTIVITY = "com.mobile.josef.ui.splash.SplashActivity";
    private static final String AUTH_ACTIVITY = "com.mobile.josef.ui.auth.AuthActivity";
    private static final String MAIN_ACTIVITY = "com.mobile.josef.ui.main.MainActivity";
    private static final String PLAYER_ACTIVITY = "com.mobile.josef.ui.player.PlayerActivity";
    private static String IDENTIFIER_ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            IDENTIFIER_ACTIVITY = getIntent().getStringExtra(WORKREQUEST_INDICATOR);
        }

        if (savedInstanceState != null) {
            IDENTIFIER_ACTIVITY = savedInstanceState.getString(WORKREQUEST_INDICATOR);
        }
    }

    @Override
    public void verifyNetworkResponse() {
        // no network..
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(WORKREQUEST_INDICATOR, IDENTIFIER_ACTIVITY);
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.try_again)
    public void tryagain() {

        switch (IDENTIFIER_ACTIVITY) {
            case SPLASH_ACTIVITY:
                startActivity(new Intent(this, SplashActivity.class));
                finishAfterTransition();
                break;
            case AUTH_ACTIVITY:
                startActivity(new Intent(this, AuthActivity.class));
                finishAfterTransition();

                break;
            case MAIN_ACTIVITY:
                startActivity(new Intent(this, MainActivity.class));
                finishAfterTransition();

                break;
            case PLAYER_ACTIVITY:
                String str = "";
                startActivity(new Intent(this, MainActivity.class));
                finishAfterTransition();
                break;
        }
    }
}