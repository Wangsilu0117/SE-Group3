package me.bakumon.moneykeeper.database.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "account",
        indices = {@Index(value = {"rank","state"})})
public class Account implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    //账户名称
    public String name;

    //排序
    public long rank;

    @Ignore
    public static int STATE_NORMAL = 0;
    @Ignore
    public static int STATE_DELETED = 1;
    /**
     * 账户的状态，为自定义选项的功能服务
     * 0表示正常，1表示被删除
     */
    public int state;

    //是否被选中
    @Ignore
    public boolean isChecked;

    @Ignore
    public Account(String name){
        this.name = name;
    }

    @Ignore
    public Account(String name, long rank){
        this.name = name;
        this.rank = rank;
    }

    public Account(int id, String name, long rank){
        this.id = id;
        this.name = name;
        this.rank = rank;
    }

}
