package me.bakumon.moneykeeper.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
@Entity(indices = {@Index(value = {"type", "ranking", "state"})})
public class MainType implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * 图片 name（本地mipmap）
     */
    //@ColumnInfo(name = "img_name")
    public String imgName;

    public String name;

    @Ignore
    public static int TYPE_OUTLAY = 0;
    @Ignore
    public static int TYPE_INCOME = 1;
    @Ignore
    public static int TYPE_TRANSFER = 2;
    /**
     * 类型
     * 0：支出
     * 1：收入
     * 2: 转账
     *
     * @see MainType#TYPE_OUTLAY
     * @see MainType#TYPE_INCOME
     * @see MainType#TYPE_TRANSFER
     */
    public int type;
    /**
     * 排序
     */
    public long ranking;
    @Ignore
    public static int STATE_NORMAL = 0;
    @Ignore
    public static int STATE_DELETED = 1;
    /**
     * 状态
     * 0：正常
     * 1：已删除
     *
     * @see RecordType#STATE_NORMAL
     * @see RecordType#STATE_DELETED
     */
    public int state;
    /**
     * 是否选中，用于 UI
     */
    @Ignore
    public boolean isChecked;
    @Ignore
    public MainType(String name, int type, long ranking) {
        this.name = name;
        this.type = type;
        this.ranking = ranking;
    }

    public MainType(int id, String name, int type,long ranking) {
        this.id = id;
        this.name = name;
        this.ranking = ranking;
        this.type = type;
    }

    @Ignore
    public MainType(String name, String imgName, int type, long ranking) {
        this.name = name;
        this.imgName = imgName;
        this.type = type;
        this.ranking = ranking;

    }
}
