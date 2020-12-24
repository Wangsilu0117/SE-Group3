package me.bakumon.moneykeeper.ui.accountmanage;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.database.entity.Account;

public class AccountManageAdapter extends BaseDataBindingAdapter<Account> {
    public AccountManageAdapter(@Nullable List<Account> data) {
        super(R.layout.item_account_manage, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, Account item) {
        ViewDataBinding binding = helper.getBinding();

        binding.setVariable(me.bakumon.moneykeeper.BR.account, item);
        boolean isLastItem = helper.getAdapterPosition() == mData.size() - 1;
        // 单位是 dp
        binding.setVariable(BR.bottomMargin, isLastItem ? 100 : 0);

        binding.executePendingBindings();
    }

    // lxy: 发现下述无用
//    public void setNewData(List<Account> data,int type) {
//        if (data != null && data.size() > 0) {
//            List<Account> result = new ArrayList<>();
//            for (int i = 0; i < data.size(); i++) {
//                result.add(data.get(i));
//            }
//            super.setNewData(result);
//        } else {
//            super.setNewData(null);
//        }
//    }
}
