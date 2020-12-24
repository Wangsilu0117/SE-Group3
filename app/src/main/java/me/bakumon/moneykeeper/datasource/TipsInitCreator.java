package me.bakumon.moneykeeper.datasource;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.entity.Tip;

public class TipsInitCreator {
    public static Tip[] createTipData() {

        List<Tip> list = new ArrayList<>();

        Resources res = App.getINSTANCE().getResources();

        Tip tip;

        tip = new Tip(res.getString(R.string.text_tip_1));
        list.add(tip);
        tip = new Tip(res.getString(R.string.text_tip_2));
        list.add(tip);
        tip = new Tip(res.getString(R.string.text_tip_3));
        list.add(tip);
        tip = new Tip(res.getString(R.string.text_tip_4));
        list.add(tip);
        tip = new Tip(res.getString(R.string.text_tip_5));
        list.add(tip);
        tip = new Tip(res.getString(R.string.text_tip_6));
        list.add(tip);
        tip = new Tip(res.getString(R.string.text_tip_7));
        list.add(tip);




        return list.toArray(new Tip[list.size()]);
    }
}
