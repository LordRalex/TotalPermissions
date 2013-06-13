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
public class CheckAction extends SubAction {

    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        if (field.equalsIgnoreCase("permissions")) {
            if (tar.has(item, world)) {
                sender.sendMessage(target + " has '" + item + "'!");
            }
            else {
                sender.sendMessage(target + " does NOT have '" + item + "'!");
            }
            return true;
        }
        else if (field.equalsIgnoreCase("inheritance")) {
            if (tar.hasInheritance(item, world)) {
                sender.sendMessage(target + " inherits '" + item + "'!");
            }
            else {
                sender.sendMessage(target + " does NOT inherit '" + item + "'!");
            }
            return true;
        }
        else if (field.equalsIgnoreCase("commands")) {
            if (tar.hasCommand(item, world)) {
                sender.sendMessage(target + " has '" + item + "'!");
            }
            else {
                sender.sendMessage(target + " does NOT have '" + item + "'!");
            }
            return true;
        }
        else if (field.equalsIgnoreCase("groups")) {
            if (tar instanceof PermissionUser) {
                PermissionUser newtar = (PermissionUser)tar;
                if (newtar.inherits(item, world)) {
                    sender.sendMessage(target + " is in group '" + item + "'!");
                }
                else {
                    sender.sendMessage(target + " is NOT in group '" + item + "'!");
                }
                return true;
            }
            return false;
        }
        //Check if the provided area has the right value within it. Not for listing
            //Wut, I don't even know what I wrote here.
        return false;
    }

    public String getName() {
        return "check";
    }

    public String[] getHelp() {
        return new String[]{
            "check <field> <value>",
            "Checks if a given target's field has the provided value"
        };
    }
    
    public String[] supportedTypes() {
        return new String[] {
            "permissions",
            "inheritance",
            "commands",
            "groups",
        };
    }
}