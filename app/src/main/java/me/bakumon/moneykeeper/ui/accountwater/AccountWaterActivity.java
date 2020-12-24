package me.bakumon.moneykeeper.ui.accountwater;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.databinding.ActivityAccountWaterBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.ui.home.HomeAdapter;
import me.bakumon.moneykeeper.utill.BigDecimalUtil;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

import static java.lang.Integer.parseInt;

public class AccountWaterActivity extends BaseActivity {
    private static final String TAG = AccountWaterActivity.class.getSimpleName();
    private static final int MAX_ITEM_TIP = 5;
    private ActivityAccountWaterBinding mBinding;

    private AccountWaterViewModel mViewModel;
    private HomeAdapter mAdapter;

    private Account mAccount;

    @Override
    protected int getLayoutId() {
        Log.v(TAG, "成功开启账户界面");
        return R.layout.activity_account_water;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountWaterViewModel.class);

        initView();
        initData();

    }

    private void initView() {

        mAccount = (Account) getIntent().getSerializableExtra(Router.ExtraKey.KEY_ACCOUNT_BEAN);

        mBinding.accountTitleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.accountTitleBar.setTitle(mAccount.name);

        mBinding.rvHome.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeAdapter(null);
        mBinding.rvHome.setAdapter(mAdapter);

        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            showOperateDialog(mAdapter.getData().get(position));
            return false;
        });
    }

    private void showOperateDialog(RecordWithType record) {
        new AlertDialog.Builder(this)
                .setItems(new String[]{getString(R.string.text_modify), getString(R.string.text_delete)}, (dialog, which) -> {
                    if (which == 0) {
                        modifyRecord(record);
                    } else {
                        deleteRecord(record);
                    }
                })
                .create()
                .show();
    }

    private void modifyRecord(RecordWithType record) {
        Floo.navigation(this, Router.Url.URL_ADD_RECORD)
                .putExtra(Router.ExtraKey.KEY_RECORD_BEAN, record)
                .start();
    }

    private void deleteRecord(RecordWithType record) {
        mDisposable.add(mViewModel.deleteRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        },
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（删除记账记录失败的时候）", throwable);
                            } else {
                                ToastUtils.show(R.string.toast_record_delete_fail);
                                Log.e(TAG, "删除记账记录失败", throwable);
                            }
                        }));
    }

    private void initData() {
        initRecordTypes();
        getCurrentMonthRecords();     // 流水区域
        getCurrentMontySumMonty();
    }


    // 账户1现金：当前月份的流水
    private void getCurrentMonthRecords() {
        mDisposable.add(mViewModel.getRecordWithTypes_Account(mAccount.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordWithTypes -> {
                            setListData(recordWithTypes);                // 调用函数，匹配项和数据
                            if (recordWithTypes == null || recordWithTypes.size() < 1) {
                                setEmptyView();
                            }
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_records_fail);
                            Log.e(TAG, "获取记录列表失败", throwable);
                        }));
    }

    private void setListData(List<RecordWithType> recordWithTypes) {
        mAdapter.setNewData(recordWithTypes);
        boolean isShowFooter = recordWithTypes != null
                && recordWithTypes.size() > MAX_ITEM_TIP;
        if (isShowFooter) {
            mAdapter.setFooterView(inflate(R.layout.layout_footer_tip));
        } else {
            mAdapter.removeAllFooterView();
        }
    }

    private void setEmptyView() {
        mAdapter.setEmptyView(inflate(R.layout.layout_home_empty));
    }


    private void initRecordTypes() {
        mDisposable.add(mViewModel.initRecordTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        },
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（初始化类型数据失败的时候）", throwable);
                            } else {
                                ToastUtils.show(R.string.toast_init_types_fail);
                                Log.e(TAG, "初始化类型数据失败", throwable);
                            }
                        }));
    }

    // 获取账户1当月的收支总数
    private void getCurrentMontySumMonty() {
        mDisposable.add(mViewModel.getSumMoney_Account(mAccount.id)          // lxy : 改成总支出总收入
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sumMoneyBean -> {
                            String outlay = "0";
                            String inCome = "0";
                            if (sumMoneyBean != null && sumMoneyBean.size() > 0) {
                                for (SumMoneyBean bean : sumMoneyBean) {
                                    if (bean.type == RecordType.TYPE_OUTLAY) {
                                        outlay = BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    } else if (bean.type == RecordType.TYPE_INCOME) {
                                        inCome = BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    }
                                }
                            }
                            mBinding.tvMonthOutlay.setText(outlay);
                            mBinding.tvMonthIncome.setText(inCome);

                            int out = parseInt(outlay,10);
                            int in = parseInt(inCome,10);
                            int sum = in - out;
                            mBinding.accountNetAsset.setText(String.valueOf(sum));      // 净资产赋值
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_current_sum_money_fail);
                            Log.e(TAG, "本月支出收入总数获取失败", throwable);
                        }));
    }


    // 下面要执行：底部菜单栏的 跳转，按月查看？ 按年查看？
    public void accountBillClick(View view) {
        Floo.navigation(this, Router.Url.URL_ACCOUNT_BILL)
                .putExtra(Router.ExtraKey.KEY_ACCOUNT_ID, mAccount.id)
                .start();
    }
    public void accountBillYearClick(View view) {
        Floo.navigation(this, Router.Url.URL_ACCOUNT_BILL_YEAR)
                .putExtra(Router.ExtraKey.KEY_ACCOUNT_ID, mAccount.id)
                .start();
    }
}