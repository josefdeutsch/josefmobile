package com.josef.mobile.ui.main.archive.helpers.firebase;

import com.josef.mobile.ui.main.MainActivity;

import io.reactivex.disposables.CompositeDisposable;

public interface FirebaseUpload {

    void synchronize(MainActivity mainActivity, CompositeDisposable compositeDisposable);
}
