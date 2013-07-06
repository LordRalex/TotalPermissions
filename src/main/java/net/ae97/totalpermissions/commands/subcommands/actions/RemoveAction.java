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

import java.io.IOException;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionBase;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class RemoveAction extends SubAction {

    @Override
    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);

        if (field.equalsIgnoreCase("permissions")) {
            try {
                tar.remPerm(item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.remove.permissions", item, target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("inheritance")) {
            try {
                tar.remInheritance(item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.remove.inheritance", item, target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("commands")) {
            try {
                tar.remCommand(item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.remove.commands", item, target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("groups")) {
            try {
                tar.remGroup(item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.remove.groups", item, target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("prefix")) {
            try {
                tar.remOption("prefix", world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.remove.prefix", target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("suffix")) {
            try {
                tar.remOption("suffix", world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.remove.suffix", target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
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
            "remove " + TotalPermissions.getPlugin().getLangFile().getString("variables.field")
            + " " + TotalPermissions.getPlugin().getLangFile().getString("variables.value")
            + " " + TotalPermissions.getPlugin().getLangFile().getString("variables.world-optional"),
            TotalPermissions.getPlugin().getLangFile().getString("command.action.remove.help")
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
