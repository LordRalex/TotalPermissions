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
public class RemoveAction implements SubAction {

    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);

        if (field.equalsIgnoreCase("permission")) {
            tar.remPerm(item, world);
            sender.sendMessage("Permission " + item + " removed from " + target + "!");
            return true;
        } else if (field.equalsIgnoreCase("inheritance")) {
            tar.remInheritance(item, world);
            sender.sendMessage("Inheritance " + item + " removed from " + target + "!");
            return true;
        } else if (field.equalsIgnoreCase("command")) {
            tar.remCommand(item, world);
            sender.sendMessage("Command " + item + " removed from " + target + "!");
            return true;
        } else if (field.equalsIgnoreCase("group")) {
            tar.remGroup(item, world);
            sender.sendMessage(target + " removed from group " + item + "!");
            return true;
        } else if (field.equalsIgnoreCase("prefix")) {
            tar.remOption("prefix", world);
            sender.sendMessage("Prefix removed from " + target + "!");
            return true;
        } else if (field.equalsIgnoreCase("suffix")) {
            tar.remOption("suffix", world);
            sender.sendMessage("Suffix removed from " + target + "!");
            return true;
        }

        // Removes from a certain field (or deletes the prefix/suffix)
        // Don't forget to catch for last group being removed, set default
        return false;
    }

    public String getName() {
        return "remove";
    }

    public String[] getHelp() {
        return new String[]{
            "remove <field> <value>",
            "Removes a value from a field"
        };
    }

    public String[] supportedTypes() {
        return new String[]{
            "permission",
            "inheritance",
            "command",
            "group",
            "prefix",
            "suffix"
        };
    }
}