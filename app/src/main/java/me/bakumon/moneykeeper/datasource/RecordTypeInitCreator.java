/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.bakumon.moneykeeper.datasource;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.entity.RecordType;


/**
 * 产生初始化的记录类型数据
 *
 * @author Bakumon https://bakumon.me
 */
public class RecordTypeInitCreator {

    public static RecordType[] createRecordTypeData() {

        List<RecordType> list = new ArrayList<>();

        Resources res = App.getINSTANCE().getResources();

        RecordType type;

        // 支出
        type = new RecordType(res.getString(R.string.text_type_eat), "type_eat", 0, 0,3);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_calendar), "type_calendar", 0, 1,2);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_hairdressing), "type_cigarette", 0, 2,1);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_clothes), "type_clothes", 0, 3,1);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_makeups), "type_sim", 0, 4,1);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_candy), "type_candy", 0, 5,3);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_skincare), "type_skincare", 0, 6,1);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_pet), "type_pet", 0, 7,2);
        list.add(type);

        // 收入
//        mainTypeId对应的是mainType的ranking
        type = new RecordType(res.getString(R.string.text_type_salary), "type_salary", 1, 0,4);
        list.add(type);

        type = new RecordType(res.getString(R.string.text_type_pluralism), "type_pluralism", 1, 1,4);
        list.add(type);

        //转账
        type = new RecordType(res.getString(R.string.text_type_transfer), "type_transfer", 2, 0,5);
        list.add(type);

        return list.toArray(new RecordType[list.size()]);
    }

}
