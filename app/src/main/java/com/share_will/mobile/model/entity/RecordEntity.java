package com.share_will.mobile.model.entity;

/**
 * Created by ChenGD on 2018/2/27.
 *
 * @author chenguandu
 */

public class RecordEntity {
    private String datetime;
    private long deposit;
    private long borrow;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public long getDeposit() {
        return deposit;
    }

    public void setDeposit(long deposit) {
        this.deposit = deposit;
    }

    public long getBorrow() {
        return borrow;
    }

    public void setBorrow(long borrow) {
        this.borrow = borrow;
    }
}
