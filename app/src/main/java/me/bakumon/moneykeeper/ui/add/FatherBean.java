package me.bakumon.moneykeeper.ui.add;

import com.contrarywind.interfaces.IPickerViewData;

public class FatherBean implements IPickerViewData {

    private int id;
    private String name;

    public FatherBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
