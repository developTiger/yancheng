package com.sunesoft.seera.fr.utils;

import java.util.Random;
import java.util.UUID;


public class StringUtils {
    private static final char[] numSource = ("0123456789").toCharArray();
    private static final char[] inviteSource = ("abcdefghijklmnopqrstuvwxyz0123456789").toCharArray();

    /**
     * 生成指定位数的随机数字字符串（取值范围：0-9）
     *
     * @param length 指定生成字符串的长度。
     * @return 随机数字字符串
     */
    public static String getRandomNumCode(int length) {
        Random rd = new Random();

        char[] rdBuffer = new char[length];
        for (int i = 0; i < length; i++) {
            rdBuffer[i] = numSource[rd.nextInt(9)];
        }
        return new String(rdBuffer);
    }

    /**
     * 生成指定位数的随机字符串（取值范围：0-9，a-z）
     *
     * @param length 指定生成字符串的长度。
     * @return 随机字符串
     */
    public static String getRandomStrCode(int length) {
        Random rd = new Random();

        char[] rdBuffer = new char[length];
        for (int i = 0; i < length; i++) {
            rdBuffer[i] = inviteSource[rd.nextInt(35)];
        }
        return new String(rdBuffer);
    }

    /**
     * 生成一个32位的唯一字符串。
     *
     * @return 票据。
     */
    public static String getUniqueTicket() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 判断数组中是否存在某个元素。
     *
     * @param collections 集合。
     * @param element     元素。
     * @return true 表示存在，false 表示不存在。
     */
    public static boolean isExist(String[] collections, String element) {
        for (String collection : collections) {
            if (collection.equals(element))
                return true;
        }
        return false;
    }

    public static boolean isNullOrWhiteSpace(String str) {
        if (str == null)
            return true;
        if ("".equals(str.trim()))
            return true;
        return false;
    }
}
