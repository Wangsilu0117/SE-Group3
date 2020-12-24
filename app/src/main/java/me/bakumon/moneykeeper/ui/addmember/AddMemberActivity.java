package me.bakumon.moneykeeper.ui.addmember;

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
import me.bakumon.moneykeeper.database.entity.Member;
import me.bakumon.moneykeeper.databinding.ActivityAddAccountBinding;
import me.bakumon.moneykeeper.databinding.ActivityAddMemberBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.ui.addaccount.AddAccountViewModel;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

public class AddMemberActivity extends BaseActivity {
    private static final String TAG = AddMemberActivity.class.getSimpleName();
    private AddMemberViewModel mViewModel;
    private ActivityAddMemberBinding mBinding;
    private Member mMember;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_member;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddMemberViewModel.class);

        initView();

    }

    private void initView(){
        mMember = (Member) getIntent().getSerializableExtra(Router.ExtraKey.KEY_MEMBER_BEAN);

        String prefix = mMember == null ? getString(R.string.text_add) : getString(R.string.text_modify);

        mBinding.edtMemberName.setText(mMember == null ? "" : mMember.name);
        mBinding.edtMemberName.setSelection(mBinding.edtMemberName.getText().length());

        mBinding.titleBar.setTitle(prefix + "成员");
        mBinding.titleBar.tvRight.setText(R.string.text_save);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.tvRight.setOnClickListener(v -> saveMember());

    }


    private void saveMember() {
        mBinding.titleBar.tvRight.setEnabled(false);
        String text = mBinding.edtMemberName.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            Animation animation = AnimationUtils.loadAnimation(App.getINSTANCE(), R.anim.shake);
            mBinding.edtMemberName.startAnimation(animation);
            mBinding.titleBar.tvRight.setEnabled(true);
            return;
        }
        mDisposable.add(mViewModel.saveMember(mMember,text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish, throwable -> {
                    if (throwable instanceof BackupFailException) {
                        ToastUtils.show(throwable.getMessage());
                        Log.e(TAG, "备份失败（账户保存失败的时候）", throwable);
                        finish();
                    } else {
                        mBinding.titleBar.tvRight.setEnabled(true);
                        String failTip = TextUtils.isEmpty(throwable.getMessage()) ? getString(R.string.toast_member_save_fail) : throwable.getMessage();
                        ToastUtils.show(failTip);
                        Log.e(TAG, "类型保存失败", throwable);
                    }
                }));
    }
}
