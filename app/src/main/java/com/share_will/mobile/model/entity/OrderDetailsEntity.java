package com.share_will.mobile.model.entity;

public class OrderDetailsEntity {
    public OrderDetailsEntity(String name) {
        this.name = name;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
