package com.josef.mobile.vfree.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class AuthResource<T> {

    @NonNull
    public final AuthStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public AuthResource(@NonNull AuthStatus status,
                        @Nullable T data,
                        @Nullable String message) {

        this.status = status;
        this.data = data;
        this.message = message;
    }

    @NonNull
    public static <T> AuthResource<T> authenticated(@Nullable T data) {
        return new AuthResource<>(AuthStatus.AUTHENTICATED, data, null);
    }

    @NonNull
    public static <T> AuthResource<T> error(@NonNull String msg, @Nullable T data) {
        return new AuthResource<>(AuthStatus.ERROR, data, msg);
    }

    @NonNull
    public static <T> AuthResource<T> loading(@Nullable T data) {
        return new AuthResource<>(AuthStatus.LOADING, data, null);
    }

    @NonNull
    public static <T> AuthResource<T> logout() {
        return new AuthResource<>(AuthStatus.NOT_AUTHENTICATED, null, null);
    }

    public enum AuthStatus {AUTHENTICATED, ERROR, LOADING, NOT_AUTHENTICATED}

}


















