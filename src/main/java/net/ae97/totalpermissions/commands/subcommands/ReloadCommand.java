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
package net.ae97.totalpermissions.commands.subcommands;

import net.ae97.totalpermissions.PermissionManager;
import net.ae97.totalpermissions.TotalPermissions;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * @since 0.1
 * @author Lord_Ralex
 * @version 0.2
 */
public class ReloadCommand implements SubCommand {

    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + TotalPermissions.getPlugin().getLangFile().getString("command.reload.reloading", TotalPermissions.getPlugin().getDescription().getFullName()));
        PermissionManager manager = TotalPermissions.getPlugin().getManager();
        manager.unload();
        try {
            manager.load();
        } catch (InvalidConfigurationException ex) {
            sender.sendMessage(ChatColor.RED + TotalPermissions.getPlugin().getLangFile().getString("command.reload.badconfig"));
            TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, null, ex);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerLoginEvent refreshEvent = new PlayerLoginEvent(player, "", null);
            try {
                TotalPermissions.getPlugin().getListener().onPlayerLogin(refreshEvent);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + TotalPermissions.getPlugin().getLangFile().getString("command.reload.general", player.getName()));
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, null, e);
            }
        }
        sender.sendMessage(ChatColor.GREEN + TotalPermissions.getPlugin().getLangFile().getString("command.reload.success", TotalPermissions.getPlugin().getDescription().getFullName()));
        return true;
    }

    public String getName() {
        return "reload";
    }

    public String getPerm() {
        return "totalpermissions.cmd.reload";
    }

    public String[] getHelp() {
        return new String[]{
            "/ttp reload",
            TotalPermissions.getPlugin().getLangFile().getString("command.reload.help")
        };
    }
}
