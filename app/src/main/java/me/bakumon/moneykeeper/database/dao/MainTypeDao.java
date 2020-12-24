package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.MainType;

@Dao
public interface MainTypeDao {
    @Query("SELECT * FROM maintype WHERE state = 0 ORDER BY ranking")
    Flowable<List<MainType>> getAllMainTypes();

    @Query("SELECT * FROM maintype WHERE state = 0 AND type = :type ORDER BY ranking")
    Flowable<List<MainType>> getMainTypes(int type);

    @Query("SELECT count(maintype.id) FROM maintype")
    long getMainTypeCount();

    @Query("SELECT * FROM maintype WHERE type = :type AND name = :name")
    MainType getMainTypeByName(int type, String name);

    @Insert
    void insertMainTypes(MainType... mainTypes);

    @Update
    void updateMainTypes(MainType... mainTypes);

    @Delete
    void deleteMainType(MainType mainType);
}

