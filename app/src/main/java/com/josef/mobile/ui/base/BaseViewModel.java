package com.josef.mobile.ui.base;

import androidx.lifecycle.ViewModel;

import com.josef.mobile.SessionManager;
import com.josef.mobile.utils.UtilManager;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel implements Base {

    private static final String TAG = "BaseViewModel";


    UtilManager utilManager;

    @Inject
    SessionManager sessionManager;

    private boolean isInternet;

    public final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseViewModel() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }



}
