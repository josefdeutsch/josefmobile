package com.josef.mobile.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.josef.mobile.data.local.db.dao.ArchiveDao;
import com.josef.mobile.data.local.db.dao.EndpointsDao;
import com.josef.mobile.data.local.db.model.Archive;
import com.josef.mobile.data.local.db.model.LocalCache;

@Database(entities = {Archive.class, LocalCache.class}, version = 6)
public abstract class AppDataBase extends RoomDatabase {

    public abstract EndpointsDao endpontsDao();

    public abstract ArchiveDao archiveDao();

}