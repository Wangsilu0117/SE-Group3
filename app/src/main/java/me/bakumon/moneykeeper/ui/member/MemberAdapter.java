package me.bakumon.moneykeeper.ui.member;

import android.databinding.ViewDataBinding;

import java.util.List;

import io.reactivex.annotations.Nullable;
import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.database.entity.MemberSumMoneyBean;
/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */


public class MemberAdapter extends BaseDataBindingAdapter<MemberSumMoneyBean> {
    private int maxSum = 0;
    public MemberAdapter (@Nullable List<MemberSumMoneyBean> data){
        super(R.layout.item_member,data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, MemberSumMoneyBean item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.MemberSumMoneyBean,item);
        binding.setVariable(BR.maxSum,this.maxSum);
        binding.executePendingBindings();
    }

    public void SetMax(int max){
        this.maxSum = max;
    }
}
