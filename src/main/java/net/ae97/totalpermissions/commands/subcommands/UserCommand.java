/*
 * Copyright (C) 2014 AE97
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

import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.base.PermissionUser;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author 1Rogue
 */
public final class UserCommand implements SubCommand {

    private final TotalPermissions plugin;

    public UserCommand(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if ((args.length == 1) || args.length == 2) {
            OfflinePlayer p = null;
            if (args.length == 2) {
                p = Bukkit.getOfflinePlayer(args[1]);
            } else if (args.length == 1 && sender instanceof Player) {
                p = (Player) sender;
            }
            if (p == null) {
                sender.sendMessage(ChatColor.RED + "No player found");
                return false;
            }
            PermissionUser user;
            try {
                user = plugin.getDataManager().getUser(p.getName());
            } catch (DataLoadFailedException ex) {
                plugin.getLogger().log(Level.SEVERE, "An error occured on loading " + args[0], ex);
                sender.sendMessage(ChatColor.RED + "An error occured on loading " + args[0]);
                return true;
            }
            sender.sendMessage(ChatColor.YELLOW + "Name: " + user.getName());
            sender.sendMessage(ChatColor.YELLOW + "Debug:" + user.isDebug());
            sender.sendMessage(ChatColor.YELLOW + "Groups: " + user.getGroups());
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "user";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "ttp user [username] [actions..]",
            "User interface"
        };
    }
}
