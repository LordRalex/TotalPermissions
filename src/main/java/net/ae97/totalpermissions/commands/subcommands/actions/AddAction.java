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
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionBase;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class AddAction implements SubAction {

    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        if (field.equalsIgnoreCase("permission")) {
            try {
                tar.addPerm(item, world);
                sender.sendMessage("Permission '" + item + "' added for " + target + "!");
            } catch (IOException ex) {
                sender.sendMessage(ChatColor.RED + "An error occured while saving the changes.");
                sender.sendMessage(ChatColor.RED + "The changes should be applied but were not saved to the file");
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "An error occured while saving " + tar.getType() + "." + tar.getName(), ex);
            }
        } else if (field.equalsIgnoreCase("inheritance")) {
            try {
                tar.addInheritance(item, world);
                sender.sendMessage("Inheritance '" + item + "' added for " + target + "!");
                return true;
            } catch (IOException ex) {
                sender.sendMessage(ChatColor.RED + "An error occured while saving the changes.");
                sender.sendMessage(ChatColor.RED + "The changes should be applied but were not saved to the file");
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "An error occured while saving " + tar.getType() + "." + tar.getName(), ex);
            }
        } else if (field.equalsIgnoreCase("command")) {
            try {
                tar.addCommand(item, world);
                sender.sendMessage("Command '" + item + "' added for " + target + "!");
                return true;
            } catch (IOException ex) {
                sender.sendMessage(ChatColor.RED + "An error occured while saving the changes.");
                sender.sendMessage(ChatColor.RED + "The changes should be applied but were not saved to the file");
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "An error occured while saving " + tar.getType() + "." + tar.getName(), ex);
            }
        } else if (field.equalsIgnoreCase("group")) {
            try {
                tar.addGroup(item, world);
                sender.sendMessage("Group '" + item + "' added for " + target + "!");
                return true;
            } catch (IOException ex) {
                sender.sendMessage(ChatColor.RED + "An error occured while saving the changes.");
                sender.sendMessage(ChatColor.RED + "The changes should be applied but were not saved to the file");
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "An error occured while saving " + tar.getType() + "." + tar.getName(), ex);
            }
        }
        return false;
    }

    public String getName() {
        return "add";
    }

    public String[] getHelp() {
        return new String[]{
            "add <field> <value>",
            "Adds a value to a field"
        };
    }

    public String[] supportedTypes() {
        return new String[]{
            "permission",
            "inheritance",
            "command",
            "group"
        };
    }
}
