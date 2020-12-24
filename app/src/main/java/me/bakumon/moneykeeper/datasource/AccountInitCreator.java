package me.bakumon.moneykeeper.datasource;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.entity.Account;
public class AccountInitCreator {

    public static Account[] createAccountData() {

        List<Account> list = new ArrayList<>();

        Resources res = App.getINSTANCE().getResources();

        Account account;

        // 支出
        account = new Account(res.getString(R.string.text_account_cash),0);
        list.add(account);

        account = new Account(res.getString(R.string.text_account_zhifubao),1);
        list.add(account);

        account = new Account(res.getString(R.string.text_account_weixin),2);
        list.add(account);


        return list.toArray(new Account[list.size()]);
    }

}
