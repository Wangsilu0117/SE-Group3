package me.bakumon.moneykeeper.datasource;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.entity.Member;

public class MemberInitCreator {
    public static Member[] createMemberData() {
        List<Member> list = new ArrayList<>();
        Resources res = App.getINSTANCE().getResources();
        Member member;
        member = new Member(res.getString(R.string.text_member_none),1);
        list.add(member);
        member = new Member(res.getString(R.string.text_member_self),2);
        list.add(member);
        member = new Member(res.getString(R.string.text_member_children),3);
        list.add(member);
        member = new Member(res.getString(R.string.text_member_husband),4);
        list.add(member);
        member = new Member(res.getString(R.string.text_member_wife),5);
        list.add(member);
        member = new Member(res.getString(R.string.text_member_family),6);
        list.add(member);
        return list.toArray(new Member[list.size()]);
    }
}