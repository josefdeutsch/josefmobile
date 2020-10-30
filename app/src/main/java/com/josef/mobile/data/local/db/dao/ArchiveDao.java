package com.josef.mobile.data.local.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.josef.mobile.ui.main.archive.model.Archive;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public interface ArchiveDao {

    @Delete
    void delete(Archive user);

    @Query("SELECT * FROM archive LIMIT 1")
    Flowable<Archive> getArchive();

    @Query("SELECT * FROM archive WHERE name LIKE :name LIMIT 1")
    Single<Archive> findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(Archive archive);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Archive user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Archive> users);

    @Query("SELECT * FROM archive")
    Flowable<List<Archive>> loadAll();

    @Query("SELECT * FROM archive WHERE id IN (:userIds)")
    List<Archive> loadAllByIds(List<Integer> userIds);
}
