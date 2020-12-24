package me.bakumon.moneykeeper.ui.addaccount;

import io.reactivex.Completable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.datasource.AppDataSource;

public class AddAccountViewModel extends BaseViewModel {
    public AddAccountViewModel(AppDataSource dataSource) {
        super(dataSource);
    }
    public Completable saveAccount(Account account, String name) {
        if (account == null) {
            // 添加
            return mDataSource.addAccount(name);
        } else {
            // 修改
            Account updateAccount = new Account(account.id, name, account.rank);
            updateAccount.state = account.state;
            return mDataSource.updateAccount(account, updateAccount);
        }
    }
}
