package com.josef.mobile.vfree.ui.auth.email.helper;

import io.reactivex.rxjava3.subjects.PublishSubject;

public final class CharSequenceObserver<T> {

    private final PublishSubject<T> subject = PublishSubject.create();

    private boolean isValid(T t) {
        return t != null;
    }

    public PublishSubject<T> getSubject() {
        return subject;
    }

    public void setSubject(T t) {
        subject.onNext(t);
    }

    public io.reactivex.rxjava3.core.Observable<T> generateObservable() {
        return subject.hide();
    }

}
