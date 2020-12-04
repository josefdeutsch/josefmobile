package com.josef.mobile.ui.err;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.josef.mobile.R;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.auth.option.account.SignActivity;
import com.josef.mobile.ui.auth.option.verification.VerificationActivity;
import com.josef.mobile.ui.base.BaseActivity;
import com.josef.mobile.ui.main.MainActivity;
import com.josef.mobile.ui.splash.SplashActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErrorActivity extends BaseActivity implements OnError {

    public static String ACTIVITY_KEY;
    public static String ACTIVITY_VALUE;
    public static String EXECEPTION_KEY;
    public static String EXECEPTION_VALUE;
    @Inject
    FirebaseAuth mAuth;
    @BindView(R.id.message)
    TextView message;

    AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ButterKnife.bind(this);
        alert = new AlertDialog.Builder(this);
        if (savedInstanceState == null) {
            ACTIVITY_VALUE = getIntent().getStringExtra(ACTIVITY_KEY);
            EXECEPTION_VALUE = getIntent().getStringExtra(EXECEPTION_KEY);
        }

        if (savedInstanceState != null) {
            ACTIVITY_VALUE = savedInstanceState.getString(ACTIVITY_KEY);
            EXECEPTION_VALUE = getIntent().getStringExtra(EXECEPTION_KEY);
        }
        message.setText(EXECEPTION_VALUE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ACTIVITY_KEY, ACTIVITY_VALUE);
        outState.putString(EXECEPTION_KEY, EXECEPTION_VALUE);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        alert.setMessage(R.string.app_quit_remainder);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
            ErrorActivity.this.mAuth.signOut();
            finish();
        });
        alert.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
        alert.show();
    }


    @OnClick(R.id.try_again)
    public void tryagain() {
        if (isOnline()) {
            switch (ACTIVITY_VALUE) {
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
                case VERIFICATION_ACTIVITY:
                    startActivity(new Intent(this, VerificationActivity.class));
                    finishAfterTransition();
                    break;
                case SIGN_ACTIVITY:
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


    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}