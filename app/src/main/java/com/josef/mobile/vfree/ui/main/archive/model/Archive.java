package com.josef.mobile.vfree.ui.main.archive.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

@Entity(tableName = "archive")
public final class Archive {

    @Nullable
    @ColumnInfo(name = "created_at")
    public String createdAt;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public Long id;
    @Nullable
    @ColumnInfo(name = "position")
    public int position;

    @Nullable
    @ColumnInfo(name = "flag")
    public boolean flag;

    @Nullable
    @ColumnInfo(name = "name")
    public String name;

    @Nullable
    @ColumnInfo(name = "url")
    public String url;

    @Nullable
    @ColumnInfo(name = "tag")
    public String tag;

    @Nullable
    @ColumnInfo(name = "png")
    public String png;

    @Nullable
    @ColumnInfo(name = "updated_at")
    public String updatedAt;

    public String exception;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Archive(long id, boolean flag, String name, String png, String url, String tag) {
        this.id = id;
        this.name = name;
        this.png = png;
        this.url = url;
        this.tag = tag;
        this.flag = flag;
    }

    public Archive(String name, String png, String url) {
        this.name = name;
        this.png = png;
        this.url = url;
    }

    public Archive(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Archive() {
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPng() {
        return png;
    }

    public void setPng(String png) {
        this.png = png;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
