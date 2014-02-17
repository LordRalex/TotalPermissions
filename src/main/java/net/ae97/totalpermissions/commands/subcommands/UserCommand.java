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

import java.util.Arrays;
import java.util.List;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.base.PermissionUser;
import net.ae97.totalpermissions.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author 1Rogue
 */
public class UserCommand implements SubCommand {

    protected final TotalPermissions plugin;

    public UserCommand(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if ((args.length == 1) || args.length == 2) {
            OfflinePlayer p = null;
            if (args.length == 2) {
                p = Bukkit.getOfflinePlayer(args[1]);
            } else if (args.length == 1) {
                if (sender instanceof Player) {
                    p = (Player) sender;
                } else {
                    sender.sendMessage(Lang.COMMAND_USER_NONPLAYER.getMessage());
                    return false;
                }
            }
            PermissionUser user = plugin.getDataManager().getUser(p.getName());
            StringBuilder sb = new StringBuilder();
            for (String group : user.getGroups()) {
                sb.append(group).append(", ");
            }
            sender.sendMessage(Lang.COMMAND_USER_PLAYER.getMessage(sender.getName()));
            sender.sendMessage(Lang.COMMAND_USER_DEBUG.getMessage(user.isDebug()));
            sender.sendMessage(Lang.COMMAND_USER_GROUPS.getMessage(sb.substring(0, sb.length() - 2)));
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
            "ttp user " + Lang.VARIABLES_USERNAME.getMessage() + " [actions..]",
            Lang.COMMAND_USER_HELP.getMessage()
        };
    }

    private List<String> fields() {
        return Arrays.asList(new String[]{
            "permissions",
            "commands",
            "groups",
            "prefix",
            "suffix"
        });
    }
}
