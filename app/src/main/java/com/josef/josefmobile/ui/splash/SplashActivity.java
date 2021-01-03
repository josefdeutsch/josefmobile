package com.josef.josefmobile.ui.splash;


import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.josef.josefmobile.data.local.db.model.LocalCache;
import com.josef.josefmobile.ui.auth.AuthActivity;
import com.josef.josefmobile.ui.base.BaseActivity;
import com.josef.josefmobile.ui.err.ErrorActivity;
import com.josef.josefmobile.ui.main.Resource;
import com.josef.josefmobile.viewmodels.ViewModelProviderFactory;
import com.josef.mobile.R;

import java.util.List;

import javax.inject.Inject;

import static com.josef.josefmobile.ui.err.ErrorActivity.ACTIVITY_KEYS;

public class SplashActivity extends BaseActivity {


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
                            //
                            break;
                        }
                        case ERROR: {
                            Intent intent = new Intent(SplashActivity.this, ErrorActivity.class);
                            intent.putExtra(ACTIVITY_KEYS, SplashActivity.this.getComponentName().getClassName());

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