package me.bakumon.moneykeeper.ui.accountbillyear;

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

public class AccountBillYearViewModel extends BaseViewModel {

    public AccountBillYearViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    // 对BillViewModel中所有函数均加上 账户 属性
    // lxy : 获取该年 该账户所有流水
    public Flowable<List<RecordWithType>> getRangeRecordWithTypes_Account(int accountId, int year, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, 1);
        Date dateTo = DateUtils.getMonthEnd(year, 12);
        return mDataSource.getRangeRecordWithTypes_Account(accountId, dateFrom, dateTo, type);
    }

    // lxy: 获取 该年 总 收入 总 流出
    public Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId, int year) {
        Date dateFrom = DateUtils.getMonthStart(year, 1);
        Date dateTo = DateUtils.getMonthEnd(year, 12);
        return mDataSource.getSumMoney_Account(accountId, dateFrom, dateTo);
    }

    /// 获取 某年某月 的 总 收入 总 流出
    public Flowable<List<SumMoneyBean>> getMonthSumMoney_Account(int accountId, int year, int month) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getMonthSumMoney_Account(accountId, dateFrom, dateTo);
    }



    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }


    /////////////// 要改的，年的统计界面的柱状图，应该以月为计。。。。。。。而不是天！！！！！！！！
    public Flowable<List<DaySumMoneyBean>> getDaySumMoney_Account(int accountId, int year, int month, int type) {
        return mDataSource.getDaySumMoney_Account(accountId, year, month, type);
    }

}
