package com.josef.mobile.vfree.data.remote.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public final class Endpoint {

    @SerializedName("message")
    public String message;

    public long id;

    public Endpoint(@Nullable String message) {
        this.message = message;
    }

    public String getMessage() {
        return  Objects.requireNonNull(message, "Message must not be null" );
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }
}
