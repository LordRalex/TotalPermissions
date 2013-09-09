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

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(plugin.getLangFile().getString("command.reload.reloading", plugin.getDescription().getFullName()));
        PermissionManager manager = plugin.getManager();
        manager.unload();
        try {
            manager.load();
        } catch (InvalidConfigurationException ex) {
            sender.sendMessage(plugin.getLangFile().getString("command.reload.badconfig"));
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerLoginEvent refreshEvent = new PlayerLoginEvent(player, "", null);
            try {
                plugin.getListener().onPlayerLogin(refreshEvent);
            } catch (Exception e) {
                sender.sendMessage(plugin.getLangFile().getString("command.reload.general", player.getName()));
                plugin.getLogger().log(Level.SEVERE, null, e);
            }
        }
        sender.sendMessage(plugin.getLangFile().getString("command.reload.success", plugin.getDescription().getFullName()));
        return true;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "ttp reload",
            plugin.getLangFile().getString("command.reload.help")
        };
    }
}