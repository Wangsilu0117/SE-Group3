/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.bakumon.moneykeeper.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * 记账类型
 *
 * @author bakumon https://bakumon.me
 */
@Entity(indices = {@Index(value = {"type", "ranking", "state"})})
public class RecordType implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    /**
     * 图片 name（本地mipmap）
     */
    @ColumnInfo(name = "img_name")
    public String imgName;

    //新加-----------------------------------------------------------------------------------------------
    @ColumnInfo(name = "main_type_id")
    public int mainTypeId;

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
     * 2：转账
     *
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     * @see RecordType#TYPE_TRANSFER
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
    public RecordType(String name, String imgName, int type, int mainTypeId) {
        this.name = name;
        this.imgName = imgName;
        this.type = type;
        this.type = mainTypeId;
    }

    @Ignore
    public RecordType(String name, String imgName, int type, long ranking, int mainTypeId) {
        this.name = name;
        this.imgName = imgName;
        this.type = type;
        this.ranking = ranking;
        this.mainTypeId = mainTypeId;
    }

    public RecordType(int id, String name, String imgName, int type, long ranking, int mainTypeId) {
        this.id = id;
        this.name = name;
        this.imgName = imgName;
        this.type = type;
        this.ranking = ranking;
        this.mainTypeId = mainTypeId;
    }

}
