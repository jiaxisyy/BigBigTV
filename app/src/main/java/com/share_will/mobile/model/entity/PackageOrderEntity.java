package com.share_will.mobile.model.entity;

public class PackageOrderEntity {

    /**
     * orderId : 201805021304011629
     * price : 19900
     * userId : 13510946094
     */

    private String orderId;
    private int price;
    private String userId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
