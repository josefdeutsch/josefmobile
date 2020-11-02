package com.josef.mobile.ui.base;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.josef.mobile.SessionManager;
import com.josef.mobile.utils.AppUtilManager;
import com.josef.mobile.utils.UtilManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseViewModel extends ViewModel implements Base {

    private static final String TAG = "BaseViewModel";


    UtilManager utilManager;

    @Inject
    SessionManager sessionManager;

    private boolean isInternet;

    public final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseViewModel() {
        utilManager = new AppUtilManager();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    public void network() {

        compositeDisposable.add(
                utilManager.isInternet()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Boolean>() {
                            @Override
                            public void onSuccess(@NonNull Boolean aBoolean) {
                                Log.d(TAG, "onSuccess: " + aBoolean);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        }));
    }

}
