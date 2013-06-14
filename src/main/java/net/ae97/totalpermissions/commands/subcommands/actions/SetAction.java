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
import net.ae97.totalpermissions.permission.PermissionGroup;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class SetAction extends SubAction {

    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        if (field.equalsIgnoreCase("default")) {
            if (tar instanceof PermissionGroup) {
                try {
                    TotalPermissions.getPlugin().getManager().changeDefaultGroup(tar.getName());
                    sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.set.default", target));
                } catch (IOException ex) {
                    saveError(tar, sender, ex);
                }
            }
        } else if (field.equalsIgnoreCase("prefix")) {
            try {
                tar.setOption("prefix", item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.set.prefix", target, item));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        } else if (field.equalsIgnoreCase("suffix")) {
            try {
                tar.setOption("suffix", item, world);
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.set.suffix", target, item));
                return true;
            } catch (IOException ex) {
                saveError(tar, sender, ex);
            }
        }
        return false;
    }

    public String getName() {
        return "set";
    }

    public String[] getHelp() {
        return new String[]{
            "set " + TotalPermissions.getPlugin().getLangFile().getString("variables.field")
            + " " + TotalPermissions.getPlugin().getLangFile().getString("variables.value")
            + " " + TotalPermissions.getPlugin().getLangFile().getString("variables.world-optional"),
            TotalPermissions.getPlugin().getLangFile().getString("command.action.set.help")
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
