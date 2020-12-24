package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Store;

@Dao
public interface StoreDao {
    @Insert
    void insertStore(Store... stores);

    @Update
    void updateStore(Store... stores);

    @Delete
    void deleteStore(Store... stores);

    @Query("SELECT * FROM Store WHERE state=0 ORDER BY ranking")
    Flowable <List<Store>> getAllStores();

    @Query("SELECT count(store.id) FROM Store")
    long getStoreCount();

    @Query("SELECT * FROM Store WHERE name = :name")
    Store getStoreByName(String name);
}
