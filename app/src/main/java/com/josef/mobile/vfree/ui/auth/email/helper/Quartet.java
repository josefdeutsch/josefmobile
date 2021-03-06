package com.josef.mobile.vfree.ui.auth.email.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Pair;

import java.util.Objects;

public final class Quartet<A, B, C, D> {

    @Nullable
    public final A a;
    @Nullable
    public final B b;
    @Nullable
    public final C c;
    @Nullable
    public final D d;


    public Quartet(@Nullable A a, @Nullable B b, @Nullable C c, @Nullable D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Quartet<?, ?, ?, ?> p = (Quartet<?, ?, ?, ?>) o;
        return ObjectsCompat.equals(p.a, a) && ObjectsCompat.equals(p.b, b)
                && ObjectsCompat.equals(p.c, c) && ObjectsCompat.equals(p.d, d);
    }

    @Override
    public int hashCode() {
        return (a == null ? 0 : a.hashCode()) ^ (b == null ? 0 : b.hashCode()
                ^ (c == null ? 0 : c.hashCode() ^ (d == null ? 0 : d.hashCode())));
    }

    @NonNull
    @Override
    public String toString() {
        return "Quartet{" + String.valueOf(a) + " " + String.valueOf(b) + String.valueOf(c) + String.valueOf(d) + "}";
    }

    @NonNull
    public static <A, B, C, D> Quartet<A, B, C, D> create(
            @Nullable A a,
            @Nullable B b,
            @Nullable C c,
            @Nullable D d) throws NullPointerException {

        return new Quartet<>(a, b, c, d);
    }

    @NonNull
    private static boolean checkForNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    public A getA() {
        return Objects.requireNonNull(a,
                "com.josef.mobile.vfree.ui.auth.email.helper.Quartet A must not be null" );
    }

    @NonNull
    public B getB() {
        return Objects.requireNonNull(b,
                "com.josef.mobile.vfree.ui.auth.email.helper.Quartet B must not be null" );
    }

    @NonNull
    public C getC() {
        return Objects.requireNonNull(c,
                "com.josef.mobile.vfree.ui.auth.email.helper.Quartet C must not be null" );
    }

    @NonNull
    public D getD() {
        return Objects.requireNonNull(d,
                "com.josef.mobile.vfree.ui.auth.email.helper.Quartet D must not be null" );
    }
}
