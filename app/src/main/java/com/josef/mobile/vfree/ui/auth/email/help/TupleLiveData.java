package com.josef.mobile.vfree.ui.auth.email.help;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
public class TupleLiveData<A, B> extends MediatorLiveData<Pair<A, B>> {

    private A a;
    private B b;

    public TupleLiveData() {

    }

    public void combinedLiveData(LiveData<A> ld1, LiveData<B> ld2) {
        setValue(Pair.create(a, b));

        addSource(ld1, a -> {
            if (a != null) {
                TupleLiveData.this.a = a;
            }
            TupleLiveData.this.setValue(Pair.create(a, b));
        });
        addSource(ld2, b -> {
            if (b != null) {
                TupleLiveData.this.b = b;
            }
            TupleLiveData.this.setValue(Pair.create(a, b));
        });
    }


    public void removeLiveData(LiveData<A> ld1, LiveData<B> ld2) {
        removeSource(ld1);
        removeSource(ld2);
    }

}


// Below is a Java program to create
// a Quartet tuple from Constructor



