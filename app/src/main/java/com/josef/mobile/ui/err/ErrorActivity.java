package com.josef.mobile.ui.err;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.josef.mobile.R;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.auth.option.account.SignActivity;
import com.josef.mobile.ui.auth.option.verification.VerificationActivity;
import com.josef.mobile.ui.base.BaseActivity;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.splash.SplashActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErrorActivity extends BaseActivity {

    public static String ACTIVITY_KEYS;
    public static String ACTIVITY_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            ACTIVITY_VALUE = getIntent().getStringExtra(ACTIVITY_KEYS);
        }
        if (savedInstanceState != null) {
            ACTIVITY_VALUE = savedInstanceState.getString(ACTIVITY_KEYS);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ACTIVITY_KEYS, ACTIVITY_VALUE);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        alert.setMessage(R.string.app_quit_remainder);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
            sessionManager.logOut();
        });
        alert.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
        alert.show();
    }


    @OnClick(R.id.try_again)
    public void tryagain() {
        if (isOnline()) {
            switch (ACTIVITY_VALUE) {
                case SPLASH_ACTIVITY_NAME:
                    startActivity(new Intent(this, SplashActivity.class));
                    finishAfterTransition();
                    break;
                case AUTH_ACTIVITY_NAME:
                    startActivity(new Intent(this, AuthActivity.class));
                    finishAfterTransition();

                    break;
                case MAIN_ACTIVITY_NAME:
                    startActivity(new Intent(this, MainActivity.class));
                    finishAfterTransition();
                    break;
                case PLAYER_ACTIVITY_NAME:
                    startActivity(new Intent(this, MainActivity.class));
                    finishAfterTransition();
                    break;
                case VERIFICATION_ACTIVITY_NAME:
                    startActivity(new Intent(this, VerificationActivity.class));
                    finishAfterTransition();
                    break;
                case SIGN_ACTIVITY_NAME:
                    startActivity(new Intent(this, SignActivity.class));
                    finishAfterTransition();
                    break;

            }

        } else {
            Toast.makeText(getApplicationContext(),
                    getApplicationContext().getResources().getString(R.string.activity_error_nonnetwork),
                    Toast.LENGTH_SHORT).show();
        }
    }
}