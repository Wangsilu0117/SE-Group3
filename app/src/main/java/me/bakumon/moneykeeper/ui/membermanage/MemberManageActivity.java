package me.bakumon.moneykeeper.ui.membermanage;

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


import me.bakumon.moneykeeper.database.entity.Member;
import me.bakumon.moneykeeper.databinding.ActivityMemberManageBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

public class MemberManageActivity extends BaseActivity {
    private static final String TAG = MemberManageActivity.class.getSimpleName();

    private ActivityMemberManageBinding mBinding;
    private MemberManageViewModel mViewModel;
    private MemberManageAdapter mAdapter;
    private List<Member> mMembers;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_member_manage;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MemberManageViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_member_manage));


        mBinding.rvType.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MemberManageAdapter(null);
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
                Floo.navigation(this, Router.Url.URL_ADD_MEMBER)
                        .putExtra(Router.ExtraKey.KEY_MEMBER_BEAN, mAdapter.getItem(position))
                        .start());

    }

    private void showDeleteDialog(Member member) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.text_dialog_delete) + member.name)
                .setMessage(R.string.text_delete_member_note)
                .setNegativeButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_button_affirm_delete, (dialog, which) -> deleteMember(member))
                .create()
                .show();
    }
    private void deleteMember(Member member) {
        mDisposable.add(mViewModel.deleteMember(member).subscribeOn(Schedulers.io())
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

    public void addMember(View view) {
        Floo.navigation(this, Router.Url.URL_ADD_MEMBER)
                .start();
    }

    private void initData() {
        mDisposable.add(mViewModel.getAllMembers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((members) -> {
                            mMembers = members;
                            mAdapter.setNewData(mMembers);

                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_types_fail);
                            Log.e(TAG, "获取账成员数据失败", throwable);
                        }));
    }
}
