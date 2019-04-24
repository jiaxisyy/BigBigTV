package com.share_will.mobile;

import java.text.DecimalFormat;

public class Terminal {

    public static void main(String[] args) {
        float mon = 60 / 100f;
        System.out.println(mon);
    }

    public static String intChange(int num) {

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }
}
