package com.josef.mobile.ui.splash;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.josef.mobile.R;
import com.josef.mobile.data.local.db.model.LocalCache;
import com.josef.mobile.ui.auth.AuthActivity;
import com.josef.mobile.ui.base.BaseActivity;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

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
                        case LOADING: {
                            Log.d(TAG, "onChanged: PostsFragment: LOADING...");
                            break;
                        }
                        case SUCCESS: {
                            viewModel.insertAllEndoints(listResource.data);
                            startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                            finishAfterTransition();
                            break;
                        }
                        case ERROR: {
                            Log.d(TAG, "onChanged: PostsFragment: ERROR... " + listResource.message);
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