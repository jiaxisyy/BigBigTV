package com.share_will.mobile.model.entity;

public class NotifyMessageEntity {

    /**
     * showNumber : 1
     * messageId : 9
     * messageTitle : 123
     * startTime : 1543664243000
     * endTime : 1546083445000
     * messageContent : 1234
     */

    private int showNumber;
    private int messageId;
    private String messageTitle;
    private long startTime;
    private long endTime;
    private String messageContent;

    public int getShowNumber() {
        return showNumber;
    }

    public void setShowNumber(int showNumber) {
        this.showNumber = showNumber;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

}
