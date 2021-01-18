package com.josef.mobile.vfree.ui.auth.email.help;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public final class QuartetLiveData<A, B, C, D> extends MediatorLiveData<Quartet<A, B, C, D>> {

    public @NonNull
    A a;
    public @NonNull
    B b;
    public @NonNull
    C c;
    public @NonNull
    D d;

    public void combinedLiveData(LiveData<A> ld1, LiveData<B> ld2, LiveData<C> ld3, LiveData<D> ld4) {
            setValue(Quartet.create(a, b, c, d));

            addSource(ld1, a -> {
                if (a != null) {
                    QuartetLiveData.this.a = a;
                }
                QuartetLiveData.this.setValue(Quartet.create(a, b, c, d));
            });
            addSource(ld2, b -> {
                if (b != null) {
                    QuartetLiveData.this.b = b;
                }
                QuartetLiveData.this.setValue(Quartet.create(a, b, c, d));
            });

            addSource(ld3, c -> {
                if (c != null) {
                    QuartetLiveData.this.c = c;
                }
                QuartetLiveData.this.setValue(Quartet.create(a, b, c, d));
            });

            addSource(ld4, d -> {
                if (d != null) {
                    QuartetLiveData.this.d = d;
                }
                QuartetLiveData.this.setValue(Quartet.create(a, b, c, d));
            });

    }


    public void removeLiveData(LiveData<A> ld1, LiveData<B> ld2, LiveData<C> ld3, LiveData<D> ld4) {
        removeSource(ld1);
        removeSource(ld2);
        removeSource(ld3);
        removeSource(ld4);
    }
}
