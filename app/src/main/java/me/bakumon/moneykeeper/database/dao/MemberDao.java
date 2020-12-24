package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Member;

@Dao
public interface MemberDao {
    @Insert
    void insertMember(Member... members);

    @Update
    void updateMember(Member... members);

    @Delete
    void deleteMember(Member... members);

    @Query("SELECT * FROM Member WHERE state=0 ORDER BY ranking")
    Flowable <List<Member>> getAllMembers();

    @Query("SELECT count(member.id) FROM member")
    long getMemberCount();

    @Query("SELECT * FROM member WHERE name = :name")
    Member getMemberByName(String name);

}
