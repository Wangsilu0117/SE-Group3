package me.bakumon.moneykeeper.ui.account;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.database.entity.Account;


public class AccountAdapter extends BaseDataBindingAdapter<Account> {

    public AccountAdapter(@Nullable List<Account> data) {
        super(R.layout.account_main_item, data);
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


}
