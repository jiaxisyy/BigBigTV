package com.share_will.mobile.model.entity;

import java.io.Serializable;

public class ChargeRecordEntity implements Serializable {

        /**
         * id : 293364
         * orderId : 18328317002201905101132516132
         * orderType : 3
         * payType : 1
         * userId : 18328317002
         * cabinetId : EYX1805301000001
         * batteryId : null
         * fullBatteryId : null
         * cabinetDoor : 3
         * energyStart : 0
         * energyEnd : 0
         * energyUsed : 0
         * money : 0
         * giveMoney : 150
         * startTime : 1557459172000
         * endTime : 1557460291000
         * fullTime : null
         * randomCode : 74tpO70XXmGET97a39c0
         * rechargeStatus : true
         * status : true
         * active : true
         * createTime : 1557459172000
         * creator : 18328317002
         * userName : null
         * customerName : null
         * stationName : null
         * stationAddress : null
         * cabinetAddress : null
         * sum : null
         * month : null
         * date : null
         * balanceType : null
         */

        private int id;
        private String orderId;
        private int orderType;
        private int payType;
        private String userId;
        private String cabinetId;
        private Object batteryId;
        private Object fullBatteryId;
        private int cabinetDoor;
        private int energyStart;
        private int energyEnd;
        private int energyUsed;
        private int money;
        private int giveMoney;
        private long startTime;
        private long endTime;
        private Object fullTime;
        private String randomCode;
        private boolean rechargeStatus;
        private boolean status;
        private boolean active;
        private long createTime;
        private String creator;
        private Object userName;
        private Object customerName;
        private Object stationName;
        private Object stationAddress;
        private String cabinetAddress;
        private Object sum;
        private Object month;
        private Object date;
        private int balanceType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCabinetId() {
            return cabinetId;
        }

        public void setCabinetId(String cabinetId) {
            this.cabinetId = cabinetId;
        }

        public Object getBatteryId() {
            return batteryId;
        }

        public void setBatteryId(Object batteryId) {
            this.batteryId = batteryId;
        }

        public Object getFullBatteryId() {
            return fullBatteryId;
        }

        public void setFullBatteryId(Object fullBatteryId) {
            this.fullBatteryId = fullBatteryId;
        }

        public int getCabinetDoor() {
            return cabinetDoor;
        }

        public void setCabinetDoor(int cabinetDoor) {
            this.cabinetDoor = cabinetDoor;
        }

        public int getEnergyStart() {
            return energyStart;
        }

        public void setEnergyStart(int energyStart) {
            this.energyStart = energyStart;
        }

        public int getEnergyEnd() {
            return energyEnd;
        }

        public void setEnergyEnd(int energyEnd) {
            this.energyEnd = energyEnd;
        }

        public int getEnergyUsed() {
            return energyUsed;
        }

        public void setEnergyUsed(int energyUsed) {
            this.energyUsed = energyUsed;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getGiveMoney() {
            return giveMoney;
        }

        public void setGiveMoney(int giveMoney) {
            this.giveMoney = giveMoney;
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

        public Object getFullTime() {
            return fullTime;
        }

        public void setFullTime(Object fullTime) {
            this.fullTime = fullTime;
        }

        public String getRandomCode() {
            return randomCode;
        }

        public void setRandomCode(String randomCode) {
            this.randomCode = randomCode;
        }

        public boolean isRechargeStatus() {
            return rechargeStatus;
        }

        public void setRechargeStatus(boolean rechargeStatus) {
            this.rechargeStatus = rechargeStatus;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public Object getUserName() {
            return userName;
        }

        public void setUserName(Object userName) {
            this.userName = userName;
        }

        public Object getCustomerName() {
            return customerName;
        }

        public void setCustomerName(Object customerName) {
            this.customerName = customerName;
        }

        public Object getStationName() {
            return stationName;
        }

        public void setStationName(Object stationName) {
            this.stationName = stationName;
        }

        public Object getStationAddress() {
            return stationAddress;
        }

        public void setStationAddress(Object stationAddress) {
            this.stationAddress = stationAddress;
        }

        public String getCabinetAddress() {
            return cabinetAddress;
        }

        public void setCabinetAddress(String cabinetAddress) {
            this.cabinetAddress = cabinetAddress;
        }

        public Object getSum() {
            return sum;
        }

        public void setSum(Object sum) {
            this.sum = sum;
        }

        public Object getMonth() {
            return month;
        }

        public void setMonth(Object month) {
            this.month = month;
        }

        public Object getDate() {
            return date;
        }

        public void setDate(Object date) {
            this.date = date;
        }

        public int getBalanceType() {
            return balanceType;
        }

        public void setBalanceType(int balanceType) {
            this.balanceType = balanceType;
        }

}
