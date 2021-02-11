package com.josef.mobile.vfree.ui.main.archive.fire.model;

import com.google.firebase.database.annotations.Nullable;

import java.util.Objects;

public final class Data {

    @Nullable
    private String png;
    @Nullable
    private String url;
    @Nullable
    private String email;

    public Data( @Nullable String png,
                 @Nullable String url,
                 @Nullable String email) {

        this.png = png;
        this.url = url;
        this.email = email;
    }

    @Nullable
    public String getPng() {
        return Objects.requireNonNull(png,
                "com.josef.mobile.vfree.ui.main.archive.fire.model.Data Data png must not be null" );
    }

    public void setPng(@Nullable String png) {
        this.png = png;
    }

    @Nullable
    public String getUrl() {
        return Objects.requireNonNull(url,
                "com.josef.mobile.vfree.ui.main.archive.fire.model.Data Data url must not be null" );
    }

    public void setUrl(@Nullable String url) {
        this.url = url;
    }

    @Nullable
    public String getEmail() {
        return Objects.requireNonNull(email,
                "com.josef.mobile.vfree.ui.main.archive.fire.model.Data Data email must not be null" );
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }
}
