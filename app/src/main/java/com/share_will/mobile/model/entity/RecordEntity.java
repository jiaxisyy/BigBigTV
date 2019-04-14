package com.share_will.mobile.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ChenGD on 2018/2/27.
 *
 * @author chenguandu
 */

public class RecordEntity {
    private String datetime;
    private long deposit;
    private long borrow;
    /**
     * orderid : 13510946094201904141552190052
     * phone : 13510946094
     * deposit : null
     * borrow : 1
     * type : 1
     */

    private String orderid;
    private String phone;
    @SerializedName("deposit")
    private Object depositX;
    @SerializedName("borrow")
    private int borrowX;
    private int type;

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

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getDepositX() {
        return depositX;
    }

    public void setDepositX(Object depositX) {
        this.depositX = depositX;
    }

    public int getBorrowX() {
        return borrowX;
    }

    public void setBorrowX(int borrowX) {
        this.borrowX = borrowX;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
