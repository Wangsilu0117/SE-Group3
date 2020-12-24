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

package me.bakumon.moneykeeper.ui.accountbill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.ui.statistics.ViewPagerAdapter;
import me.bakumon.moneykeeper.ui.statistics.reports.ChooseMonthDialog;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 统计
 *
 * @author Bakumon https://bakumon
 */
public class AccountBillActivity extends BaseActivity {
    private static final String TAG = AccountBillActivity.class.getSimpleName();
    public static int account_id;
    private me.bakumon.moneykeeper.databinding.ActivityStatisticsAccountBinding mBinding;
    private AccountBillFragment mBillFragment;
    private AccountMonthChartFragment mReportsFragment;
    private int mCurrentYear = DateUtils.getCurrentYear();
    private int mCurrentMonth = DateUtils.getCurrentMonth();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_statistics_account;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();

        initView();
    }

    private void initView() {

        account_id = (int) getIntent().getSerializableExtra(Router.ExtraKey.KEY_ACCOUNT_ID);

        Log.v(TAG, "跳：成功进入第"+ account_id +"个账户的主流水页面");

        String title = DateUtils.getCurrentYearMonth();
        mBinding.titleBar.setTitle(title);
        mBinding.titleBar.ivTitle.setVisibility(View.VISIBLE);
        mBinding.titleBar.llTitle.setOnClickListener(v -> chooseMonth());
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.typeChoice.rbOutlay.setText(R.string.text_order);
        mBinding.typeChoice.rbIncome.setText(R.string.text_reports);

        setUpFragment();
    }

    private void setUpFragment() {
        ViewPagerAdapter infoPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mBillFragment = new AccountBillFragment();
        mReportsFragment = new AccountMonthChartFragment();
        infoPagerAdapter.addFragment(mBillFragment);
        infoPagerAdapter.addFragment(mReportsFragment);

        mBinding.viewPager.setAdapter(infoPagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(2);

        //点击了哪个就展示哪个
        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_outlay) {
                mBinding.viewPager.setCurrentItem(0, false);
            } else {
                mBinding.viewPager.setCurrentItem(1, false);
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
        });
        chooseMonthDialog.show();
    }
}
