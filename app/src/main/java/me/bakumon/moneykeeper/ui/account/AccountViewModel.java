package me.bakumon.moneykeeper.ui.account;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.datasource.AppDataSource;

public class AccountViewModel extends BaseViewModel {

    public AccountViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<SumMoneyBean>> getSumMoney_Account(int accountId) {
        return mDataSource.getSumMoney_Account(accountId);
    }

    public Flowable<List<SumMoneyBean>> getSumMoney_allAccount() {
        return mDataSource.getSumMoney_allAccount();
    }

    public Flowable<List<Account>> getAllAccounts() {
        return mDataSource.getAllAccounts();
    }

}
