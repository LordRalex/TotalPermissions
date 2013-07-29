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
package net.ae97.totalpermissions.commands.subcommands.actions;

import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionBase;
import net.ae97.totalpermissions.permission.PermissionType;
import net.ae97.totalpermissions.permission.PermissionUser;
import java.util.Map;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class ListAction extends SubAction {

    @Override
    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);

        if (field.equalsIgnoreCase("permissions")) {
            sender.sendMessage("Permissions for " + target + ":");
            sender.sendMessage(this.permMapToString(tar.getPerms(world)));
            return true;
        } else if (field.equalsIgnoreCase("inheritance")) {
            StringBuilder sb = new StringBuilder();
            for (String inher : tar.getInheritances(world)) {
                sb.append(inher).append(", ");
            }
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.list.inheritance", target, sb.substring(0, sb.length() - 3)));
            return true;
        } else if (field.equalsIgnoreCase("commands")) {
            StringBuilder sb = new StringBuilder();
            for (String cmd : tar.getCommands(world)) {
                sb.append(cmd).append(", ");
            }
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.list.commands", target, sb.substring(0, sb.length() - 3)));
            return true;
        } else if (field.equalsIgnoreCase("groups")) {
            if (tar instanceof PermissionUser) {
                PermissionUser newtar = (PermissionUser) tar;
                StringBuilder sb = new StringBuilder();
                for (String group : newtar.getGroups(world)) {
                    sb.append(group).append(", ");
                }
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.list.groups", target, sb.substring(0, sb.length() - 3)));
                return true;
            }
            return false;
        } else if (field.equalsIgnoreCase("prefix")) {
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.list.prefix", target, tar.getOption("prefix")));
            return true;
        } else if (field.equalsIgnoreCase("suffix")) {
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.list.suffix", target, tar.getOption("suffix")));
            return true;
        }

        return false;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "list " + TotalPermissions.getPlugin().getLangFile().getString("variables.field")
            + " " + TotalPermissions.getPlugin().getLangFile().getString("variables.world-optional"),
            TotalPermissions.getPlugin().getLangFile().getString("command.action.list.help")
        };
    }

    @Override
    public String[] supportedTypes() {
        return new String[]{
            "permissions",
            "inheritance",
            "commands",
            "groups",
            "prefix",
            "suffix"
        };
    }

    private String permMapToString(Map<String, Boolean> map) {
        StringBuilder sb = new StringBuilder();
        String[] keys = map.keySet().toArray(new String[map.size()]);
        for (String temp : keys) {
            if (!map.get(temp)) {
                sb.append('-');
            }
            sb.append(temp).append('\n');
        }
        return sb.toString();
    }
}