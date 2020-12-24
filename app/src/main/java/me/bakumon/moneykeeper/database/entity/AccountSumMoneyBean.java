package me.bakumon.moneykeeper.database.entity;

import java.math.BigDecimal;

/**
 * 某个账户收入/支出的总和
 */

public class AccountSumMoneyBean {
    /**
     * 类型
     * 0：现金
     * 1：支付宝
     * 2: 微信
     */
    public int account;

    //lxy:
    public String accountName;

    public int type;

    /**
     * 支出或收入的总和
     */
    public BigDecimal accountSumMoney;


}
