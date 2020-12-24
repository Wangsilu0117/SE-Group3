package me.bakumon.moneykeeper.ui.accountbill;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;
import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.utill.DateUtils;

public class AccountMonthChartViewModel extends BaseViewModel {
    public AccountMonthChartViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<SumMoneyBean>> getMonthSumMoney_Account(int accountId, int year, int month) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getMonthSumMoney_Account(accountId, dateFrom, dateTo);
    }

    public Flowable<List<TypeSumMoneyBean>> getTypeSumMoney_Account(int accountId, int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getTypeSumMoney_Account(accountId, dateFrom, dateTo, type);
    }

}