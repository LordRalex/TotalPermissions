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
package net.ae97.totalpermissions.commands.subcommands.actions;

import java.util.Map;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.lang.Lang;
import net.ae97.totalpermissions.permission.PermissionBase;
import net.ae97.totalpermissions.permission.PermissionType;
import net.ae97.totalpermissions.permission.PermissionUser;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class ListAction extends SubAction {

    protected final TotalPermissions plugin;

    public ListAction(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);

        if (field.equalsIgnoreCase("permissions")) {
            sender.sendMessage("Permissions for " + target + ":");
            sender.sendMessage(permMapToString(tar.getPerms(world)));
            return true;
        } else if (field.equalsIgnoreCase("inheritance")) {
            StringBuilder sb = new StringBuilder();
            for (String inher : tar.getInheritances(world)) {
                sb.append(inher).append(", ");
            }
            sender.sendMessage(Lang.COMMAND_ACTION_LIST_INHERITANCE.getMessage(target, sb.substring(0, sb.length() - 3)));
            return true;
        } else if (field.equalsIgnoreCase("commands")) {
            StringBuilder sb = new StringBuilder();
            for (String cmd : tar.getCommands(world)) {
                sb.append(cmd).append(", ");
            }
            sender.sendMessage(Lang.COMMAND_ACTION_LIST_COMMANDS.getMessage(target, sb.substring(0, sb.length() - 3)));
            return true;
        } else if (field.equalsIgnoreCase("groups")) {
            if (tar instanceof PermissionUser) {
                PermissionUser newtar = (PermissionUser) tar;
                StringBuilder sb = new StringBuilder();
                for (String group : newtar.getGroups(world)) {
                    sb.append(group).append(", ");
                }
                sender.sendMessage(Lang.COMMAND_ACTION_LIST_GROUPS.getMessage(target, sb.substring(0, sb.length() - 3)));
                return true;
            }
            return false;
        } else if (field.equalsIgnoreCase("prefix")) {
            sender.sendMessage(Lang.COMMAND_ACTION_LIST_PREFIX.getMessage(target, tar.getOption("prefix")));
            return true;
        } else if (field.equalsIgnoreCase("suffix")) {
            sender.sendMessage(Lang.COMMAND_ACTION_LIST_SUFFIX.getMessage(target, tar.getOption("suffix")));
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
            "list " + Lang.VARIABLES_FIELD.getMessage()
            + " " + Lang.VARIABLES_WORLDOPTIONAL.getMessage(),
            Lang.COMMAND_ACTION_LIST_HELP.getMessage()
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
