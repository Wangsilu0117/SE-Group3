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

package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.AccountSumMoneyBean;
import me.bakumon.moneykeeper.database.entity.DaySumMoneyBean;
import me.bakumon.moneykeeper.database.entity.Member;
import me.bakumon.moneykeeper.database.entity.MemberSumMoneyBean;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;
import me.bakumon.moneykeeper.database.entity.MainType;
/**
 * 记账记录表操作类
 *
 * @author Bakumon https://bakumon.me
 */
@Dao
public interface RecordDao {

    @Transaction
    @Query("SELECT * from record WHERE time BETWEEN :from AND :to ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes(Date from, Date to);

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (RecordType.type=:type AND time BETWEEN :from AND :to) ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes(Date from, Date to, int type);

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (RecordType.type=:type AND record.record_type_id=:typeId AND time BETWEEN :from AND :to) ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes(Date from, Date to, int type, int typeId);

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (RecordType.type=:type AND record.record_type_id=:typeId AND time BETWEEN :from AND :to) ORDER BY money DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRecordWithTypesSortMoney(Date from, Date to, int type, int typeId);

    @Insert
    void insertRecord(Record record);

    @Update
    void updateRecords(Record... records);

    @Delete
    void deleteRecord(Record record);

    @Query("SELECT recordType.type AS type, sum(record.money) AS sumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE time BETWEEN :from AND :to GROUP BY RecordType.type")
    Flowable<List<SumMoneyBean>> getSumMoney(Date from, Date to);


    @Query("SELECT count(id) FROM record WHERE record_type_id = :typeId")
    long getRecordCountWithTypeId(int typeId);

    @Query("SELECT * FROM record WHERE record_type_id = :typeId")
    List<Record> getRecordsWithTypeId(int typeId);

    /**
     * 尽量使用 Flowable 返回，因为当数据库数据改变时，会自动回调
     * 而直接用 List ，在调用的地方自己写 Flowable 不会自动回调
     */
    @Query("SELECT recordType.type AS type, record.time AS time, sum(record.money) AS daySumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id where (RecordType.type=:type and record.time BETWEEN :from AND :to) GROUP BY record.time")
    Flowable<List<DaySumMoneyBean>> getDaySumMoney(Date from, Date to, int type);

    @Query("SELECT t_type.img_name AS imgName,t_type.name AS typeName, record.record_type_id AS typeId,sum(record.money) AS typeSumMoney, count(record.record_type_id) AS count FROM record LEFT JOIN RecordType AS t_type ON record.record_type_id=t_type.id where (t_type.type=:type and record.time BETWEEN :from AND :to) GROUP by record.record_type_id Order by sum(record.money) DESC")
    Flowable<List<TypeSumMoneyBean>> getTypeSumMoney(Date from, Date to, int type);

    /**
     * 账户
     */
    /**
     * 返回分帐户记账的支出/收入的总和
     */

    //    lxy下:
    @Transaction
    @Query("SELECT * from record WHERE (record.account_id = :accountId) ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRecordWithTypes_Account(int accountId);
    //    lxy上

    @Transaction
    @Query("SELECT * from record WHERE (record.account_id = :accountId AND time BETWEEN :from AND :to) ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes_Account(int accountId, Date from, Date to);

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (record.account_id = :accountId AND RecordType.type=:type AND time BETWEEN :from AND :to) ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes_Account(int accountId, Date from, Date to, int type);

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (record.account_id = :accountId AND RecordType.type=:type AND record.record_type_id=:typeId AND time BETWEEN :from AND :to) ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes_Account(int accountId, Date from, Date to, int type, int typeId);

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (record.account_id = :accountId AND RecordType.type=:type AND record.record_type_id=:typeId AND time BETWEEN :from AND :to) ORDER BY money DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRecordWithTypesSortMoney_Account(int accountId, Date from, Date to, int type, int typeId);


    @Query("SELECT recordType.type AS type, sum(record.money) AS sumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (record.account_id = :accountId AND time BETWEEN :from AND :to) GROUP BY RecordType.type")
    Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId, Date from, Date to);

    @Query("SELECT recordType.type AS type, sum(record.money) AS sumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (record.account_id = :accountId ) GROUP BY RecordType.type")
    Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId);

    @Query("SELECT recordType.type AS type, sum(record.money) AS sumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id  GROUP BY RecordType.type")
    Flowable<List<SumMoneyBean>> getSumMoney_allAccount();

    @Query("SELECT count(id) FROM record WHERE (record_type_id = :typeId AND record.account_id = :accountId)")
    long getRecordCountWithTypeId_Account(int accountId, int typeId);

    @Query("SELECT * FROM record WHERE (record_type_id = :typeId AND record.account_id = :accountId)")
    List<Record> getRecordsWithTypeId_Account(int accountId, int typeId);

    @Query("SELECT recordType.type AS type, record.time AS time, sum(record.money) AS daySumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id where (record.account_id = :accountId and RecordType.type=:type and record.time BETWEEN :from AND :to) GROUP BY record.time")
    Flowable<List<DaySumMoneyBean>> getDaySumMoney_Account(int accountId, Date from, Date to, int type);

    @Query("SELECT t_type.img_name AS imgName," +
            "t_type.name AS typeName, " +
            "record.record_type_id AS typeId," +
            "sum(record.money) AS typeSumMoney, " +
            "count(record.record_type_id) AS count FROM record " +
            "LEFT JOIN RecordType AS t_type ON record.record_type_id=t_type.id " +
            "where (record.account_id = :accountId and t_type.type=:type and record.time BETWEEN :from AND :to) " +
            "GROUP by record.record_type_id " +
            "Order by sum(record.money) DESC")
    Flowable<List<TypeSumMoneyBean>> getTypeSumMoney_Account(int accountId, Date from, Date to, int type);


    //新加10---------------------------------------------------------------------------------------------------------
    @Query("SELECT count(id) FROM record WHERE account_id = :accountId")
    long getRecordCountWithAccountId(int accountId);

    //新加11---------------------------------------------------------------------------------------------------------
    @Query("SELECT count(id) FROM record WHERE member_id = :memberId")
    long getRecordCountWithMemberId(int memberId);
    /**
     * 成员
     */
    /**
     * 返回分成员账户记账/收入的总和
     */
//    @Query("SELECT t_member.name AS memberName, " +
//            "record.memberid AS memberId," +
//            "sum(record.money) AS memberSumMoney, " +
//            "count(record.memberId) AS count FROM record " +
//            "LEFT JOIN Member AS t_member ON record.memberId=t_member.id " +
//            "where (t_member.id=:memberId and record.time BETWEEN :from AND :to) " +
//            "GROUP by record.memberId " +
//            "Order by sum(record.money) DESC")
//    Flowable<List<MemberSumMoneyBean>> getMemberSumMoney(Date from, Date to, int memberId);
//
//}
    @Query("SELECT" +
            " t_member.name AS memberName, " +
            "record.member_Id AS memberId," +
            "sum(record.money) AS memberSumMoney, " +
            "count(record.member_Id) AS count FROM record " +
            "LEFT JOIN Member AS t_member ON record.member_Id=t_member.id " +
            "where (record.time BETWEEN :from AND :to and record.type = :type) " +
            "GROUP by record.member_Id " +
            "Order by sum(record.money) DESC")
    Flowable<List<MemberSumMoneyBean>> getMemberSumMoney(Date from, Date to, int type);
//    @Query("SELECT t_type.img_name AS imgName,t_type.name AS typeName, record.record_type_id AS typeId,sum(record.money) AS typeSumMoney, count(record.record_type_id) AS count FROM record LEFT JOIN RecordType AS t_type ON record.record_type_id=t_type.id where (t_type.type=:type and record.time BETWEEN :from AND :to) GROUP by record.record_type_id Order by sum(record.money) DESC")
//    Flowable<List<MemberSumMoneyBean>> getMemberSumMoney(Date from, Date to,int type);

    //新加10-------------------------------------------------------------------------------
    @Query("SELECT * FROM record WHERE account_id = :accountId")
    List<Record> getRecordsWithAccountId(int accountId);


    // lxy:
    @Query("SELECT t_type.imgName AS imgName, t_type.name AS typeName, record.main_type_id AS typeId,sum(record.money) AS typeSumMoney, count(record.main_type_id) AS count FROM record LEFT JOIN MainType AS t_type ON record.main_type_id=t_type.id where (t_type.type=:type and record.time BETWEEN :from AND :to) GROUP by record.main_type_id Order by sum(record.money) DESC")
    Flowable<List<TypeSumMoneyBean>> getTypeSumMoneyOne(Date from, Date to, int type);

    // lxy:
//    @Query("SELECT record.account_id AS account, t_account.name AS accountName, recordType.type AS type, sum(record.money) AS accountSumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id  LEFT JOIN Account AS t_account ON record.account_id= t_account.id  GROUP BY record.account_id, RecordType.type")
//    Flowable<List<AccountSumMoneyBean>> getSumMoneyAccount();

    //wsl 新加不知道有没有用，如果按商家显示图表的话就要改-------------------------------------------------------------------------------
    @Query("SELECT * FROM record WHERE store_Id = :storeId")
    List<Record> getRecordsWithStoreId(int storeId);

    //wsl 新加不知道有没有用，如果按商家显示图表的话就要改-------------------------------------------------------------------------------
    @Query("SELECT * FROM record WHERE project_Id = :projectId")
    List<Record> getRecordsWithProjectId(int projectId);

    @Query("SELECT t_type.imgName AS imgName,"+
            " t_type.name AS typeName, record.store_Id AS typeId,sum(record.money) AS typeSumMoney, "+
            "count(record.store_Id) AS count FROM record LEFT JOIN Store AS t_type ON record.store_Id=t_type.id "+
            "LEFT JOIN RecordType AS r_type ON record.record_type_id=r_type.id " +
            "where (r_type.type=:type and record.time BETWEEN :from AND :to) "+
            "GROUP by record.store_Id Order by sum(record.money) DESC")
    Flowable<List<TypeSumMoneyBean>> getStoreSumMoney(Date from, Date to, int type);

    @Query("SELECT t_type.imgName AS imgName,"+
            " t_type.name AS typeName, record.project_Id AS typeId,sum(record.money) AS typeSumMoney, "+
            "count(record.project_Id) AS count FROM record LEFT JOIN Project AS t_type ON record.project_id=t_type.id "+
            "LEFT JOIN RecordType AS r_type ON record.record_type_id=r_type.id " +
            "where (r_type.type=:type and record.time BETWEEN :from AND :to) "+
            "GROUP by record.project_Id Order by sum(record.money) DESC")
    Flowable<List<TypeSumMoneyBean>> getProjectSumMoney(Date from, Date to, int type);


    // type为收支，typeId为StoreId
    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id "+
            "LEFT JOIN Store AS t_type ON record.store_Id=t_type.id "+
            "WHERE (RecordType.type=:type AND t_type.id=:typeId AND time BETWEEN :from AND :to) "+
            "ORDER BY money DESC, create_time DESC")
    Flowable<List<RecordWithType>> getStoreRecordWithTypesSortMoney(Date from, Date to, int type, int typeId);


    // type为收支，typeId为ProjectId
    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id "+
            "LEFT JOIN Project AS t_type ON record.project_Id=t_type.id "+
            "WHERE (RecordType.type=:type AND t_type.id=:typeId AND time BETWEEN :from AND :to) "+
            "ORDER BY money DESC, create_time DESC")
    Flowable<List<RecordWithType>> getProjectRecordWithTypesSortMoney(Date from, Date to, int type, int typeId);
}
