package com.josef.mobile.ui.base;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel implements Base {

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
