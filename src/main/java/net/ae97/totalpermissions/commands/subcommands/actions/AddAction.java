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
import java.io.IOException;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class AddAction extends SubAction {

    @Override
    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        if (field.equalsIgnoreCase("permissions")) {
            try {
                tar.addPerm(item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.add.permissions", item, target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("inheritance")) {
            try {
                tar.addInheritance(item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.add.inheritance", item, target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("commands")) {
            try {
                tar.addCommand(item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.add.commands", item, target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("groups")) {
            try {
                tar.addGroup(item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.add.groups", item, target));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "add " + TotalPermissions.getPlugin().getLangFile().getString("variables.field")
            + " " + TotalPermissions.getPlugin().getLangFile().getString("variables.value")
            + " " + TotalPermissions.getPlugin().getLangFile().getString("variables.world-optional"),
            TotalPermissions.getPlugin().getLangFile().getString("command.action.add.help")
        };
    }

    @Override
    public String[] supportedTypes() {
        return new String[]{
            "permissions",
            "inheritance",
            "commands",
            "groups"
        };
    }
}
