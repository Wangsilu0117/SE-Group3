package me.bakumon.moneykeeper.ui.membermanage;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.Account;
import me.bakumon.moneykeeper.database.entity.Member;
import me.bakumon.moneykeeper.datasource.AppDataSource;

public class MemberManageViewModel extends BaseViewModel {
    public MemberManageViewModel(AppDataSource dataSource) {
        super(dataSource);
    }
    public Flowable<List<Member>> getAllMembers() {
        return mDataSource.getAllMembers();
    }

    public Completable deleteMember(Member member) {
        return mDataSource.deleteMember(member);
    }
}
