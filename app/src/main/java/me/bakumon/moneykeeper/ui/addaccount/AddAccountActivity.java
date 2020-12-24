package me.bakumon.moneykeeper.ui.addaccount;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.databinding.ActivityAddAccountBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

public class AddAccountActivity extends BaseActivity {
    private static final String TAG = AddAccountActivity.class.getSimpleName();
    private AddAccountViewModel mViewModel;
    private ActivityAddAccountBinding mBinding;
    private Account mAccount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_account;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddAccountViewModel.class);

        initView();

    }

    private void initView(){
        mAccount = (Account) getIntent().getSerializableExtra(Router.ExtraKey.KEY_ACCOUNT_BEAN);

        String prefix = mAccount == null ? getString(R.string.text_add) : getString(R.string.text_modify);

        mBinding.edtAccountName.setText(mAccount == null ? "" : mAccount.name);
        mBinding.edtAccountName.setSelection(mBinding.edtAccountName.getText().length());

        mBinding.titleBar.setTitle(prefix + "账户");
        mBinding.titleBar.tvRight.setText(R.string.text_save);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.tvRight.setOnClickListener(v -> saveAccount());

    }


    private void saveAccount() {
        mBinding.titleBar.tvRight.setEnabled(false);
        String text = mBinding.edtAccountName.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            Animation animation = AnimationUtils.loadAnimation(App.getINSTANCE(), R.anim.shake);
            mBinding.edtAccountName.startAnimation(animation);
            mBinding.titleBar.tvRight.setEnabled(true);
            return;
        }
        mDisposable.add(mViewModel.saveAccount(mAccount,text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish, throwable -> {
                    if (throwable instanceof BackupFailException) {
                        ToastUtils.show(throwable.getMessage());
                        Log.e(TAG, "备份失败（账户保存失败的时候）", throwable);
                        finish();
                    } else {
                        mBinding.titleBar.tvRight.setEnabled(true);
                        String failTip = TextUtils.isEmpty(throwable.getMessage()) ? getString(R.string.toast_account_save_fail) : throwable.getMessage();
                        ToastUtils.show(failTip);
                        Log.e(TAG, "类型保存失败", throwable);
                    }
                }));
    }
}
