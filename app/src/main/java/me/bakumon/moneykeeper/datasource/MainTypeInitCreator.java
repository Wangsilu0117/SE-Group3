package me.bakumon.moneykeeper.datasource;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.entity.MainType;
import me.bakumon.moneykeeper.database.entity.Member;

public class MainTypeInitCreator {
    public static MainType[] createMainTypeData() {
        List<MainType> list = new ArrayList<>();
        Resources res = App.getINSTANCE().getResources();
        MainType main_type;
        main_type = new MainType(res.getString(R.string.text_maintype_beauty),"type_cigarette",0,1);
        list.add(main_type);
        main_type = new MainType(res.getString(R.string.text_maintype_dayly), "type_calendar", 0,2);
        list.add(main_type);
        main_type = new MainType(res.getString(R.string.text_maintype_diet), "type_eat", 0,3);
        list.add(main_type);
        main_type = new MainType(res.getString(R.string.text_maintype_salary),"type_salary",1,4);
        list.add(main_type);
        main_type = new MainType(res.getString(R.string.text_maintype_transfer),"type_transfer",2,5);
        list.add(main_type);
        return list.toArray(new MainType[list.size()]);
    }
}
