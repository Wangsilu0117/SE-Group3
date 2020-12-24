package me.bakumon.moneykeeper.ui.accountbillyear;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;
import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.utill.DateUtils;

public class AccountYearChartViewModel extends BaseViewModel {
    public AccountYearChartViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    // lxy: 获取 该年 总 收入 总 流出
    public Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId, int year) {
        Date dateFrom = DateUtils.getMonthStart(year, 1);
        Date dateTo = DateUtils.getMonthEnd(year, 12);
        return mDataSource.getSumMoney_Account(accountId, dateFrom, dateTo);
    }

    public Flowable<List<TypeSumMoneyBean>> getTypeSumMoney_Account(int accountId, int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, 1);
        Date dateTo = DateUtils.getMonthEnd(year, 12);
        return mDataSource.getTypeSumMoney_Account(accountId, dateFrom, dateTo, type);
    }
}
