package com.share_will.mobile;

import java.text.DecimalFormat;

public class Terminal {

    public static void main(String[] args) {
        System.out.println(intChange(100));
    }

    public static String intChange(int num) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(num);
    }
}
