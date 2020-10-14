package com.josef.mobile.ui.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.josef.mobile.models.Player;
import com.josef.mobile.ui.main.Resource;
import com.josef.mobile.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.josef.mobile.util.Constants.REQUEST_PNG;
import static com.josef.mobile.util.Constants.REQUEST_URL;

public class PlayerActivity extends DaggerAppCompatActivity {

    private static final String TAG = "PlayerActivity";

    String requestPng;
    String requestUrl;

    @Inject
    ViewModelProviderFactory providerFactory;

    private PlayerViewModel playerViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            requestPng = intent.getStringExtra(REQUEST_PNG);
            requestUrl = intent.getStringExtra(REQUEST_URL);
        } else {
            requestPng = savedInstanceState.getString(REQUEST_PNG);
            requestUrl = savedInstanceState.getString(REQUEST_URL);
        }

        Log.d(TAG, "onCreate: " + requestUrl);

        playerViewModel = new ViewModelProvider(this, providerFactory).get(PlayerViewModel.class);
        playerViewModel.perfomLiveData(requestUrl);
        subscribeObservers();


    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(REQUEST_PNG, requestPng);
        outState.putString(REQUEST_URL, requestUrl);
        super.onSaveInstanceState(outState);
    }

    private void subscribeObservers() {
        playerViewModel.observeAuthState().observe(this, new Observer<Resource<Player>>() {
            @Override
            public void onChanged(Resource<Player> playerResource) {
                if (playerResource != null) {
                    switch (playerResource.status) {
                        case LOADING: {
                            Log.d(TAG, "onChanged: PostsFragment: LOADING...");
                            break;
                        }

                        case SUCCESS: {
                            Log.d(TAG, "onChanged: " + playerResource.data.getMessage());
                            break;
                        }

                        case ERROR: {
                            Log.d(TAG, "onChanged: PostsFragment: ERROR... " + playerResource.data.getMessage());
                            break;
                        }
                    }
                }
            }
        });
    }
}
