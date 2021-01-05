package com.josef.mobile.vfree.ui.base;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends ViewModel implements Base {

    private CompositeDisposable compositeDisposable;

    public BaseViewModel() {
    }

    public void addToCompositeDisposable(Disposable disposable) {
        getCompositeDisposable().add(disposable);
    }

    private void dispose() {
        getCompositeDisposable().dispose();
    }

    private void clear() {
        getCompositeDisposable().clear();
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        dispose();
        clear();
    }
}
