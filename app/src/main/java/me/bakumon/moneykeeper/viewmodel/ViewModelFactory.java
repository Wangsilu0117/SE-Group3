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

package me.bakumon.moneykeeper.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.ui.account.AccountViewModel;
import me.bakumon.moneykeeper.ui.accountbill.AccountBillViewModel;
import me.bakumon.moneykeeper.ui.accountbill.AccountMonthChartViewModel;
import me.bakumon.moneykeeper.ui.accountbillyear.AccountBillYearViewModel;
import me.bakumon.moneykeeper.ui.accountbillyear.AccountYearChartViewModel;
import me.bakumon.moneykeeper.ui.accountmanage.AccountManageViewModel;
import me.bakumon.moneykeeper.ui.accountwater.AccountWaterViewModel;
import me.bakumon.moneykeeper.ui.add.AddRecordViewModel;
import me.bakumon.moneykeeper.ui.addaccount.AddAccountViewModel;
import me.bakumon.moneykeeper.ui.addmember.AddMemberViewModel;
import me.bakumon.moneykeeper.ui.addtype.AddTypeViewModel;
import me.bakumon.moneykeeper.ui.home.HomeViewModel;
import me.bakumon.moneykeeper.ui.member.MemberViewModel;
import me.bakumon.moneykeeper.ui.membermanage.MemberManageViewModel;
import me.bakumon.moneykeeper.ui.projecttyperecords.ProjectTypeRecordsViewModel;
import me.bakumon.moneykeeper.ui.statistics.bill.BillViewModel;
import me.bakumon.moneykeeper.ui.statistics.project.ProjectViewModel;
import me.bakumon.moneykeeper.ui.statistics.reports.ReportsViewModel;
import me.bakumon.moneykeeper.ui.statistics.store.StoreViewModel;
import me.bakumon.moneykeeper.ui.storetyperecords.StoreTypeRecordsViewModel;
import me.bakumon.moneykeeper.ui.typemanage.TypeManageViewModel;
import me.bakumon.moneykeeper.ui.typerecords.TypeRecordsViewModel;
import me.bakumon.moneykeeper.ui.typesort.TypeSortViewModel;

/**
 * ViewModel 工厂
 *
 * @author Bakumon https://bakumon.me
 */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final AppDataSource mDataSource;

    public ViewModelFactory(AppDataSource dataSource) {
        mDataSource = dataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddRecordViewModel.class)) {
            return (T) new AddRecordViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(TypeManageViewModel.class)) {
            return (T) new TypeManageViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(TypeSortViewModel.class)) {
            return (T) new TypeSortViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(AddTypeViewModel.class)) {
            return (T) new AddTypeViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(BillViewModel.class)) {
            return (T) new BillViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(ReportsViewModel.class)) {
            return (T) new ReportsViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(TypeRecordsViewModel.class)) {
            return (T) new TypeRecordsViewModel(mDataSource);
        } else if(modelClass.isAssignableFrom(MemberViewModel.class)){
            return (T) new MemberViewModel(mDataSource);
        }else if (modelClass.isAssignableFrom(AccountManageViewModel.class)){
            return (T) new AccountManageViewModel(mDataSource);
        }else if (modelClass.isAssignableFrom(AddAccountViewModel.class)){
            return (T) new AddAccountViewModel(mDataSource);
        }

        else if (modelClass.isAssignableFrom(AccountViewModel.class)){
            return (T) new AccountViewModel(mDataSource);
        }
        else if (modelClass.isAssignableFrom(AccountWaterViewModel.class)){
            return (T) new AccountWaterViewModel(mDataSource);
        }else if (modelClass.isAssignableFrom(AccountBillViewModel.class)) {
            return (T) new AccountBillViewModel(mDataSource);
        }else if (modelClass.isAssignableFrom(AccountBillYearViewModel.class)) {
            return (T) new AccountBillYearViewModel(mDataSource);
        }

        else if (modelClass.isAssignableFrom(AccountMonthChartViewModel.class)) {
            return (T) new AccountMonthChartViewModel(mDataSource);
        }else if (modelClass.isAssignableFrom(AccountYearChartViewModel.class)) {
            return (T) new AccountYearChartViewModel(mDataSource);
        }else if (modelClass.isAssignableFrom(AddMemberViewModel.class)) {
            return (T) new AddMemberViewModel(mDataSource);
        }else if (modelClass.isAssignableFrom(MemberManageViewModel.class)) {
            return (T) new MemberManageViewModel(mDataSource);
        }

        else if (modelClass.isAssignableFrom(StoreViewModel.class)) {
            return (T) new StoreViewModel(mDataSource);
        }else if (modelClass.isAssignableFrom(ProjectViewModel.class)) {
            return (T) new ProjectViewModel(mDataSource);
        }

        else if (modelClass.isAssignableFrom(StoreTypeRecordsViewModel.class)) {
            return (T) new StoreTypeRecordsViewModel(mDataSource);
        }else if (modelClass.isAssignableFrom(ProjectTypeRecordsViewModel.class)) {
            return (T) new ProjectTypeRecordsViewModel(mDataSource);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
