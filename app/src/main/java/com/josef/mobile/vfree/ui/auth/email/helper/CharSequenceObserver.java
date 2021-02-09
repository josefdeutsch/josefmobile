package com.josef.mobile.vfree.ui.auth.email.helper;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public final class CharSequenceObserver<T> {

    @NonNull
    private final PublishSubject<T> subject = PublishSubject.create();

    public void setSubject(@NonNull T t) {
        subject.onNext(t);
    }

    @NonNull
    public Observable<T> generateObservable() {
        return subject.hide();
    }

}
