package me.bakumon.moneykeeper.ui.storetyperecords;


import android.os.Bundle;
import android.support.annotation.Nullable;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.databinding.ActivityStatisticsBinding;
import me.bakumon.moneykeeper.databinding.ActivityTyperecordsBinding;
import me.bakumon.moneykeeper.ui.statistics.ViewPagerAdapter;
import me.bakumon.moneykeeper.ui.storetyperecords.StoreTypeRecordsFragment;
import me.bakumon.moneykeeper.ui.typerecords.TypeRecordsFragment;

/**
 * 某一类型的记账记录
 *
 * @author Bakumon https://bakumon
 */
public class StoreTypeRecordsActivity extends BaseActivity {

    private ActivityTyperecordsBinding mBinding;

    private int mRecordType;
    private int mRecordTypeId;
    private int mYear;
    private int mMonth;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_typerecords;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();

        initView();
    }

    private void initView() {
        if (getIntent() != null) {
            mBinding.titleBar.setTitle(getIntent().getStringExtra(Router.ExtraKey.KEY_TYPE_NAME));
            mRecordType = getIntent().getIntExtra(Router.ExtraKey.KEY_RECORD_TYPE, 0);
            mRecordTypeId = getIntent().getIntExtra(Router.ExtraKey.KEY_RECORD_TYPE_ID, 0);
            mYear = getIntent().getIntExtra(Router.ExtraKey.KEY_YEAR, 0);
            mMonth = getIntent().getIntExtra(Router.ExtraKey.KEY_MONTH, 0);
        }

        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.typeChoice.rbOutlay.setText(R.string.text_sort_time);
        mBinding.typeChoice.rbIncome.setText(R.string.text_sort_money);

        setUpFragment();
    }

    private void setUpFragment() {
        ViewPagerAdapter infoPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        StoreTypeRecordsFragment timeSortFragment = StoreTypeRecordsFragment.newInstance(StoreTypeRecordsFragment.SORT_TIME, mRecordType, mRecordTypeId, mYear, mMonth);
        StoreTypeRecordsFragment moneySortFragment = StoreTypeRecordsFragment.newInstance(StoreTypeRecordsFragment.SORT_MONEY, mRecordType, mRecordTypeId, mYear, mMonth);
        infoPagerAdapter.addFragment(timeSortFragment);
        infoPagerAdapter.addFragment(moneySortFragment);
        mBinding.viewPager.setAdapter(infoPagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(2);

        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_outlay) {
                mBinding.viewPager.setCurrentItem(0, false);
            } else {
                mBinding.viewPager.setCurrentItem(1, false);
            }
        });
        mBinding.typeChoice.rgType.check(R.id.rb_outlay);
    }
}
