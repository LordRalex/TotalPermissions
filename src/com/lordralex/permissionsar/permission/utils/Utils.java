package com.lordralex.permissionsar.permission.utils;

import com.lordralex.permissionsar.PermissionsAR;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author AmberK
 */
public class Utils {

    // All utils crap below, thought about moving to a Utils.class but not sure yet, as all commands just extend this class anyhow.
    public static String match(String name) {
        List<Player> matches = Bukkit.matchPlayer(name);
        if (matches.size() >= 1) {
            return (("Perhaps you meant " + matches.get(0).getName()) + "?");
        } else {
            return ("No close matches found.");
        }
    }

    public static void out(String message) {
        PermissionsAR.log.info(message);
    }

    public static void outc(CommandSender cs, String message) {
        if (cs instanceof Player) {
            System.out.println("[PLAYER COMMAND] " + cs.getName() + ": /" + message);
        }
    }

    public static void output(CommandSender cs, String message) {
        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void output(Player p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void formatTitle(CommandSender s, String title, ChatColor barcolor, ChatColor titlecolor) {
        String line = barcolor + "------------------------------------------------------------";
        int pivot = line.length() / 2;
        String center = "[ " + titlecolor + title + barcolor + " ]";
        String out = line.substring(0, pivot - center.length() / 2);
        out += center + line.substring(pivot + center.length() / 2);
        output(s, out);
    }
}
