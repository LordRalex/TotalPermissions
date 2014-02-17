/*
 * Copyright (C) 2014 AE97
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

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

/**
 * @author Lord_Ralex
 */
public class Formatter {

    private static final String BAR = "--------------------------------------------------------";
    private static final char BAR_CHAR = '-';

    public static String formatTitle(String title, ChatColor barcolor, ChatColor titlecolor) {
        return StringUtils.center("[" + title + "]", BAR.length(), BAR_CHAR);
    }

    public static String formatFooter(ChatColor color) {
        return (color + BAR);
    }

    public static String formatColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String stripColors(String message) {
        return ChatColor.stripColor(message);
    }
}
