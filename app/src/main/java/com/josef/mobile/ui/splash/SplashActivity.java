package com.josef.mobile.ui.splash;


import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.josef.mobile.R;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.base.BaseActivity;
import com.josef.mobile.ui.err.ErrorActivity;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import static com.josef.mobile.ui.err.ErrorActivity.ACTIVITY_KEY;
import static com.josef.mobile.ui.err.ErrorActivity.EXECEPTION_KEY;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        viewModel = new ViewModelProvider(this, providerFactory).get(SplashViewModel.class);
        subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel.observeEndpoints().removeObservers(this);
        viewModel.observeEndpoints().observe(this, new Observer<Resource<List<LocalCache>>>() {
            @Override
            public void onChanged(Resource<List<LocalCache>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case SUCCESS: {
                            viewModel.insertAllEndoints(listResource.data);
                            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(SplashActivity.this,
                                    android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                            startActivity(new Intent(SplashActivity.this, AuthActivity.class), bundle);
                            finishAfterTransition();
                            break;
                        }
                        case ERROR: {
                            Intent intent = new Intent(SplashActivity.this, ErrorActivity.class);
                            intent.putExtra(ACTIVITY_KEY, SplashActivity.this.getComponentName().getClassName());
                            intent.putExtra(EXECEPTION_KEY, listResource.message);

                            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(SplashActivity.this,
                                    android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

                            startActivity((intent), bundle);

                            finishAfterTransition();
                            break;
                        }
                    }
                }
            }
        });
    }


    @Override
    public void subscribeToSessionManager() {

    }
}