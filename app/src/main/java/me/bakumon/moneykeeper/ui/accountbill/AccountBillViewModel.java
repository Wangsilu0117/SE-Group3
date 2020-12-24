package me.bakumon.moneykeeper.ui.accountbill;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.DaySumMoneyBean;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.utill.DateUtils;

public class AccountBillViewModel extends BaseViewModel {

    public AccountBillViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    // 对BillViewModel中所有函数均加上 账户 属性
    public Flowable<List<RecordWithType>> getRecordWithTypes_Account(int accountId, int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getRecordWithTypes_Account(accountId, dateFrom, dateTo, type);
    }

    public Flowable<List<DaySumMoneyBean>> getDaySumMoney_Account(int accountId, int year, int month, int type) {
        return mDataSource.getDaySumMoney_Account(accountId, year, month, type);
    }


    public Flowable<List<SumMoneyBean>> getMonthSumMoney_Account(int accountId, int year, int month) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getMonthSumMoney_Account(accountId, dateFrom, dateTo);
    }

    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }

    // 对于目前所需的当月 数据 的 函数 均已加上账户属性，   未加获得 年 的 函数（打算：dao中加上这个按年返回总值）
}
