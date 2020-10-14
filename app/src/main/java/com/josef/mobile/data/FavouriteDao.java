package com.josef.mobile.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favourite note);

    @Update()
    void update(Favourite note);

    @Delete
    void delete(Favourite note);

    @Query("DELETE FROM favourite_table")
    void deleteAllNotes();

    @Query("SELECT * FROM favourite_table")
        //ORDER BY priority DESC
    LiveData<List<Favourite>> getAllNotes();
}

/**
 * @Entity class Favourite {
 * @ColumnInfo(name = "priority")
 * private int priority;
 * }
 **/