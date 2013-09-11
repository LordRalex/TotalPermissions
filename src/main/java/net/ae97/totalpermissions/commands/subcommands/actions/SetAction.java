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
import java.util.List;
import net.ae97.totalpermissions.permission.PermissionUser;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class SetAction extends SubAction {

    protected final TotalPermissions plugin;

    public SetAction(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String aType, String target, String field, String item, String world) {
        PermissionType type = PermissionType.getType(aType);
        PermissionBase tar = PermissionType.getTarget(type, target);
        if (field.equalsIgnoreCase("default")) {
            if (tar instanceof PermissionGroup) {
                try {
                    plugin.getManager().changeDefaultGroup(tar.getName());
                    sender.sendMessage(plugin.getLangFile().getString("command.action.set.default", target));
                    return true;
                } catch (IOException ex) {
                    saveError(plugin, tar, sender, ex);
                }
            }
        } else if (field.equalsIgnoreCase("group")) {
            if (tar instanceof PermissionUser) {
                PermissionUser utar = (PermissionUser) tar;
                try {
                    List<String> groups = utar.getGroups(world);
                    utar.addGroup(item, world);
                    for (String group : groups) {
                        if (!group.equalsIgnoreCase(item)) {
                            utar.remGroup(group, world);
                        }
                    }
                    //TODO: Make a lang string for setting groups
                    sender.sendMessage(plugin.getLangFile().getString("command.action.add.groups", item, target));
                    return true;
                } catch (IOException ex) {
                    saveError(plugin, tar, sender, ex);
                }
            }

        } else if (field.equalsIgnoreCase("prefix")) {
            tar.setOption("prefix", item, world);
            sender.sendMessage(plugin.getLangFile().getString("command.action.set.prefix", target, item));
            return true;
        } else if (field.equalsIgnoreCase("suffix")) {
            tar.setOption("suffix", item, world);
            sender.sendMessage(plugin.getLangFile().getString("command.action.set.suffix", target, item));
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
            "set " + plugin.getLangFile().getString("variables.field")
            + " " + plugin.getLangFile().getString("variables.value")
            + " " + plugin.getLangFile().getString("variables.world-optional"),
            plugin.getLangFile().getString("command.action.set.help")
        };
    }

    @Override
    public String[] supportedTypes() {
        return new String[]{
            "default",
            "prefix",
            "suffix",
            "group"
        };
    }
}
