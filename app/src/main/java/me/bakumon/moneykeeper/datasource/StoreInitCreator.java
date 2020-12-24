package me.bakumon.moneykeeper.datasource;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.entity.Store;

public class StoreInitCreator {
    public static Store[] createStoreData() {
        List<Store> list = new ArrayList<>();
        Resources res = App.getINSTANCE().getResources();
        Store store;
        store = new Store(res.getString(R.string.text_shop_init),String.valueOf(R.drawable.ic_mar),1);
        list.add(store);
        store = new Store(res.getString(R.string.text_shop_restaurant), String.valueOf(R.drawable.ic_restaurant),2);
        list.add(store);
        store = new Store(res.getString(R.string.text_shop_bank),String.valueOf(R.drawable.ic_bank),3);
        list.add(store);
        store = new Store(res.getString(R.string.text_shop_market),String.valueOf(R.drawable.ic_market),4);
        list.add(store);
        store = new Store(res.getString(R.string.text_shop_supermarket),String.valueOf(R.drawable.ic_supermarket),5);
        list.add(store);
        store = new Store(res.getString(R.string.text_shop_other),String.valueOf(R.drawable.ic_others),6);
        list.add(store);
        return list.toArray(new Store[list.size()]);
    }
}