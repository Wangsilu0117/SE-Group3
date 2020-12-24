package me.bakumon.moneykeeper.database.entity;

import java.math.BigDecimal;

/**
 * 成员汇总实体
 */
public class MemberSumMoneyBean {
    /**
     * 类型
     * 0：支出
     * 1：收入
     *
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
//    public int type;

    public String memberName;
    public BigDecimal memberSumMoney;
    public int memberId;
    public int count;
}
