package com.lordralex.permissionsar.permission.utils;

import com.lordralex.permissionsar.PermissionsAR;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @version 1.0
 * @author AmberK
 * @since 1.0
 */
public class Utils {

    /**
     * Searches for a name. If no name is found, this returns null.
     *
     * @param name Name to search
     * @return Closest name, or null if none
     */
    public static String match(String name) {
        List<Player> matches = Bukkit.matchPlayer(name);
        if (matches.isEmpty()) {
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
        PermissionsAR.getLog().info(stripColors(message));
    }

    /**
     * Sends a message to the console that a command was used.
     *
     * @param cs Command user
     * @param message Message to amend
     */
    public static void outc(CommandSender cs, String message) {
        if (cs instanceof Player) {
            Bukkit.getLogger().info("[PLAYER COMMAND] " + cs.getName() + ": /" + message);
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
        String newMessage = message;
        newMessage = ChatColor.translateAlternateColorCodes(ChatColor.COLOR_CHAR, newMessage);
        return newMessage;
    }

    /**
     * Removes colors from a message
     *
     * @param message Message to strip
     * @return The non-colored version
     */
    public static String stripColors(String message) {
        String newMessage = message;
        newMessage = ChatColor.stripColor(newMessage);
        return newMessage;
    }
}
