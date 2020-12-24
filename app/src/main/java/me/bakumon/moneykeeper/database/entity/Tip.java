package me.bakumon.moneykeeper.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Tip implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    //提示内容
    public String content;

    public Tip(String content){
        this.content = content;
    }


}
