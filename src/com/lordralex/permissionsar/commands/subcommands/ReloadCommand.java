/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.permissionsar.commands.subcommands;

import com.lordralex.permissionsar.PermissionManager;
import com.lordralex.permissionsar.PermissionsAR;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 *
 * @author Joshua
 */
public class ReloadCommand implements SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "Reloading " + PermissionsAR.getPlugin().getDescription().getFullName());
        PermissionManager manager = PermissionsAR.getManager();
        manager.unload();
        try {
            manager.load();
        } catch (InvalidConfigurationException ex) {
            sender.sendMessage(ChatColor.RED + "An error occured when reloading, check your server logs");
            PermissionsAR.getLog().log(Level.SEVERE, null, ex);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerLoginEvent refreshEvent = new PlayerLoginEvent(player, "", null);
            try {
                PermissionsAR.getListener().onPlayerLogin(refreshEvent);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "An error occured while reloading " + player.getName());
                PermissionsAR.getLog().log(Level.SEVERE, null, e);
            }
        }
        sender.sendMessage(ChatColor.GREEN + PermissionsAR.getPlugin().getDescription().getFullName() + " has reloaded");
    }

    @Override
    public String getPerm() {
        return "permissionsar.command.reload";
    }
}
