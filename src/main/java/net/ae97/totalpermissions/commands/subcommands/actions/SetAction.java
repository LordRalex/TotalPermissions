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

import java.io.IOException;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionBase;
import net.ae97.totalpermissions.permission.PermissionGroup;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class SetAction implements SubAction {

    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        if (field.equalsIgnoreCase("default")) {
            if (tar instanceof PermissionGroup) {
                try {
                    TotalPermissions.getPlugin().getManager().changeDefaultGroup(tar.getName());
                } catch (IOException ex) {
                    sender.sendMessage(ChatColor.RED + "An error occured while saving the changes.");
                    sender.sendMessage(ChatColor.RED + "The changes should be applied but were not saved to the file");
                    TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "An error occured while saving " + tar.getType() + "." + tar.getName(), ex);
                }
            }
        } else if (field.equalsIgnoreCase("prefix")) {
            try {
                tar.setOption("prefix", item, world);
                return true;
            } catch (IOException ex) {
                sender.sendMessage(ChatColor.RED + "An error occured while saving the changes.");
                sender.sendMessage(ChatColor.RED + "The changes should be applied but were not saved to the file");
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "An error occured while saving " + tar.getType() + "." + tar.getName(), ex);
            }
        } else if (field.equalsIgnoreCase("suffix")) {
            try {
                tar.setOption("suffix", item, world);
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
        return "set";
    }

    public String[] getHelp() {
        return new String[]{
            "set <field> <value>",
            ""
        };
    }

    public String[] supportedTypes() {
        return new String[]{
            "default",
            "prefix",
            "suffix"
        };
    }
}
