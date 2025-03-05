package com.clone.rottentomato.util;

import java.math.BigDecimal;

/** 숫자형 관련 util 클래스 */
public class UtilNumber {
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            new BigDecimal(str); // 정수, 실수 모두 검증 가능
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(str); // 정수, 실수 모두 검증 가능
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Long returnLong(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(str); // 정수, 실수 모두 검증 가능
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
