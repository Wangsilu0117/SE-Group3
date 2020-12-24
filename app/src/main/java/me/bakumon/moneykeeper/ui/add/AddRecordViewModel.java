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

package me.bakumon.moneykeeper.ui.add;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.database.entity.MainType;
import me.bakumon.moneykeeper.database.entity.Member;
import me.bakumon.moneykeeper.database.entity.Project;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.Store;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 记一笔界面 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class AddRecordViewModel extends BaseViewModel {
    public AddRecordViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordType>> getAllRecordTypes() {
        return mDataSource.getAllRecordTypes();
    }
    //新加9--------------------------------------------------------------------
    public Flowable<List<RecordType>> getAllRecordTypesWithMain(int mainTypeId){
        if(mainTypeId == -1){
            //wsl:设置?
            return mDataSource.getAllRecordTypes();
        }else{
//            wsl：返回对应的二级分类名称
            return mDataSource.getAllRecordTypesWithMain(mainTypeId);
        }
    }

    public Flowable<List<Account>> getAllAccounts(){
        return mDataSource.getAllAccounts();
    }

    public Flowable<List<Member>> getAllMembers() {
        return mDataSource.getAllMembers();
    }



    public Completable insertRecord(Record record) {
        return mDataSource.insertRecord(record);
    }

    public Completable updateRecord(Record record) {
        return mDataSource.updateRecord(record);
    }

    //新加8--------------------------------------------------------------------------------
    public  Flowable<List<MainType>> getAllMainTypes() {
        return mDataSource.getAllMainTypes();
    }

    //wsl 新加--------------------------------------------------------------------------------
    public Flowable<List<Store>> getAllStores() {
        return mDataSource.getAllStore();
    }

    //wsl 新加--------------------------------------------------------------------------------
    public Flowable<List<Project>> getAllProjects() {
        return mDataSource.getAllProject();
    }
}
