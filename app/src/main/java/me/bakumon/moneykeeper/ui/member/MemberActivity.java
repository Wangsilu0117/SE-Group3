package me.bakumon.moneykeeper.ui.member;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.MemberSumMoneyBean;
import me.bakumon.moneykeeper.databinding.ActivityStatisticsMemberBinding;
import me.bakumon.moneykeeper.ui.home.HomeActivity;
import me.bakumon.moneykeeper.ui.statistics.reports.ChooseMonthDialog;
import me.bakumon.moneykeeper.utill.BigDecimalUtil;
import me.bakumon.moneykeeper.utill.DateUtils;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

public class MemberActivity extends BaseActivity {

    private static final String TAG = MemberActivity.class.getSimpleName();
    MemberViewModel mViewModel;
    private ActivityStatisticsMemberBinding mBinding;
    public int mYear = DateUtils.getCurrentYear();
    public int mMonth = DateUtils.getCurrentMonth();
    private int mCurrentYear = DateUtils.getCurrentYear();
    private int mCurrentMonth = DateUtils.getCurrentMonth();
    private MemberAdapter mAdapter;

    private static final int MAX_ITEM_TIP = 5;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_statistics_member;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding=getDataBinding();
        mBinding.rvMember.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MemberAdapter(null);
        mBinding.rvMember.setAdapter(mAdapter);

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MemberViewModel.class);
        String title = DateUtils.getCurrentYearMonth();
        mBinding.statisticsMemberTitleBar.setTitle(title);
        mBinding.statisticsMemberTitleBar.ivTitle.setVisibility(View.VISIBLE);
        mBinding.statisticsMemberTitleBar.llTitle.setOnClickListener(v -> chooseMonth());
        mBinding.statisticsMemberTitleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.statisticsMemberTitleBar.llTitle.setOnClickListener(v -> chooseMonth());

        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_outlay) {
                getMemberSumMoneyOutlay();
            } else {
                getMemberSumMoneyIncome();
            }
        });
        mBinding.typeChoice.rgType.check(R.id.rb_outlay);

    }

    public void setYearMonth(int year, int month) {
        if (year == mYear && month == mMonth) {
            return;
        }
        mYear = year;
        mMonth = month;
        // 更新数据
        getMemberSumMoneyIncome();
        getMemberSumMoneyOutlay();
    }
    //弹框，选择日期时间
    private void chooseMonth() {
        mBinding.statisticsMemberTitleBar.llTitle.setEnabled(false);
        ChooseMonthDialog chooseMonthDialog = new ChooseMonthDialog(this, mCurrentYear, mCurrentMonth);
        chooseMonthDialog.setOnDismissListener(() -> mBinding.statisticsMemberTitleBar.llTitle.setEnabled(true));
        chooseMonthDialog.setOnChooseAffirmListener((year, month) -> {
            mCurrentYear = year;
            mCurrentMonth = month;
            String title = DateUtils.getYearMonthFormatString(year, month);
            mBinding.statisticsMemberTitleBar.setTitle(title);
            setYearMonth(year, month);
            setYearMonth(year, month);
        });
        chooseMonthDialog.show();
    }


    private void getMemberSumMoneyOutlay() {
        mDisposable.add(mViewModel.getMemberSumMoney(mYear, mMonth, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(memberSumMoneyBeansOutlay -> {
                            int i;
                            int maxSum = 0;
                            for(i = 0;i<memberSumMoneyBeansOutlay.size();i++){
                                if(maxSum<Integer.valueOf(BigDecimalUtil.fen2Yuan(memberSumMoneyBeansOutlay.get(i).memberSumMoney))){
                                    maxSum = Integer.valueOf(BigDecimalUtil.fen2Yuan(memberSumMoneyBeansOutlay.get(i).memberSumMoney));
                                }
                            }
                            setMaxData(maxSum);
                            System.out.println("maxsum:"+maxSum);
                            setListData(memberSumMoneyBeansOutlay);
                        },
                        throwable -> ToastUtils.show(R.string.toast_get_type_summary_fail)));
    }

    private void getMemberSumMoneyIncome() {
        mDisposable.add(mViewModel.getMemberSumMoney(mYear, mMonth, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(memberSumMoneyBeansIncome -> {
                            int i;
                            int maxSum = 0;
                            for(i = 0;i<memberSumMoneyBeansIncome.size();i++){
                                if(maxSum<Integer.valueOf(BigDecimalUtil.fen2Yuan(memberSumMoneyBeansIncome.get(i).memberSumMoney))){
                                    maxSum = Integer.valueOf(BigDecimalUtil.fen2Yuan(memberSumMoneyBeansIncome.get(i).memberSumMoney));
                                }
                            }
                            setMaxData(maxSum);
                            System.out.println("maxsum:"+maxSum);
                            setListData(memberSumMoneyBeansIncome);
                        },
                        throwable -> ToastUtils.show(R.string.toast_get_type_summary_fail)));
    }

    private void setListData(List<MemberSumMoneyBean> memberSumMoneyBeanList) {
        mAdapter.setNewData(memberSumMoneyBeanList);
        boolean isShowFooter = memberSumMoneyBeanList != null
                && memberSumMoneyBeanList.size() > MAX_ITEM_TIP;
        if (isShowFooter) {
            mAdapter.setFooterView(inflate(R.layout.layout_footer_tip));
        } else {
            mAdapter.removeAllFooterView();
        }
    }
    private void setMaxData(int maxData){
        mAdapter.SetMax(maxData);
    }

// 添加成员
    public void memberSettingClick(View view){
        Log.v(TAG,"跳1");
        Floo.navigation(this, Router.Url.URL_MEMBER_MANAGE)
                .start();
    }
}
