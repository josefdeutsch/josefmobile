package com.josef.mobile.data.local.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.josef.mobile.data.local.db.model.Archive;

import java.util.List;

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
    void insert(Archive user);

    @Update(entity = Archive.class)
    void update(Archive archive);

    @Query("SELECT * FROM archive")
    Flowable<List<Archive>> loadAllArchives();

    @Query("SELECT * FROM archive WHERE id IN (:userIds)")
    List<Archive> loadAllByIds(List<Integer> userIds);
}
