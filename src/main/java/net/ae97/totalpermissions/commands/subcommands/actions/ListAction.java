/*
 * Copyright (C) 2013 Spencer Alderman
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

import java.util.Map;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionBase;
import net.ae97.totalpermissions.permission.PermissionType;
import net.ae97.totalpermissions.permission.PermissionUser;
import org.bukkit.command.CommandSender;

/**
 *
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class ListAction extends SubAction {

    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        
        if (field.equalsIgnoreCase("permissions")) {
            sender.sendMessage("Permissions for " + target + ":");
            sender.sendMessage(this.permMapToString(tar.getPerms()));
            return true;
        }
        
        else if (field.equalsIgnoreCase("inheritance")) {
            StringBuilder sb = new StringBuilder(target);
            sb.append(" inherits: ");
            for (String inher : tar.getInheritances(world)) {
                sb.append(inher).append(", ");
            }
            sb.substring(0, sb.length() - 3);
            sb.append('.');
            sender.sendMessage(sb.toString());
            return true;
        }
        
        else if (field.equalsIgnoreCase("commands")) {
            StringBuilder sb = new StringBuilder("Commands for ");
            sb.append(target).append(": ");
            for (String cmd : tar.getCommands(world)) {
                sb.append(cmd).append(", ");
            }
            sb.substring(0, sb.length() - 3);
            sb.append('.');
            sender.sendMessage(sb.toString());
            return true;

        }
        
        else if (field.equalsIgnoreCase("groups")) {
            if (tar instanceof PermissionUser) {
                PermissionUser newtar = (PermissionUser) tar;
                StringBuilder sb = new StringBuilder("Groups for ");
                sb.append(target).append(": ");
                // Groups shouldn't be empty, should at least have the default group.
                for (String group : newtar.getGroups(world)) {
                    sb.append(group).append(", ");
                }
                sb.substring(0, sb.length() - 3);
                sb.append('.');
                sender.sendMessage(sb.toString());
                return true;
            }
            return false;
        }
        
        else if (field.equalsIgnoreCase("default")) {
            sender.sendMessage("Default group: " + TotalPermissions.getPlugin().getManager().getDefaultGroup());
            return true;
        }
        
        else if (field.equalsIgnoreCase("prefix")) {
            sender.sendMessage("Prefix: " + tar.getOption("prefix"));
            return true;
        }
        
        else if (field.equalsIgnoreCase("suffix")) {
            sender.sendMessage("Suffix: " + tar.getOption("suffix"));
            return true;
        }
        
        return false;
    }

    public String getName() {
        return "list";
    }

    public String[] getHelp() {
        return new String[]{
            "list <field>",
            "Lists the values from a given field"
        };
    }

    public String[] supportedTypes() {
        return new String[]{
            "permissions",
            "inheritance",
            "commands",
            "groups",
            "default",
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