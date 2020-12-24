package me.bakumon.moneykeeper.ui.accountmanage;

import android.app.AlertDialog;
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
import me.bakumon.moneykeeper.databinding.ActivityAccountManageBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

public class AccountManageActivity extends BaseActivity {
    private static final String TAG = AccountManageActivity.class.getSimpleName();

    private ActivityAccountManageBinding mBinding;
    private AccountManageViewModel mViewModel;
    private AccountManageAdapter mAdapter;
    private List<Account> mAccounts;

//    private int mCurrentAccount;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_manage;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountManageViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_account_manage));


        mBinding.rvType.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AccountManageAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            if (adapter.getData().size() > 1) {
                showDeleteDialog(mAdapter.getData().get(position));
            } else {
                ToastUtils.show(R.string.toast_least_one_type);
            }
            return true;
        });

        mAdapter.setOnItemClickListener((adapter, view, position) ->
                Floo.navigation(this, Router.Url.URL_ADD_ACCOUNT)
                        .putExtra(Router.ExtraKey.KEY_ACCOUNT_BEAN, mAdapter.getItem(position))
//                        .putExtra(Router.ExtraKey.KEY_TYPE, mCurrentType)
                        .start());

//        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
//            mCurrentType = checkedId == R.id.rb_outlay ? RecordType.TYPE_OUTLAY : RecordType.TYPE_INCOME;
//            mAdapter.setNewData(mRecordTypes, mCurrentType);
//            int visibility = mAdapter.getData().size() > 1 ? View.VISIBLE : View.INVISIBLE;
//            mBinding.titleBar.tvRight.setVisibility(visibility);
//        });

    }
    private void showDeleteDialog(Account account) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.text_dialog_delete) + account.name)
                .setMessage(R.string.text_delete_account_note)
                .setNegativeButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_button_affirm_delete, (dialog, which) -> deleteAccount(account))
                .create()
                .show();
    }
    private void deleteAccount(Account account) {
        mDisposable.add(mViewModel.deleteAccount(account).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        },
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（类型删除失败的时候）", throwable);
                            } else {
                                ToastUtils.show(R.string.toast_delete_fail);
                                Log.e(TAG, "类型删除失败", throwable);
                            }
                        }
                ));
    }
    public void addAccount(View view) {
        Floo.navigation(this, Router.Url.URL_ADD_ACCOUNT)
//                .putExtra(Router.ExtraKey.KEY_TYPE, mCurrentType)
                .start();
    }

    private void initData() {
        mDisposable.add(mViewModel.getAllAccounts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((accounts) -> {
                            mAccounts = accounts;
                            mAdapter.setNewData(mAccounts);
//                            int id = mCurrentType == RecordType.TYPE_OUTLAY ? R.id.rb_outlay : R.id.rb_income;
//                            mBinding.typeChoice.rgType.clearCheck();
//                            mBinding.typeChoice.rgType.check(id);
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_types_fail);
                            Log.e(TAG, "获取账户数据失败", throwable);
                        }));
    }

}
