package com.josef.mobile.vfree.ui.auth.email.help;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class QuartetLiveData<A, B, C, D> extends MediatorLiveData<Quartet<A, B, C, D>> {

    public @Nullable
    A a;
    public @Nullable
    B b;
    public @Nullable
    C c;
    public @Nullable
    D d;

    public QuartetLiveData(){

    }

    public QuartetLiveData(@Nullable A a, @Nullable B b, @Nullable C c, @Nullable D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

    }

    public void combinedLiveData(LiveData<A> ld1, LiveData<B> ld2, LiveData<C> ld3, LiveData<D> ld4) {
        setValue(Quartet.create(a, b, c, d));

        addSource(ld1, new Observer<A>() {
            @Override
            public void onChanged(A a) {
                if (a != null) {
                    QuartetLiveData.this.a = a;
                }
                QuartetLiveData.this.setValue(Quartet.create(a, b, c, d));
            }
        });
        addSource(ld2, new Observer<B>() {
            @Override
            public void onChanged(B b) {
                if (b != null) {
                    QuartetLiveData.this.b = b;
                }
                QuartetLiveData.this.setValue(Quartet.create(a, b, c, d));
            }
        });

        addSource(ld3, new Observer<C>() {
            @Override
            public void onChanged(C c) {
                if (c != null) {
                    QuartetLiveData.this.c = c;
                }
                QuartetLiveData.this.setValue(Quartet.create(a, b, c, d));
            }
        });

        addSource(ld4, new Observer<D>() {
            @Override
            public void onChanged(D d) {
                if (d != null) {
                    QuartetLiveData.this.d = d;
                }
                QuartetLiveData.this.setValue(Quartet.create(a, b, c, d));
            }
        });
    }


    public void removeLiveData(LiveData<A> ld1, LiveData<B> ld2, LiveData<C> ld3,LiveData<D> ld4) {
        removeSource(ld1);
        removeSource(ld2);
        removeSource(ld3);
        removeSource(ld4);
    }
}
