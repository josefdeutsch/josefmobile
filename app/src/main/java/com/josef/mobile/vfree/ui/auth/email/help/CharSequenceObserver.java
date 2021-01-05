package com.josef.mobile.vfree.ui.auth.email.help;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class CharSequenceObserver<T> {

    PublishSubject<T> subject = PublishSubject.create();

    public CharSequenceObserver() {

    }

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
