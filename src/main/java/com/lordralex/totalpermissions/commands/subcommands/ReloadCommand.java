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

/**
 * @since 0.1
 * @author Lord_Ralex
 * @version 0.1
 */
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