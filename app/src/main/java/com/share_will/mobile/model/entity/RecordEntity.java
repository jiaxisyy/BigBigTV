package com.share_will.mobile.model.entity;

/**
 * Created by ChenGD on 2018/2/27.
 *
 * @author chenguandu
 */

public class RecordEntity {

        /**
         * orderid : 13510946094201904141552190052
         * phone : 13510946094
         * deposit : null
         * borrow : 1
         * type : 1
         * datetime : 2019-04-14 15:53:11
         */

        private String orderid;
        private String phone;
        private int deposit;
        private int borrow;
        private int type;
        private String datetime;

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

        public int getDeposit() {
            return deposit;
        }

        public void setDeposit(int deposit) {
            this.deposit = deposit;
        }

        public int getBorrow() {
            return borrow;
        }

        public void setBorrow(int borrow) {
            this.borrow = borrow;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

}
