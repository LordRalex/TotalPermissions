/*
 * Copyright (C) 2013 Lord_Ralex
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
package com.lordralex.totalpermissions.permission.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * @author Lord_Ralex
 * @version 0.1
 * @since 0.1
 */
public final class Utility {

    /**
     * Formats a title bar. The text will be in the center surrounded by "-"
     *
     * @param title The title to show
     * @param barcolor Color for the bars
     * @param titlecolor Color for the title
     * @return Title in form of a String
     *
     * @since 0.1
     */
    public static String formatTitle(String title, ChatColor barcolor, ChatColor titlecolor) {
        String line = barcolor + "------------------------------------------------------------";
        int pivot = line.length() / 2;
        String center = "[ " + titlecolor + title + barcolor + " ]";
        String out = line.substring(0, pivot - center.length() / 2);
        out += center + line.substring(pivot + center.length() / 2);
        return out;
    }

    /**
     * Generates the colors in a message
     *
     * @param message Message to handle
     * @return New message with colors shown
     *
     * @since 0.1
     */
    public static String formatColors(String message) {
        return ChatColor.translateAlternateColorCodes(ChatColor.COLOR_CHAR, message);
    }

    /**
     * Removes colors from a message
     *
     * @param message Message to strip
     * @return The non-colored version
     *
     * @since 0.1
     */
    public static String stripColors(String message) {
        return ChatColor.stripColor(message);
    }

    /**
     * Provides a list of all perms given by using '*'. This will only look at
     * registered permissions made by plugins.
     *
     * @return List of perms given by '*'
     */
    public static List<String> handleWildcard() {
        return handleWildcard(false);
    }

    /**
     * Provides a list of all perms given by using '*' or '**'. This will only
     * look at registered permissions made by plugins.
     *
     * @param isAll True if '**', false for '*'
     *
     * @return List of perms given
     */
    public static List<String> handleWildcard(boolean isAll) {
        List<String> perms = new ArrayList<String>();
        Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
        for (Permission permTest : permT) {
            if (permTest.getDefault() == PermissionDefault.OP || permTest.getDefault() == PermissionDefault.TRUE) {
                perms.add(permTest.getName());
            } else if (isAll) {
                perms.add(permTest.getName());
            }
        }
        return perms;
    }

    /**
     * Gets a list of the permissions for a given list of commands. This list
     * may not be in the exact order and may not contain the correct perm or may
     * not be the same size.
     *
     * @param commands List of commands to get perms for
     * @return List of perms for those commands
     */
    public static List<String> getPermsForCommands(List<String> commands) {
        List<String> perms = new ArrayList<String>();
        for (String command : commands) {
            Command cmd = Bukkit.getPluginCommand(command);
            if (cmd != null) {
                perms.add(cmd.getPermission());
            }
        }
        return perms;
    }

    private Utility() {
    }
}
