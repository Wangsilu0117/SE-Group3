package me.bakumon.moneykeeper.ui.accountwater;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.datasource.AppDataSource;


// 基于 LocalAPPDataSource中的函数
public class AccountWaterViewModel extends BaseViewModel {
    public AccountWaterViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Completable initRecordTypes() {
        return mDataSource.initRecordTypes();
    }

    public Completable initAccounts(){
        return mDataSource.initAccounts();
    }

//    public Completable initMembers() {
//        return mDataSource.initMembers();
//    }

    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        return mDataSource.getCurrentMonthRecordWithTypes();
    }

    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }

    public Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney() {
        return mDataSource.getCurrentMonthSumMoney();
    }

    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes_Account(int accountId){
        return mDataSource.getCurrentMonthRecordWithTypes_Account(accountId);
    }

    public Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney_Account(int accountId){
        return mDataSource.getCurrentMonthSumMoney_Account(accountId);
    }

    // lxy新加：获取该账户所有记录
    public Flowable<List<RecordWithType>> getRecordWithTypes_Account(int accountId){
        return mDataSource.getRecordWithTypes_Account(accountId);
    }
    // lxy新加：获取总支出总收入
    public Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId){
        return mDataSource.getSumMoney_Account(accountId);
    }

}
