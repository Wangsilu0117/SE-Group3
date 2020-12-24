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
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 记账记录
 *
 * @author bakumon https://bakumon.me
 */
@Entity(foreignKeys = {@ForeignKey(entity = RecordType.class, parentColumns = "id", childColumns = "record_type_id"),
                        //@ForeignKey(entity = Account.class,parentColumns = "id",childColumns = "account_id")
        },
        indices = {@Index(value = {"record_type_id","account_id","time","money", "create_time", "main_type_id"})})
public class Record implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public BigDecimal money;

    public String remark;

    public Date time;

    @ColumnInfo(name = "account_id")
    public int accountId;

    @ColumnInfo(name = "create_time")
    public Date createTime;

    @ColumnInfo(name = "record_type_id")
    public int recordTypeId;
    //新加----------------------------------------------------------------
    @ColumnInfo(name = "main_type_id")
    public int mainTypeId;

    @ColumnInfo(name = "member_Id")
    public int memberId;
//为2，3都是转账，但2是-（outlay），3是+（income）
    @ColumnInfo(name = "type")
    public int type;
//    wsl:add-------------------------------------------------------------
    @ColumnInfo(name = "store_Id")
    public int storeId;

    @ColumnInfo(name = "project_Id")
    public int projectId;

}
