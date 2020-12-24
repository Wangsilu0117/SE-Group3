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

package me.bakumon.moneykeeper.ui.home;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.Tip;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 主页 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class HomeViewModel extends BaseViewModel {
    public HomeViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Completable initRecordTypes() {
        return mDataSource.initRecordTypes();
    }
    //新加----------------------------------------------------------------
    public Completable initMainTypes() {
        return mDataSource.initMainTypes();
    }

    public Completable initAccounts(){
        return mDataSource.initAccounts();
    }

    public Completable initMembers() {
        return mDataSource.initMembers();
    }
    //wsl新加-----------------------------------------------------------------
    public Completable initStores() {
        return mDataSource.initStores();
    }

    public Completable initProjects() {
        return mDataSource.initProjects();
    }


    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        return mDataSource.getCurrentMonthRecordWithTypes();
    }

    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }

    public Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney() {
        return mDataSource.getCurrentMonthSumMoney();
    }

    public Flowable<Tip> getOneTip(int i){
        return mDataSource.getOneTip(i);
    }

    public Completable initTips(){
        return mDataSource.initTips();
    }
}
