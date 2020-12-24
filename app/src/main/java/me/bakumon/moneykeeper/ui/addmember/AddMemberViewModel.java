package me.bakumon.moneykeeper.ui.addmember;

import io.reactivex.Completable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.Member;
import me.bakumon.moneykeeper.datasource.AppDataSource;

public class AddMemberViewModel extends BaseViewModel {
    public AddMemberViewModel(AppDataSource dataSource) {
        super(dataSource);
    }
    public Completable saveMember(Member member, String name) {
        if (member == null) {
            // 添加
            return mDataSource.addMember(name);
        } else {
            // 修改
            Member updateMember = new Member(member.id, name, member.ranking);
            updateMember.state = member.state;
            return mDataSource.updateMember(member, updateMember);
        }
    }
}
