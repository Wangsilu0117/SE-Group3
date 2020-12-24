package me.bakumon.moneykeeper.ui.membermanage;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.database.entity.Member;

public class MemberManageAdapter extends BaseDataBindingAdapter<Member> {
    public MemberManageAdapter(@Nullable List<Member> data) {
        super(R.layout.item_member_manage, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, Member item) {
        ViewDataBinding binding = helper.getBinding();

        binding.setVariable(BR.member, item);
        boolean isLastItem = helper.getAdapterPosition() == mData.size() - 1;
        // 单位是 dp
        binding.setVariable(BR.bottomMargin, isLastItem ? 100 : 0);

        binding.executePendingBindings();
    }


}
