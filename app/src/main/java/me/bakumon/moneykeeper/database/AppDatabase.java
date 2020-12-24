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

package me.bakumon.moneykeeper.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.database.converters.Converters;
import me.bakumon.moneykeeper.database.dao.AccountDao;
import me.bakumon.moneykeeper.database.dao.MainTypeDao;
import me.bakumon.moneykeeper.database.dao.MemberDao;
import me.bakumon.moneykeeper.database.dao.ProjectDao;
import me.bakumon.moneykeeper.database.dao.RecordDao;
import me.bakumon.moneykeeper.database.dao.RecordTypeDao;
import me.bakumon.moneykeeper.database.dao.StoreDao;
import me.bakumon.moneykeeper.database.dao.TipDao;
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.database.entity.MainType;
import me.bakumon.moneykeeper.database.entity.Member;
import me.bakumon.moneykeeper.database.entity.Project;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.Store;
import me.bakumon.moneykeeper.database.entity.Tip;


/**
 * 数据库
 *
 * @author Bakumon https:bakumon.me
 */
@Database(entities = {Record.class, RecordType.class, Account.class, Tip.class
        , Member.class, MainType.class, Store.class, Project.class
        }, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "MoneyKeeper.db";

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(App.getINSTANCE(),
                            AppDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取记账类型操作类
     *
     * @return RecordTypeDao 记账类型操作类
     */
    public abstract RecordTypeDao recordTypeDao();

    /**
     * 获取记账操作类
     *
     * @return RecordDao 记账操作类
     */
    public abstract RecordDao recordDao();

    public abstract AccountDao accountDao();


    /**
     * 获取记账类型操作类
     *
     * @return MemberDao 成员操作类
     * @return StoreDao  商家操作类
     * @return ProjectDao  项目操作类
     */
    public abstract MemberDao memberDao();
    public abstract StoreDao storeDao();
    public abstract ProjectDao projectDao();

    //新加-------------------------------------------------
    /**
     * 获取记账主类操作类
     */
    public abstract MainTypeDao mainTypeDao();

    public abstract TipDao tipDao();


}
