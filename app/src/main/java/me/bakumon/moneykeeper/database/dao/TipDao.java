package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Tip;

@Dao
public interface TipDao {

    @Query("SELECT * FROM tip WHERE id = :id")
    Flowable<Tip> getOneTip(int id);

    @Query("SELECT count(tip.id) FROM tip")
    long getTipCount();

    @Insert
    void insertTips(Tip... tips);

}
