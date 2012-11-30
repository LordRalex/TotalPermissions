package com.lordralex.totalpermissions.commands.subcommands;

import com.lordralex.totalpermissions.PermissionManager;
import com.lordralex.totalpermissions.TotalPermissions;
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
        sender.sendMessage(ChatColor.YELLOW + "Reloading " + TotalPermissions.getPlugin().getDescription().getFullName());
        PermissionManager manager = TotalPermissions.getManager();
        manager.unload();
        try {
            manager.load();
        } catch (InvalidConfigurationException ex) {
            sender.sendMessage(ChatColor.RED + "An error occured when reloading, check your server logs");
            TotalPermissions.getLog().log(Level.SEVERE, null, ex);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerLoginEvent refreshEvent = new PlayerLoginEvent(player, "", null);
            try {
                TotalPermissions.getListener().onPlayerLogin(refreshEvent);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "An error occured while reloading " + player.getName());
                TotalPermissions.getLog().log(Level.SEVERE, null, e);
            }
        }
        sender.sendMessage(ChatColor.GREEN + TotalPermissions.getPlugin().getDescription().getFullName() + " has reloaded");
    }

    @Override
    public String getPerm() {
        return "totalpermissions.command.reload";
    }

    public String[] getHelp() {
        return new String[]{
                    "Usage: /totalperms reload",
                    "Reloads TotalPermissions, clearing the cache and re-reading the files"
                };
    }
}
