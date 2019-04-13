package com.ubock.library.utils;

import java.io.EOFException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okio.Buffer;

/**
 * Created by ChenGD on 2017-5-3.
 */

public class StringUtil {

    /**
     * 判断输入的号码是否为手机号
     */
    public static boolean checkMobile(String mobile) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public static String format(double d) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(d);
    }


    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 正则表达式:验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";

    /**
     * 手机号码中间部分替换为*
     *
     * @param mobile
     * @return 返回替换后的字符串
     */
    public static String phoneNumberHideCenter(String mobile) {
        mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return mobile;
    }

    /**
     * @param str
     * @return
     * @Method : isNumber
     * @Description: 判断是否为金额
     */
    public static boolean isNumber(String str) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        java.util.regex.Matcher match = pattern.matcher(str);
        return match.matches();
    }

    private static int ID_LENGTH = 17;

    /**
     * 校验身份证号
     *
     * @param idNum
     * @return
     */
    public static boolean vIDNumByCode(String idNum) {
        // 系数列表
        int[] ratioArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        // 校验码列表
        char[] checkCodeList = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        try {


            // 获取身份证号字符数组
            char[] cIds = idNum.toCharArray();

            // 获取最后一位（身份证校验码）
            char oCode = cIds[ID_LENGTH];
            int[] iIds = new int[ID_LENGTH];
            int idSum = 0;// 身份证号第1-17位与系数之积的和
            int residue = 0;// 余数(用加出来和除以11，看余数是多少？)

            for (int i = 0; i < ID_LENGTH; i++) {
                iIds[i] = cIds[i] - '0';
                idSum += iIds[i] * ratioArr[i];
            }

            residue = idSum % 11;// 取得余数

            return Character.toUpperCase(oCode) == checkCodeList[residue];
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 隐藏部分号码
     * @param source
     * @param start 包含start
     * @param end 不包含end
     * @return
     */
    public static String hidePhone(String source, int start, int end){
        if (source == null){
            return null;
        }
        if (source.length() <= end){
            return source;
        }
        StringBuilder sb = new StringBuilder(source);
        StringBuilder holder = new StringBuilder();
        for (int i = start; i < end; i++){
            holder.append("*");
        }
        sb.replace(start, end, holder.toString());
        return sb.toString();
    }

    /**
     * 是否是文本类型
     */
    public static boolean isText(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

}
