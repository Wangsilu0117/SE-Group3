package me.bakumon.moneykeeper.ui.account;


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
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.databinding.ActivityAccountBinding;
import me.bakumon.moneykeeper.utill.BigDecimalUtil;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

import static java.lang.Integer.parseInt;


public class AccountActivity extends BaseActivity {
    private static final String TAG = AccountActivity.class.getSimpleName();

    private AccountViewModel accountViewModel ;
    private ActivityAccountBinding mBinding;

    private AccountAdapter mAdapter;
    private List<Account> mAccounts;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        accountViewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountViewModel.class);

        initView();
        initData();

        getSum();

    }

    private void initView() {
        mBinding.accountTitleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.accountTitleBar.setTitle(getString(R.string.text_account));

        mBinding.rvType.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AccountAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) ->
                Floo.navigation(this, Router.Url.URL_ACCOUNT_WATER)
                        .putExtra(Router.ExtraKey.KEY_ACCOUNT_BEAN, mAdapter.getItem(position))
                        .start());

    }
    private void initData() {
        mDisposable.add(accountViewModel.getAllAccounts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((accounts) -> {
                            mAccounts = accounts;
                            mAdapter.setNewData(mAccounts);
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_types_fail);
                            Log.e(TAG, "获取账户数据失败", throwable);
                        }));

    }

    public void accountSettingClick(View view){
        Floo.navigation(this, Router.Url.URL_ACCOUNT_MANAGE)
                .start();
    }

    private void getSum() {
        mDisposable.add(accountViewModel.getSumMoney_allAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sumMoneyBean -> {
                            String outlay = "0",income = "0";
                            if (sumMoneyBean != null && sumMoneyBean.size() > 0) {
                                for (SumMoneyBean bean : sumMoneyBean) {
                                    if (bean.type == RecordType.TYPE_OUTLAY) {
                                        outlay = BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    } else if (bean.type == RecordType.TYPE_INCOME) {
                                        income = BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    }
                                }
                            }
                            int out = parseInt(outlay,10);
                            int in = parseInt(income,10);
                            int sum = in - out;
                            mBinding.accountNetAsset.setText(String.valueOf(sum));
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_month_summary_fail);
                        }));
    }

//    private void getAccountSum(int accountId){
//        mDisposable.add(accountViewModel.getSumMoney_Account(accountId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(sumMoneyBean -> {
//                            String outlay = "0",income = "0";
//                            if (sumMoneyBean != null && sumMoneyBean.size() > 0) {
//                                for (SumMoneyBean bean : sumMoneyBean) {
//                                    if (bean.type == RecordType.TYPE_OUTLAY) {
//                                        outlay = BigDecimalUtil.fen2Yuan(bean.sumMoney);
//                                    } else if (bean.type == RecordType.TYPE_INCOME) {
//                                        income = BigDecimalUtil.fen2Yuan(bean.sumMoney);
//                                    }
//                                }
//                            }
//                            int out = parseInt(outlay,10);
//                            int in = parseInt(income,10);
//                            int sum = in-out;
//
//
////                            setSum(String.valueOf(sum));
////                            if (accountId == 1){
////                                mBinding.cash.sum.setText(String.valueOf(sum));
////                            } else if (accountId == 2){
////                                mBinding.zfb.sum.setText(String.valueOf(sum));
////                            } else if (accountId == 3){
////                                mBinding.wx.sum.setText(String.valueOf(sum));
////                            }
//                        },
//                        throwable -> {
//                            ToastUtils.show(R.string.toast_get_month_summary_fail);
//                        }));
//
//    }






}