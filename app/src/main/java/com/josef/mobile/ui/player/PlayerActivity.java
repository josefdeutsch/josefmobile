package com.josef.mobile.ui.player;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.josef.mobile.R;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.ui.main.post.model.Container;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.utils.AppConstants.REQUEST_INDEX;

public class PlayerActivity extends DaggerAppCompatActivity {

    private static final String TAG = "PlayerActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    private PlayerViewModel viewModel;

    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Log.d(TAG, "onCreate: ");
        if (savedInstanceState == null) {
            index = getIntent().getIntExtra(REQUEST_INDEX, 0);
        }
        viewModel = new ViewModelProvider(this, providerFactory).get(PlayerViewModel.class);
        subscribeObserver();

    }

    private void subscribeObserver() {
        viewModel.authenticateWithEndpoint(index);
        viewModel.observeContainer().observe(this, new Observer<Resource<Container>>() {
            @Override
            public void onChanged(Resource<Container> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: PlayerActivity: LOADING...");
                            break;
                        }
                        case SUCCESS: {
                            Log.d(TAG, "onChanged: PlayerActivity: SUCCESS");

                            Log.d(TAG, "onChanged: " + listResource.data.getUrl());
                            break;
                        }
                        case ERROR: {
                            Log.d(TAG, "onChanged: PlayerActivity: ERROR... " + listResource.message);
                            break;
                        }
                    }
                }
            }
        });
    }
}