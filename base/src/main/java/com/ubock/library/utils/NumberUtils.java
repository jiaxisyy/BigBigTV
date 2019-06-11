package com.ubock.library.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ChenGD on 2017-8-24.
 */

public class NumberUtils {
    /**
     * 是否整数
     *
     * @param num
     * @return
     */
    public static boolean isInt(String num) {
        String reg = "^-?\\d+$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(num);
        boolean b = m.matches();
        return b;
    }

    /**
     * 是否小数,浮点数
     *
     * @param num
     * @return
     */
    public static boolean isFloat(String num) {
        String reg = "^(-?\\d+)(\\.\\d+)?$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(num);
        boolean b = m.matches();
        return b;
    }

    public static BigDecimal stringToBigDecimalFormat(String bdStr) {
        if (bdStr == null || "".equals(bdStr)) {
            bdStr = "0";
        }
        BigDecimal bd = new BigDecimal(bdStr);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 默认保留一位小数
     *
     * @param f
     * @return
     */
    public static float formatNum(float f) {
        return formatNum(f, 1);
    }

    /**
     * @param f
     * @param i 保留几位小数,最多两位
     * @return
     */
    public static float formatNum(float f, int i) {

        if (i == 1) {
            return (float) (Math.round(f * 10) / 10);
        } else if (i == 2) {
            return (float) (Math.round(f * 100) / 100);
        }
        return (float) (Math.round(f * 100) / 100);
    }

}
