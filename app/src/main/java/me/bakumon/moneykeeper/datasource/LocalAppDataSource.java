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

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.ConfigManager;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.AppDatabase;
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
import me.bakumon.moneykeeper.utill.BackupUtil;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 数据源本地实现类
 *
 * @author Bakumon https://bakumon.me
 */
public class LocalAppDataSource implements AppDataSource {
    private final AppDatabase mAppDatabase;

    public LocalAppDataSource(AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
    }

    /**
     * 自动备份
     */
    private void autoBackup() throws Exception {
        if (ConfigManager.isAutoBackup()) {
            boolean isSuccess = BackupUtil.autoBackup();
            if (!isSuccess) {
                throw new BackupFailException();
            }
        }
    }

    /**
     * 自动备份
     */
    private void autoBackupForNecessary() throws Exception {
        if (ConfigManager.isAutoBackup()) {
            boolean isSuccess = BackupUtil.autoBackupForNecessary();
            if (!isSuccess) {
                throw new BackupFailException();
            }
        }
    }

    @Override
    public Flowable<List<RecordType>> getAllRecordTypes() {
        return mAppDatabase.recordTypeDao().getAllRecordTypes();
    }

    //新加8——————————————————————————————————————————
    @Override
    public Flowable<List<MainType>> getAllMainTypes() {
        return mAppDatabase.mainTypeDao().getAllMainTypes();
    }

    //新加9------------------------------------------------------------------------------------
    @Override
    public Flowable<List<RecordType>> getAllRecordTypesWithMain(int mainTypeId){
        return mAppDatabase.recordTypeDao().getAllRecordTypesWithMain(mainTypeId);
    }


    @Override
    public Completable initRecordTypes() {
        return Completable.fromAction(() -> {
            if (mAppDatabase.recordTypeDao().getRecordTypeCount() < 1) {
                // 没有记账类型数据记录，插入默认的数据类型
                mAppDatabase.recordTypeDao().insertRecordTypes(RecordTypeInitCreator.createRecordTypeData());
                autoBackupForNecessary();
            }
        });
    }

    //新加---------------------------------------------------
    @Override
    public Completable initMainTypes() {
        return Completable.fromAction(() -> {
            if (mAppDatabase.mainTypeDao().getMainTypeCount() < 1) {
                // 没有记账类型数据记录，插入默认的数据类型
                mAppDatabase.mainTypeDao().insertMainTypes(MainTypeInitCreator.createMainTypeData());
                autoBackupForNecessary();
            }
        });
    }



    @Override
    public Completable deleteRecord(Record record) {
        return Completable.fromAction(() -> {
            mAppDatabase.recordDao().deleteRecord(record);
            autoBackup();
        });
    }

    @Override
    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        Date dateFrom = DateUtils.getCurrentMonthStart();
        Date dateTo = DateUtils.getCurrentMonthEnd();
        return mAppDatabase.recordDao().getRangeRecordWithTypes(dateFrom, dateTo);
    }

    @Override
    public Flowable<List<RecordWithType>> getRecordWithTypes(Date dateFrom, Date dateTo, int type) {
        return mAppDatabase.recordDao().getRangeRecordWithTypes(dateFrom, dateTo, type);
    }

    @Override
    public Flowable<List<RecordWithType>> getRecordWithTypes(Date dateFrom, Date dateTo, int type, int typeId) {
        return mAppDatabase.recordDao().getRangeRecordWithTypes(dateFrom, dateTo, type, typeId);
    }

    @Override
    public Flowable<List<RecordWithType>> getRecordWithTypesSortMoney(Date dateFrom, Date dateTo, int type, int typeId) {
        return mAppDatabase.recordDao().getRecordWithTypesSortMoney(dateFrom, dateTo, type, typeId);
    }

    @Override
    public Completable insertRecord(Record record) {
        return Completable.fromAction(() -> {
            mAppDatabase.recordDao().insertRecord(record);
            autoBackup();
        });
    }

    @Override
    public Completable updateRecord(Record record) {
        return Completable.fromAction(() -> {
            mAppDatabase.recordDao().updateRecords(record);
            autoBackup();
        });
    }

    @Override
    public Completable sortRecordTypes(List<RecordType> recordTypes) {
        return Completable.fromAction(() -> {
            if (recordTypes != null && recordTypes.size() > 1) {
                List<RecordType> sortTypes = new ArrayList<>();
                for (int i = 0; i < recordTypes.size(); i++) {
                    RecordType type = recordTypes.get(i);
                    if (type.ranking != i) {
                        type.ranking = i;
                        sortTypes.add(type);
                    }
                }
                RecordType[] typeArray = new RecordType[sortTypes.size()];
                mAppDatabase.recordTypeDao().updateRecordTypes(sortTypes.toArray(typeArray));
                autoBackup();
            }
        });
    }

    @Override
    public Completable deleteRecordType(RecordType recordType) {
        return Completable.fromAction(() -> {
            if (mAppDatabase.recordDao().getRecordCountWithTypeId(recordType.id) > 0) {
                recordType.state = RecordType.STATE_DELETED;
                mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
            } else {
                mAppDatabase.recordTypeDao().deleteRecordType(recordType);
            }
            autoBackup();
        });
    }

    @Override
    public Flowable<List<RecordType>> getRecordTypes(int type) {
        return mAppDatabase.recordTypeDao().getRecordTypes(type);
    }

    @Override
    public Flowable<List<TypeImgBean>> getAllTypeImgBeans(int type) {
        return Flowable.create(e -> {
            List<TypeImgBean> beans = TypeImgListCreator.createTypeImgBeanData(type);
            e.onNext(beans);
            e.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable addRecordType(int type, String imgName, String name,int mainTypeId) {
        return Completable.fromAction(() -> {
            RecordType recordType = mAppDatabase.recordTypeDao().getTypeByName(type, name);
            if (recordType != null) {
                // name 类型存在
                if (recordType.state == RecordType.STATE_DELETED) {
                    // 已删除状态
                    recordType.state = RecordType.STATE_NORMAL;
                    recordType.ranking = System.currentTimeMillis();
                    recordType.imgName = imgName;
                    mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
                } else {
                    // 提示用户该类型已经存在
                    throw new IllegalStateException(name + App.getINSTANCE().getString(R.string.toast_type_is_exist));
                }
            } else {
                // 不存在，直接新增
                RecordType insertType = new RecordType(name, imgName, type, System.currentTimeMillis(), mainTypeId);
                mAppDatabase.recordTypeDao().insertRecordTypes(insertType);
            }
            autoBackup();
        });
    }

    @Override
    public Completable updateRecordType(RecordType oldRecordType, RecordType recordType) {
        return Completable.fromAction(() -> {
            String oldName = oldRecordType.name;
            String oldImgName = oldRecordType.imgName;
            if (!TextUtils.equals(oldName, recordType.name)) {
                RecordType recordTypeFromDb = mAppDatabase.recordTypeDao().getTypeByName(recordType.type, recordType.name);
                if (recordTypeFromDb != null) {
                    if (recordTypeFromDb.state == RecordType.STATE_DELETED) {

                        // 1。recordTypeFromDb 改成正常状态，name改成recordType#name，imageName同理
                        // 2。更新 recordTypeFromDb
                        // 3。判断是否有 oldRecordType 类型的 record 记录
                        // 4。如果有记录，把这些记录的 type_id 改成 recordTypeFromDb.id
                        // 5。删除 oldRecordType 记录

                        recordTypeFromDb.state = RecordType.STATE_NORMAL;
                        recordTypeFromDb.name = recordType.name;
                        recordTypeFromDb.imgName = recordType.imgName;
                        recordTypeFromDb.ranking = System.currentTimeMillis();

                        mAppDatabase.recordTypeDao().updateRecordTypes(recordTypeFromDb);

                        List<Record> recordsWithOldType = mAppDatabase.recordDao().getRecordsWithTypeId(oldRecordType.id);
                        if (recordsWithOldType != null && recordsWithOldType.size() > 0) {
                            for (Record record : recordsWithOldType) {
                                record.recordTypeId = recordTypeFromDb.id;
                            }
                            mAppDatabase.recordDao().updateRecords(recordsWithOldType.toArray(new Record[recordsWithOldType.size()]));
                        }

                        mAppDatabase.recordTypeDao().deleteRecordType(oldRecordType);
                    } else {
                        // 提示用户该类型已经存在
                        throw new IllegalStateException(recordType.name + App.getINSTANCE().getString(R.string.toast_type_is_exist));
                    }
                } else {
                    mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
                }
            } else if (!TextUtils.equals(oldImgName, recordType.imgName)) {
                mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
            }
            autoBackup();
        });
    }

    @Override
    public Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney() {
        Date dateFrom = DateUtils.getCurrentMonthStart();
        Date dateTo = DateUtils.getCurrentMonthEnd();
        return mAppDatabase.recordDao().getSumMoney(dateFrom, dateTo);
    }

    @Override
    public Flowable<List<SumMoneyBean>> getMonthSumMoney(Date dateFrom, Date dateTo) {
        return mAppDatabase.recordDao().getSumMoney(dateFrom, dateTo);
    }

    @Override
    public Flowable<List<DaySumMoneyBean>> getDaySumMoney(int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mAppDatabase.recordDao().getDaySumMoney(dateFrom, dateTo, type);
    }

    @Override
    public Flowable<List<TypeSumMoneyBean>> getTypeSumMoney(Date from, Date to, int type) {
        return mAppDatabase.recordDao().getTypeSumMoney(from, to, type);
    }

    /**
     * 账户
     */

    /**
     * 获取所有的账户类型
     * @return
     */
    @Override
    public Flowable<List<Account>> getAllAccounts() {
        return mAppDatabase.accountDao().getAllAccounts();
    }
    //新加10--------------------------------------------------------------------------------------
    @Override
    public Completable deleteAccount(Account account) {
        return Completable.fromAction(() -> {
            if (mAppDatabase.recordDao().getRecordCountWithAccountId(account.id) > 0) {
                account.state = Account.STATE_DELETED;
                mAppDatabase.accountDao().updateAccounts(account);
            } else {
                mAppDatabase.accountDao().deleteAccounts(account);
            }
            autoBackup();
        });
    }

    @Override
    public Completable updateAccount(Account oldAccount, Account account) {
        return Completable.fromAction(() -> {
            String oldName = oldAccount.name;
            if (!TextUtils.equals(oldName, account.name)) {
                Account accountFromDb = mAppDatabase.accountDao().getAccountByName(account.name);
                if (accountFromDb != null) {
                    if (account.state == Account.STATE_DELETED) {

                        // 1。recordTypeFromDb 改成正常状态，name改成recordType#name，imageName同理
                        // 2。更新 recordTypeFromDb
                        // 3。判断是否有 oldRecordType 类型的 record 记录
                        // 4。如果有记录，把这些记录的 type_id 改成 recordTypeFromDb.id
                        // 5。删除 oldRecordType 记录

                        accountFromDb.state = Account.STATE_NORMAL;
                        accountFromDb.name = account.name;
                        accountFromDb.rank = System.currentTimeMillis();

                        mAppDatabase.accountDao().updateAccounts(accountFromDb);

                        List<Record> recordsWithOldAccount = mAppDatabase.recordDao().getRecordsWithAccountId(oldAccount.id);
                        if (recordsWithOldAccount != null && recordsWithOldAccount.size() > 0) {
                            for (Record record : recordsWithOldAccount) {
                                record.accountId = accountFromDb.id;
                            }
                            mAppDatabase.recordDao().updateRecords(recordsWithOldAccount.toArray(new Record[recordsWithOldAccount.size()]));
                        }

                        mAppDatabase.accountDao().deleteAccounts(oldAccount);
                    } else {
                        // 提示用户该账户已经存在
                        throw new IllegalStateException(account.name + App.getINSTANCE().getString(R.string.toast_account_is_exist));
                    }
                } else {
                    mAppDatabase.accountDao().updateAccounts(account);
                }
            }
            autoBackup();
        });
    }

    @Override
    public Completable addMember(String name) {
        return Completable.fromAction(() -> {
            Member member = mAppDatabase.memberDao().getMemberByName(name);
            if (member != null) {
                // name 类型存在
                if (member.state == RecordType.STATE_DELETED) {
                    // 已删除状态
                    member.state = RecordType.STATE_NORMAL;
                    member.ranking = System.currentTimeMillis();
                    mAppDatabase.memberDao().updateMember(member);
                } else {
                    // 提示用户该类型已经存在
                    throw new IllegalStateException(name + App.getINSTANCE().getString(R.string.toast_member_is_exist));
                }
            } else {
                // 不存在，直接新增
                Member insertMember = new Member(name, System.currentTimeMillis());
                mAppDatabase.memberDao().insertMember(insertMember);
            }
            autoBackup();
        });
    }

    @Override
    public Completable deleteMember(Member member) {
        return Completable.fromAction(() -> {
            if (mAppDatabase.recordDao().getRecordCountWithMemberId(member.id) > 0) {
                member.state = Member.STATE_DELETED;
                mAppDatabase.memberDao().updateMember(member);
            } else {
                mAppDatabase.memberDao().deleteMember(member);
            }
            autoBackup();
        });

    }

    @Override
    public Completable updateMember(Member oldMember, Member member) {
        return null;
    }


    // lxy: 对应 APPDataSource 加上
    public Flowable<List<RecordWithType>> getRangeRecordWithTypes_Account(int accountId, Date dateFrom, Date dateTo, int type){
        return mAppDatabase.recordDao().getRangeRecordWithTypes_Account(accountId, dateFrom, dateTo, type);

    }
    // lxy: 对应 APPDataSource 加上
    public Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId, Date from, Date to){
        return mAppDatabase.recordDao().getSumMoney_Account(accountId, from, to);

    }
    // lxy: 对应 APPDataSource 加上     返回该账户的所有记录
    public Flowable<List<RecordWithType>> getRecordWithTypes_Account(int accountId){
        return mAppDatabase.recordDao().getRecordWithTypes_Account(accountId);
    }

    /**
     * 初始化所有的记账类型
     * @return
     */

    @Override
    public Completable initAccounts() {
        return Completable.fromAction(() -> {
            if (mAppDatabase.accountDao().getAccountCount() < 1){
                mAppDatabase.accountDao().insertAccounts(AccountInitCreator.createAccountData());
            }
        });
    }
//新加10-------------------------------------------------------------------------------------------------------------------
    @Override
    public Completable addAccount(String name) {
        return Completable.fromAction(() -> {
            Account account = mAppDatabase.accountDao().getAccountByName(name);
            if (account != null) {
                // name 类型存在
                if (account.state == RecordType.STATE_DELETED) {
                    // 已删除状态
                    account.state = RecordType.STATE_NORMAL;
                    account.rank = System.currentTimeMillis();
                    mAppDatabase.accountDao().updateAccounts(account);
                } else {
                    // 提示用户该类型已经存在
                    throw new IllegalStateException(name + App.getINSTANCE().getString(R.string.toast_account_is_exist));
                }
            } else {
                // 不存在，直接新增
                Account insertAccount = new Account(name, System.currentTimeMillis());
                mAppDatabase.accountDao().insertAccounts(insertAccount);
            }
            autoBackup();
        });
    }

    /**
     * 获取指定账户的本月的记账数据
     * @param accountId
     * @return
     */

    @Override
    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes_Account(int accountId) {
        Date dateFrom = DateUtils.getCurrentMonthStart();
        Date dateTo = DateUtils.getCurrentMonthEnd();
        return mAppDatabase.recordDao().getRangeRecordWithTypes_Account(accountId, dateFrom, dateTo);
    }

    @Override
    public Flowable<List<RecordWithType>> getRecordWithTypes_Account(int accountId, Date dateFrom, Date dateTo, int type) {
        return mAppDatabase.recordDao().getRangeRecordWithTypes_Account(accountId, dateFrom, dateTo, type);
    }

    @Override
    public Flowable<List<RecordWithType>> getRecordWithTypes_Account(int accountId, Date dateFrom, Date dateTo, int type, int typeId) {
        return mAppDatabase.recordDao().getRangeRecordWithTypes_Account(accountId, dateFrom, dateTo, type, typeId);
    }

    @Override
    public Flowable<List<RecordWithType>> getRecordWithTypesSortMoney_Account(int accountId, Date dateFrom, Date dateTo, int type, int typeId) {
        return mAppDatabase.recordDao().getRecordWithTypesSortMoney_Account(accountId, dateFrom, dateTo, type, typeId);
    }



    @Override
    public Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney_Account(int accountId) {
        Date dateFrom = DateUtils.getCurrentMonthStart();
        Date dateTo = DateUtils.getCurrentMonthEnd();
        return mAppDatabase.recordDao().getSumMoney_Account(accountId, dateFrom, dateTo);
    }

    @Override
    public Flowable<List<SumMoneyBean>> getMonthSumMoney_Account(int accountId, Date dateFrom, Date dateTo) {
        return mAppDatabase.recordDao().getSumMoney_Account(accountId, dateFrom, dateTo);
    }

    @Override
    public Flowable<List<DaySumMoneyBean>> getDaySumMoney_Account(int accountId, int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mAppDatabase.recordDao().getDaySumMoney_Account(accountId, dateFrom, dateTo, type);
    }

    // lxy
    @Override
    public Flowable<List<TypeSumMoneyBean>> getTypeSumMoney_Account(int accountId, Date from, Date to, int type) {
        return mAppDatabase.recordDao().getTypeSumMoney_Account(accountId, from, to, type);
    }
    // lxy
    @Override
    public Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId) {
        return mAppDatabase.recordDao().getSumMoney_Account(accountId);
    }
    // lxy:
    @Override
    public Flowable<List<SumMoneyBean>> getSumMoney_allAccount() {
        return mAppDatabase.recordDao().getSumMoney_allAccount();
    }
    /**
     * 小贴士
     */
    @Override
    public Completable initTips() {
        return Completable.fromAction(() -> {
            if (mAppDatabase.tipDao().getTipCount() < 1){
                mAppDatabase.tipDao().insertTips(TipsInitCreator.createTipData());
            }
        });
    }

//    @Override
//    public int getTipCount() {
//        return mAppDatabase.tipDao().getTipCount();
//    }


    @Override
    public Flowable<Tip> getOneTip(int id) {
        return mAppDatabase.tipDao().getOneTip(id);
    }


    /**
     * 成员
     */

    @Override
    public Flowable<List<Member>> getAllMembers() {
        return mAppDatabase.memberDao().getAllMembers();
    }

    @Override
    public  Flowable<List<MemberSumMoneyBean>> getMemberSumMoney(Date from, Date to, int type){
        return mAppDatabase.recordDao().getMemberSumMoney(from, to, type);
    }

    @Override
    public Completable initMembers() {
        return Completable.fromAction(() -> {
            if (mAppDatabase.memberDao().getMemberCount() < 1) {
                // 没有成员记录，插入默认的数据类型
                mAppDatabase.memberDao().insertMember(MemberInitCreator.createMemberData());
                autoBackupForNecessary();
            }
        });
    }



    @Override
    public Flowable<List<TypeSumMoneyBean>> getTypeSumMoneyOne(Date from, Date to, int type) {
        return mAppDatabase.recordDao().getTypeSumMoneyOne(from, to, type);
    }


    //lxy:
//    public Flowable<List<AccountSumMoneyBean>> getSumMoneyAccount(){
//        return mAppDatabase.recordDao().getSumMoneyAccount();
//    }

    public long getAccountCount(){
        return mAppDatabase.accountDao().getAccountCount();
    }

    //wsl:store
    /**
     * 商家
     */

    @Override
    public Flowable<List<Store>> getAllStore() {
        return mAppDatabase.storeDao().getAllStores();
    }

    @Override
    public Completable initStores() {
        return Completable.fromAction(() -> {
            if (mAppDatabase.storeDao().getStoreCount() < 1) {
                // 没有成员记录，插入默认的数据类型
                mAppDatabase.storeDao().insertStore(StoreInitCreator.createStoreData());
                autoBackupForNecessary();
            }
        });
    }

    /**
     * 项目
     */

    @Override
    public Flowable<List<Project>> getAllProject() {
        return mAppDatabase.projectDao().getAllProjects();
    }

    @Override
    public Completable initProjects() {
        return Completable.fromAction(() -> {
            if (mAppDatabase.projectDao().getProjectCount() < 1) {
                // 没有成员记录，插入默认的数据类型
                mAppDatabase.projectDao().insertProject(ProjectInitCreator.createProjectData());
                autoBackupForNecessary();
            }
        });
    }

    public Flowable<List<TypeSumMoneyBean>> getStoreSumMoney(Date dateFrom, Date dateTo, int type) {
        return mAppDatabase.recordDao().getStoreSumMoney(dateFrom, dateTo, type);
    }

    public Flowable<List<TypeSumMoneyBean>> getProjectSumMoney(Date dateFrom, Date dateTo, int type) {
        return mAppDatabase.recordDao().getProjectSumMoney(dateFrom, dateTo, type);
    }

    // lxy
    public Flowable<List<RecordWithType>> getStoreRecordWithTypesSortMoney(Date from, Date to, int type, int typeId){
        return mAppDatabase.recordDao().getStoreRecordWithTypesSortMoney(from, to, type, typeId);
    }

    public Flowable<List<RecordWithType>> getProjectRecordWithTypesSortMoney(Date from, Date to, int type, int typeId){
        return mAppDatabase.recordDao().getProjectRecordWithTypesSortMoney(from, to, type, typeId);
    }



}

