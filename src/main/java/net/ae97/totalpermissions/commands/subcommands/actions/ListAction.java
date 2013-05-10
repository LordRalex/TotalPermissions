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
import net.ae97.totalpermissions.permission.PermissionGroup;
import net.ae97.totalpermissions.permission.PermissionType;
import net.ae97.totalpermissions.permission.PermissionUser;
import org.bukkit.command.CommandSender;

/**
 *
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class ListAction implements SubAction {

    public boolean execute(CommandSender sender, String aType, String target, String field, String item) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        if (field.equalsIgnoreCase("permission")) {
            sender.sendMessage("Permissions for " + target + ":");
            sender.sendMessage(this.permMapToString(tar.getPerms()));
        }
        else if (field.equalsIgnoreCase("inheritance")) {
            // Not possible
        }
        else if (field.equalsIgnoreCase("command")) {
            // Not possible
        }
        else if (field.equalsIgnoreCase("group")) {
            if (tar instanceof PermissionUser) {
                PermissionUser newtar = (PermissionUser)tar;
                // Not possible
            }
        }
        else if (field.equalsIgnoreCase("default")) {
            sender.sendMessage("Default group: " + TotalPermissions.getPlugin().getManager().getDefaultGroup());
        }
        else if (field.equalsIgnoreCase("prefix")) {
            sender.sendMessage("Prefix: " + tar.getOption("prefix"));
        }
        else if (field.equalsIgnoreCase("suffix")) {
            sender.sendMessage("Suffix: " + tar.getOption("suffix"));
        }
        //Self explanatory
        return true;
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
        return new String[] {
            "permission",
            "inheritance",
            "command",
            "group",
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