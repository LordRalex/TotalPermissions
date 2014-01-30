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
public class CheckAction extends SubAction {

    protected final TotalPermissions plugin;

    public CheckAction(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        if (field.equalsIgnoreCase("permissions")) {
            if (tar.has(item, world)) {
                sender.sendMessage(Lang.COMMAND_ACTION_CHECK_PERMISSIONS_HAS.getMessage(target, item));
            } else {
                sender.sendMessage(Lang.COMMAND_ACTION_CHECK_PERMISSIONS_NOT.getMessage(target, item));
            }
            return true;
        } else if (field.equalsIgnoreCase("inheritance")) {
            if (tar.hasInheritance(item, world)) {
                sender.sendMessage(Lang.COMMAND_ACTION_CHECK_INHERITANCE_HAS.getMessage(target, item));
            } else {
                sender.sendMessage(Lang.COMMAND_ACTION_CHECK_INHERITANCE_NOT.getMessage(target, item));
            }
            return true;
        } else if (field.equalsIgnoreCase("commands")) {
            if (tar.hasCommand(item, world)) {
                sender.sendMessage(Lang.COMMAND_ACTION_CHECK_COMMANDS_HAS.getMessage(target, item));
            } else {
                sender.sendMessage(Lang.COMMAND_ACTION_CHECK_COMMANDS_NOT.getMessage(target, item));
            }
            return true;
        } else if (field.equalsIgnoreCase("groups")) {
            if (tar instanceof PermissionUser) {
                PermissionUser newtar = (PermissionUser) tar;
                if (newtar.hasInheritance(item, world)) {
                    sender.sendMessage(Lang.COMMAND_ACTION_CHECK_GROUPS_HAS.getMessage(target, item));
                } else {
                    sender.sendMessage(Lang.COMMAND_ACTION_CHECK_GROUPS_NOT.getMessage(target, item));
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "check " + Lang.VARIABLES_FIELD.getMessage()
            + " " + Lang.VARIABLES_VALUE.getMessage()
            + " " + Lang.VARIABLES_WORLDOPTIONAL.getMessage(),
            Lang.COMMAND_ACTION_CHECK_HELP.getMessage()
        };
    }

    @Override
    public String[] supportedTypes() {
        return new String[]{
            "permissions",
            "inheritance",
            "commands",
            "groups",};
    }
}
