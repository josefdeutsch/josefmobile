package com.josef.mobile.vfree.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.josef.mobile.vfree.data.local.db.model.LocalCache;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface EndpointsDao {

    @Delete
    void delete(LocalCache localCache);

    @Update(entity = LocalCache.class)
    void update(LocalCache localCache);

    @Query("SELECT * FROM localcache LIMIT 1")
    Flowable<LocalCache> getEndpoint();

    @Query("SELECT * FROM localcache WHERE name LIKE :name LIMIT 1")
    Single<LocalCache> findEndpointByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocalCache localCache);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllEndpoints(List<LocalCache> endpoints);

    @Query("SELECT * FROM localcache")
    Flowable<List<LocalCache>> loadAllEndpoints();

    @Query("SELECT * FROM localcache WHERE id IN (:userIds)")
    List<LocalCache> loadAllByIds(List<Integer> userIds);
}
