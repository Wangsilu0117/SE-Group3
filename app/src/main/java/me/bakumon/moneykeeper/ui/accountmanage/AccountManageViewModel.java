package me.bakumon.moneykeeper.ui.accountmanage;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.datasource.AppDataSource;

public class AccountManageViewModel extends BaseViewModel {
    public AccountManageViewModel(AppDataSource dataSource) {
        super(dataSource);
    }
    public Flowable<List<Account>> getAllAccounts() {
        return mDataSource.getAllAccounts();
    }

    public Completable deleteAccount(Account account) {
        return mDataSource.deleteAccount(account);
    }
}
