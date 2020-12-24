package me.bakumon.moneykeeper.ui.accountbillyear;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.ui.statistics.ViewPagerAdapter;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 统计
 *
 * @author Bakumon https://bakumon
 */
public class AccountBillYearActivity extends BaseActivity {
    public static int account_id;
    private me.bakumon.moneykeeper.databinding.ActivityStatisticsAccountBinding mBinding;
    private AccountBillYearFragment mBillFragment;
    private AccountYearChartFragment mReportsFragment;
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

        String title = String.valueOf(DateUtils.getCurrentYear());    // 标题为年的选择
        mBinding.titleBar.setTitle(title);
        mBinding.titleBar.ivTitle.setVisibility(View.VISIBLE);
        mBinding.titleBar.llTitle.setOnClickListener(v -> chooseYear());
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.typeChoice.rbOutlay.setText(R.string.text_order);
        mBinding.typeChoice.rbIncome.setText(R.string.text_reports);

        setUpFragment();
    }

    private void setUpFragment() {
        ViewPagerAdapter infoPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mBillFragment = new AccountBillYearFragment();
        mReportsFragment = new AccountYearChartFragment();
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

    private void chooseYear() {
        mBinding.titleBar.llTitle.setEnabled(false);
        ChooseYearDialog chooseYearDialog = new ChooseYearDialog(this, mCurrentYear);
        chooseYearDialog.setOnDismissListener(() -> mBinding.titleBar.llTitle.setEnabled(true));
        chooseYearDialog.setOnChooseAffirmListener((year) -> {
            mCurrentYear = year;

            String title = String.valueOf(year);        // year转成int型
            mBinding.titleBar.setTitle(title);
            mBillFragment.setYear(year);
            mReportsFragment.setYear(year);
        });
        chooseYearDialog.show();
    }
}
