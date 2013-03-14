/*
 * Copyright (C) 2013 LordRalex
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

import com.lordralex.totalpermissions.TotalPermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * @version 0.1
 * @author Rogue
 * @since 0.1
 * @deprecated
 */
public class Utils {

    /**
     * Searches for a name. If no name is found, this returns null.
     *
     * @param name Name to search
     * @return Closest name, or null if none
     *
     * @deprecated
     */
    public static String match(String name) {
        List<Player> matches = Bukkit.matchPlayer(name);
        if (!matches.isEmpty()) {
            return matches.get(0).getName();
        } else {
            return null;
        }
    }

    /**
     * Sends a message to the console.
     *
     * @param message Message to send
     */
    public static void out(String message) {
        TotalPermissions.getPlugin().getLogger().info(stripColors(message));
    }

    /**
     * Sends a message to the console that a command was used.
     *
     * @param cs Command user
     * @param message Message to amend
     */
    public static void outc(CommandSender cs, String message) {
        if (cs instanceof Player) {
            TotalPermissions.getPlugin().getLogger().info("[PLAYER COMMAND] " + cs.getName() + ": /" + message);
        }
    }

    /**
     * Sends a message to an instance of {@link CommandSender}
     *
     * @param sender Target
     * @param message Message to send
     */
    public static void output(CommandSender sender, String message) {
        sender.sendMessage(formatColors(message));
    }

    /**
     * Sends a message to an instance of {@link Player}
     *
     * @param player Target
     * @param message Message to send
     */
    public static void output(Player player, String message) {
        player.sendMessage(formatColors(message));
    }

    /**
     * Formats a title bar. The text will be in the center surrounded by "-"
     *
     * @param title The title to show
     * @param barcolor Color for the bars
     * @param titlecolor Color for the title
     * @return Title in form of a String
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
     */
    public static String formatColors(String message) {
        return ChatColor.translateAlternateColorCodes(ChatColor.COLOR_CHAR, message);
    }

    /**
     * Removes colors from a message
     *
     * @param message Message to strip
     * @return The non-colored version
     */
    public static String stripColors(String message) {
        return ChatColor.stripColor(message);
    }

    public static List<String> handleWildcard() {
        return handleWildcard(false);
    }

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

    private Utils() {
    }
}
