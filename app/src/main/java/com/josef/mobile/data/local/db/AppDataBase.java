package com.josef.mobile.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.josef.mobile.data.local.db.dao.ArchiveDao;
import com.josef.mobile.data.local.db.model.Archive;


@Database(entities = {Archive.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract ArchiveDao archiveDao();

}