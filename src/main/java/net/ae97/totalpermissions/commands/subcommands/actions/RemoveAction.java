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

import java.io.IOException;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.lang.Lang;
import net.ae97.totalpermissions.permission.PermissionBase;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class RemoveAction extends SubAction {

    protected final TotalPermissions plugin;

    public RemoveAction(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);

        if (field.equalsIgnoreCase("permissions")) {
            try {
                tar.remPerm(item, world);
                sender.sendMessage(Lang.COMMAND_ACTION_REMOVE_PERMISSIONS.getMessage(item, target));
                return true;
            } catch (IOException ex) {
                saveError(plugin, tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("inheritance")) {
            try {
                tar.remInheritance(item, world);
                sender.sendMessage(Lang.COMMAND_ACTION_REMOVE_INHERITANCE.getMessage(item, target));
                return true;
            } catch (IOException ex) {
                saveError(plugin, tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("commands")) {
            try {
                tar.remCommand(item, world);
                sender.sendMessage(Lang.COMMAND_ACTION_REMOVE_COMMANDS.getMessage(item, target));
                return true;
            } catch (IOException ex) {
                saveError(plugin, tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("groups")) {
            try {
                tar.remGroup(item, world);
                sender.sendMessage(Lang.COMMAND_ACTION_REMOVE_GROUPS.getMessage(item, target));
                return true;
            } catch (IOException ex) {
                saveError(plugin, tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("prefix")) {
            try {
                tar.remOption("prefix", world);
                sender.sendMessage(Lang.COMMAND_ACTION_REMOVE_PREFIX.getMessage(target));
                return true;
            } catch (IOException ex) {
                saveError(plugin, tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("suffix")) {
            try {
                tar.remOption("suffix", world);
                sender.sendMessage(Lang.COMMAND_ACTION_REMOVE_SUFFIX.getMessage(target));
                return true;
            } catch (IOException ex) {
                saveError(plugin, tar, sender, ex);
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "remove " + Lang.VARIABLES_FIELD.getMessage()
            + " " + Lang.VARIABLES_VALUE.getMessage()
            + " " + Lang.VARIABLES_WORLDOPTIONAL.getMessage(),
            Lang.COMMAND_ACTION_REMOVE_HELP.getMessage()
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
}
