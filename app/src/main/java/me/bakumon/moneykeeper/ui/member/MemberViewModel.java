package me.bakumon.moneykeeper.ui.member;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.MemberSumMoneyBean;
import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.utill.DateUtils;

public class MemberViewModel extends BaseViewModel {

    public MemberViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<MemberSumMoneyBean>> getMemberSumMoney(int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getMemberSumMoney(dateFrom, dateTo, type);
    }

}
