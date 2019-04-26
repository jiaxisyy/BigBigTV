package com.share_will.mobile.model.entity;

public class TempEntity {
    private int status;
    private boolean clicked;

    public TempEntity(int status, boolean clicked) {
        this.status = status;
        this.clicked = clicked;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
