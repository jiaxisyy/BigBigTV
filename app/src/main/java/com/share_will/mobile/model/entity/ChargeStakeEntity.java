package com.share_will.mobile.model.entity;

/**
 * Created by ChenGD on 2019/4/28.
 *
 * @author chenguandu
 */
public class ChargeStakeEntity {
    private int index;
    private String cabinet;
    private int status;
    private long time;
    private boolean selected;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCabinet() {
        return cabinet;
    }

    public void setCabinet(String cabinet) {
        this.cabinet = cabinet;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "ChargeStakeEntity{" +
                "index=" + index +
                ", cabinet='" + cabinet + '\'' +
                ", status=" + status +
                ", time=" + time +
                '}';
    }
}
