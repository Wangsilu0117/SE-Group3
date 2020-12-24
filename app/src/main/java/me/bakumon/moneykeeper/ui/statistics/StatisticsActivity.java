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

package me.bakumon.moneykeeper.ui.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.databinding.ActivityStatisticsBinding;
import me.bakumon.moneykeeper.ui.statistics.bill.BillFragment;
import me.bakumon.moneykeeper.ui.statistics.project.ProjectFragment;
import me.bakumon.moneykeeper.ui.statistics.reports.ChooseMonthDialog;
import me.bakumon.moneykeeper.ui.statistics.reports.ReportsFragment;
import me.bakumon.moneykeeper.ui.statistics.store.StoreFragment;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 统计
 *
 * @author Bakumon https://bakumon
 */
public class StatisticsActivity extends BaseActivity {
    private ActivityStatisticsBinding mBinding;
    //    private BillFragment mBillFragment;
    private StoreFragment mBillFragment;
    private ReportsFragment mReportsFragment;
    private ProjectFragment mProjectFragment;
    private int mCurrentYear = DateUtils.getCurrentYear();
    private int mCurrentMonth = DateUtils.getCurrentMonth();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_statistics;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();

        initView();
    }

    private void initView() {
        String title = DateUtils.getCurrentYearMonth();
        mBinding.titleBar.setTitle(title);
        mBinding.titleBar.ivTitle.setVisibility(View.VISIBLE);
        mBinding.titleBar.llTitle.setOnClickListener(v -> chooseMonth());
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.typeChoice.rbOutlay.setText("商家");
        mBinding.typeChoice.rbIncome.setText(R.string.text_reports);
        mBinding.typeChoice.rbTransfer.setText("项目");
        setUpFragment();
    }

    private void setUpFragment() {
        ViewPagerAdapter infoPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        mBillFragment = new BillFragment();
        mBillFragment = new StoreFragment();
        mReportsFragment = new ReportsFragment();
        mProjectFragment = new ProjectFragment();

        infoPagerAdapter.addFragment(mBillFragment);
        infoPagerAdapter.addFragment(mReportsFragment);
        infoPagerAdapter.addFragment(mProjectFragment);
        mBinding.viewPager.setAdapter(infoPagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(3);

        //点击了哪个就展示哪个
        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_outlay) {
                mBinding.viewPager.setCurrentItem(0, false);
            } else if(checkedId == R.id.rb_income){
                mBinding.viewPager.setCurrentItem(1, false);
            }else{
                mBinding.viewPager.setCurrentItem(2, false);
            }
        });
        mBinding.typeChoice.rgType.check(R.id.rb_outlay);
    }

    //弹框，选择日期时间
    private void chooseMonth() {
        mBinding.titleBar.llTitle.setEnabled(false);
        ChooseMonthDialog chooseMonthDialog = new ChooseMonthDialog(this, mCurrentYear, mCurrentMonth);
        chooseMonthDialog.setOnDismissListener(() -> mBinding.titleBar.llTitle.setEnabled(true));
        chooseMonthDialog.setOnChooseAffirmListener((year, month) -> {
            mCurrentYear = year;
            mCurrentMonth = month;
            String title = DateUtils.getYearMonthFormatString(year, month);
            mBinding.titleBar.setTitle(title);
            mBillFragment.setYearMonth(year, month);
            mReportsFragment.setYearMonth(year, month);
            mProjectFragment.setYearMonth(year, month);
        });
        chooseMonthDialog.show();
    }
}
