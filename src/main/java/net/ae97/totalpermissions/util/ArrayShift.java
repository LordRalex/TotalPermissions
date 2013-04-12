/*
 * Copyright (C) 2013 AE97
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.ae97.totalpermissions.util;

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
