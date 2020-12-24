package me.bakumon.moneykeeper.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(indices = {@Index(value = { "ranking", "state"})})
public class Member implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    /**
     * 排序
     */
    public long ranking;

    /**
     * 状态
     * 0：正常
     * 1：已删除
     *
     * @see RecordType#STATE_NORMAL
     * @see RecordType#STATE_DELETED
     */
    @Ignore
    public static int STATE_NORMAL = 0;
    @Ignore
    public static int STATE_DELETED = 1;

    public int state;



    @Ignore
    public Member(String name, long ranking) {
        this.name = name;
        this.ranking = ranking;
    }
    public Member(int id, String name, long ranking) {
        this.id = id;
        this.name = name;
        this.ranking = ranking;
    }
}
