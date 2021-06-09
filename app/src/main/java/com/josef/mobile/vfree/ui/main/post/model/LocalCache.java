package com.josef.mobile.vfree.ui.main.post.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

@Entity(tableName = "localcache")
public final class LocalCache {

    @Nullable
    @ColumnInfo(name = "created_at")
    @SerializedName("id;")
    public String createdAt;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @Nullable
    @ColumnInfo(name = "position")
    @SerializedName("position")
    public int position;

    @Nullable
    @ColumnInfo(name = "flag")
    @SerializedName("flag")
    public boolean flag;

    @Nullable
    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String name;

    @Nullable
    @ColumnInfo(name = "url")
    @SerializedName("url")
    public String url;

    @Nullable
    @ColumnInfo(name = "tag")
    @SerializedName("tag")
    public String tag;

    @Nullable
    @ColumnInfo(name = "png")
    @SerializedName("png")
    public String png;

    @Nullable
    @ColumnInfo(name = "buy")
    @SerializedName("buy")
    public String buy;


    @Nullable
    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    public String updatedAt;

    @NonNull
    public String exception;
    @NonNull
    public String getException() {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }


    public LocalCache(@NonNull Long id,
                      @Nullable boolean flag,
                      @Nullable String name,
                      @Nullable String url,
                      @Nullable String tag,
                      @Nullable String png) {

        this.id = id;
        this.flag = flag;
        this.name = name;
        this.url = url;
        this.tag = tag;
        this.png = png;
    }

    public LocalCache(@Nullable String name,
                      @Nullable String png,
                      @Nullable String url) {

        this.name = name;
        this.png = png;
        this.url = url;
    }

    public LocalCache(@Nullable long id,
                      @Nullable String name) {

        this.id = id;
        this.name = name;
    }

    public LocalCache() {
    }

    @Nullable
    public String getCreatedAt() {
        return Objects.requireNonNull(createdAt,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "createdAt must not be null" );
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }

    @NonNull
    public Long getId() {
        return Objects.requireNonNull(id,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "id must not be null" );
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public int getPosition() {
        return Objects.requireNonNull(position,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "createdAt must not be null" );
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFlag() {
        return Objects.requireNonNull(flag,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "flag must not be null" );
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Nullable
    public String getName() {
        return Objects.requireNonNull(name,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "name must not be null" );
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getUrl() {
        return Objects.requireNonNull(url,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "url must not be null" );
    }

    public void setUrl(@Nullable String url) {
        this.url = url;
    }

    @Nullable
    public String getTag() {
         return Objects.requireNonNull(tag,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "tag must not be null" );
    }

    public void setTag(@Nullable String tag) {
        this.tag = tag;
    }

    @Nullable
    public String getPng() {
        return Objects.requireNonNull(png,
                "com.josef.mobile.vfree.ui.main.post.model.LocalCache " +
                        "png must not be null" );
    }

    public void setPng(@Nullable String png) {
        this.png = png;
    }

    @Nullable
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Nullable
    public String getBuy() {
        return buy;
    }

    public void setBuy(@Nullable String buy) {
        this.buy = buy;
    }
    public void setUpdatedAt(@Nullable String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
