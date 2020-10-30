package com.josef.mobile.data.local.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "archive")
public class Archive {

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "png")
    public String png;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "updated_at")
    public String updatedAt;

    public Archive(long id, String name, String png, String url) {
        this.id = id;
        this.name = name;
        this.png = png;
        this.url = url;
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
}
