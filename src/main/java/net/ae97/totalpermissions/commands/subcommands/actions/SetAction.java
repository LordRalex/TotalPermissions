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
import net.ae97.totalpermissions.permission.PermissionGroup;
import net.ae97.totalpermissions.permission.PermissionType;
import java.io.IOException;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class SetAction extends SubAction {
    
    private final TotalPermissions plugin;
    
    public SetAction(TotalPermissions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        if (field.equalsIgnoreCase("default")) {
            if (tar instanceof PermissionGroup) {
                try {
                    this.plugin.getManager().changeDefaultGroup(tar.getName());
                    sender.sendMessage(this.plugin.getLangFile().getString("command.action.set.default", target));
                } catch (IOException ex) {
                    saveError(this.plugin, tar, sender, ex);
                }
            }
        } else if (field.equalsIgnoreCase("prefix")) {
            tar.setOption("prefix", item, world);
            sender.sendMessage(this.plugin.getLangFile().getString("command.action.set.prefix", target, item));
            return true;
        } else if (field.equalsIgnoreCase("suffix")) {
            tar.setOption("suffix", item, world);
            sender.sendMessage(this.plugin.getLangFile().getString("command.action.set.suffix", target, item));
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "set " + this.plugin.getLangFile().getString("variables.field")
            + " " + this.plugin.getLangFile().getString("variables.value")
            + " " + this.plugin.getLangFile().getString("variables.world-optional"),
            this.plugin.getLangFile().getString("command.action.set.help")
        };
    }

    @Override
    public String[] supportedTypes() {
        return new String[]{
            "default",
            "prefix",
            "suffix"
        };
    }
}
