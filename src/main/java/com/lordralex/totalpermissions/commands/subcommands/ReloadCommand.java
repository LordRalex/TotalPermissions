package com.lordralex.totalpermissions.commands.subcommands;

import com.lordralex.totalpermissions.PermissionManager;
import com.lordralex.totalpermissions.TotalPermissions;
import com.lordralex.totalpermissions.listeners.TPListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginDescriptionFile;

public class ReloadCommand implements SubCommand {

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "Reloading " + TotalPermissions.getPlugin().getDescription().getFullName());
        PermissionManager manager = TotalPermissions.getPlugin().getManager();
        manager.unload();
        try {
            manager.load();
        } catch (InvalidConfigurationException ex) {
            sender.sendMessage(ChatColor.RED + "An error occured when reloading, check your server logs");
            TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, null, ex);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerLoginEvent refreshEvent = new PlayerLoginEvent(player, "", null);
            try {
                TotalPermissions.getPlugin().getListener().onPlayerLogin(refreshEvent);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "An error occured while reloading " + player.getName());
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, null, e);
            }
        }
        sender.sendMessage(ChatColor.GREEN + TotalPermissions.getPlugin().getDescription().getFullName() + " has reloaded");
    }

    public String getName() {
        return "reload";
    }
    
    public String getPerm() {
        return "totalpermissions.cmd.reload";
    }

    public String[] getHelp() {
        return new String[] {
            "/ttp reload",
            "Reloads TotalPermissions"
        };
    }
}