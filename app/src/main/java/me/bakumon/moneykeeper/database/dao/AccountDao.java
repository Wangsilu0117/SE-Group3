package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Account;

@Dao
public interface AccountDao {

    @Query("SELECT * FROM account WHERE state = 0 ORDER BY rank")
    Flowable<List<Account>> getAllAccounts();

    @Query("SELECT count(account.id) FROM account")
    long getAccountCount();

    @Query("SELECT * FROM account WHERE name = :name")
    Account getAccountByName(String name);

    @Insert
    void insertAccounts(Account... accounts);

    @Update
    void updateAccounts(Account... accounts);

    @Delete
    void deleteAccounts(Account accounts);
}
