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

package me.bakumon.moneykeeper.datasource;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.database.entity.AccountSumMoneyBean;
import me.bakumon.moneykeeper.database.entity.DaySumMoneyBean;
import me.bakumon.moneykeeper.database.entity.MainType;
import me.bakumon.moneykeeper.database.entity.Member;
import me.bakumon.moneykeeper.database.entity.MemberSumMoneyBean;
import me.bakumon.moneykeeper.database.entity.Project;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.Store;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.Tip;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;
import me.bakumon.moneykeeper.ui.addtype.TypeImgBean;

/**
 * 数据源
 *
 * @author Bakumon https://bakumon.me
 */
public interface AppDataSource {
    /**
     * 获取所有记账类型数据
     *
     * @return 所有记账类型数据
     */
    Flowable<List<RecordType>> getAllRecordTypes();

    //新加8————————————————————————————————————
    Flowable<List<MainType>> getAllMainTypes();

    //新加9-----------------------------------------------------------------------------
    Flowable<List<RecordType>> getAllRecordTypesWithMain(int mainTypeId);

    /**
     * 初始化默认的记账类型
     */
    Completable initRecordTypes();

    //新加----------------------------------------------------------------
    /**
     * 初始化记账的主类
     */
    Completable initMainTypes();

    /**
     * 获取当前月份的记账记录数据
     *
     * @return 当前月份的记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes();



    /**
     * 根据类型获取某段时间的记账记录数据
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getRecordWithTypes(Date dateFrom, Date dateTo, int type);

    /**
     * 获取某一类型某段时间的记账记录数据
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getRecordWithTypes(Date dateFrom, Date dateTo, int type, int typeId);

    /**
     * 获取某一类型某段时间的记账记录数据，money 排序
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getRecordWithTypesSortMoney(Date dateFrom, Date dateTo, int type, int typeId);

    /**
     * 新增一条记账记录
     *
     * @param record 记账记录实体
     */
    Completable insertRecord(Record record);

    /**
     * 更新一条记账记录
     *
     * @param record 记录对象
     */
    Completable updateRecord(Record record);

    /**
     * 删除一天记账记录
     *
     * @param record 要删除的记账记录
     */
    Completable deleteRecord(Record record);

    /**
     * 记账类型排序
     *
     * @param recordTypes 记账类型对象
     */
    Completable sortRecordTypes(List<RecordType> recordTypes);

    /**
     * 删除记账类型
     *
     * @param recordType 要删除的记账类型对象
     */
    Completable deleteRecordType(RecordType recordType);

    /**
     * 获取指出或收入记账类型数据
     *
     * @param type 类型
     * @return 记账类型数据
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    Flowable<List<RecordType>> getRecordTypes(int type);

    /**
     * 获取类型图片数据
     *
     * @param type 收入或支出类型
     * @return 所有获取类型图片数据
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    Flowable<List<TypeImgBean>> getAllTypeImgBeans(int type);

    /**
     * 添加一个记账类型
     *
     * @param type    类型
     * @param imgName 图片
     * @param name    类型名称
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    Completable addRecordType(int type, String imgName, String name,int mainTypeId);

    /**
     * 修改记账类型
     *
     * @param oldRecordType 修改之前的 RecordType
     * @param recordType    修改的 RecordType
     */
    Completable updateRecordType(RecordType oldRecordType, RecordType recordType);

    /**
     * 获取本月支出和收入总数
     */
    Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney();

    /**
     * 获取本月支出和收入总数
     */
    Flowable<List<SumMoneyBean>> getMonthSumMoney(Date dateFrom, Date dateTo);

    /**
     * 获取某天的合计
     *
     * @param year  年
     * @param month 月
     * @param type  类型
     */
    Flowable<List<DaySumMoneyBean>> getDaySumMoney(int year, int month, int type);

    /**
     * 获取按类型汇总数据
     */
    Flowable<List<TypeSumMoneyBean>> getTypeSumMoney(Date from, Date to, int type);


    /**
     * 账户
     */

    /**
     * 初始化默认的账户类型
     */
    Completable initAccounts();

    /**
     * 添加一条账户
     */
    Completable addAccount(String name);


    /**
     * 获取所有账户类型数据
     */
    Flowable<List<Account>> getAllAccounts();

    /**
     * 删除某条数据
     */
    Completable deleteAccount(Account account);

    /**
     * 修改记账类型
     *
     * @param oldAccount    修改之前的 RecordType
     * @param account    修改的 RecordType
     */
    Completable updateAccount(Account oldAccount, Account account);


//    wsl---------------------------------------------------------------------
    /**
     * 添加一条成员
     */
    Completable addMember(String name);

    /**
     * 删除某条数据
     */
    Completable deleteMember(Member member);

    /**
     * 修改记账类型
     *
     * @param oldMember    修改之前的 RecordType
     * @param member    修改的 RecordType
     */
    Completable updateMember(Member oldMember, Member member);


    /**
     * 获取当前月份的记账记录数据
     *
     * @return 当前月份的记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes_Account(int accountId);



    /**
     * 根据类型获取某段时间的记账记录数据
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getRecordWithTypes_Account(int accountId, Date dateFrom, Date dateTo, int type);

    /**
     * 获取某一类型某段时间的记账记录数据
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getRecordWithTypes_Account(int accountId, Date dateFrom, Date dateTo, int type, int typeId);

    /**
     * 获取某一类型某段时间的记账记录数据，money 排序
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getRecordWithTypesSortMoney_Account(int accountId, Date dateFrom, Date dateTo, int type, int typeId);

    //lxy:
    Flowable<List<SumMoneyBean>> getSumMoney_allAccount();
    /**
     * 获取本月支出和收入总数
     */
    Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney_Account(int accountId);

    /**
     * 获取本月支出和收入总数
     */
    Flowable<List<SumMoneyBean>> getMonthSumMoney_Account(int accountId, Date dateFrom, Date dateTo);

    /**
     * 获取某天的合计
     *
     * @param year  年
     * @param month 月
     * @param type  类型
     */
    Flowable<List<DaySumMoneyBean>> getDaySumMoney_Account(int accountId, int year, int month, int type);

    /**
     * 获取按类型汇总数据
     */
    Flowable<List<TypeSumMoneyBean>> getTypeSumMoney_Account(int accountId, Date from, Date to, int type);

//    Flowable<List<TypeSumMoneyBean>> getTypeSumMoney_Account(int accountId, int type);

    // lxy: 获取指定账户 from 到 to 的 type 类型（支出0 / 收入1）的 流水
    Flowable<List<RecordWithType>> getRangeRecordWithTypes_Account(int accountId, Date dateFrom, Date dateTo, int type);
    // lxy: 获取指定账户 from 到 to 的 总 入及出
    Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId, Date from, Date to);


    // lxy: 获取指定账户的所有记录
    Flowable<List<RecordWithType>> getRecordWithTypes_Account(int accountId);

    /**lxy
     * 获取某一指定账户的所有记录的总额
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId);

    /**
     * 小贴士
     */
    /**
     * 初始化Tip
     * @return
     */
    Completable initTips();


//    /**
//     * 获取贴士数量
//     * @return
//     */
//    int getTipCount();

    /**
     * 获取一条tip
     */
    Flowable<Tip> getOneTip(int id);


    /**
     * 成员
     */

    /**
     * 初始化默认的成员
     */
    Completable initMembers();

    /**
     * 获取成员
     *
     * @return 所有记账类型数据
     */
    Flowable<List<Member>> getAllMembers();

    Flowable<List<MemberSumMoneyBean>> getMemberSumMoney(Date from, Date to, int type);


    Flowable<List<TypeSumMoneyBean>> getTypeSumMoneyOne(Date from, Date to, int type);

    long getAccountCount();

    /**
     * 初始化默认的商家
     */
    Completable initStores();

    /**
     * 获取商家
     *
     * @return 所有记账类型数据
     */
    Flowable<List<Store>> getAllStore();


    /**
     * 初始化默认的项目
     */
    Completable initProjects();
    /**
     * 获取项目
     *
     * @return 所有记账类型数据
     */
    Flowable<List<Project>> getAllProject();


    Flowable<List<TypeSumMoneyBean>> getStoreSumMoney(Date dateFrom, Date dateTo, int type);
    Flowable<List<TypeSumMoneyBean>> getProjectSumMoney(Date dateFrom, Date dateTo, int type);

// lxy
    Flowable<List<RecordWithType>> getStoreRecordWithTypesSortMoney(Date from, Date to, int type, int typeId);
    Flowable<List<RecordWithType>> getProjectRecordWithTypesSortMoney(Date from, Date to, int type, int typeId);


}
