package com.lordralex.totalpermissions.util;

/**
 *
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class ArrayShift {

    public static String[] getEndOfStringArray(String[] array, int length) {
        int newLength = array.length - length;
        if (newLength < 0) {
            newLength = 0;
        }
        String[] newArray = new String[newLength];
        if (length == 0) {
            newArray = new String[]{""};
        }
        int w = length;
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = array[w];
            w++;
        }
        return newArray;
    }
}
