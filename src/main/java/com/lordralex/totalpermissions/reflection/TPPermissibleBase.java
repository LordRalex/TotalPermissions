/*
 * Copyright (C) 2013 Lord_Ralex
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
package com.lordralex.totalpermissions.reflection;

import com.lordralex.totalpermissions.TotalPermissions;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

/**
 * @author Lord_Ralex
 * @version 0.1
 * @since 0.1
 */
public class TPPermissibleBase extends PermissibleBase {

    protected Permissible initialParent;

    public TPPermissibleBase(CommandSender parent) {
        super(parent);
        initialParent = parent;
    }

    @Override
    public boolean hasPermission(Permission perm) {

        boolean has = super.hasPermission(perm);
        if (initialParent instanceof CommandSender) {
            CommandSender cs = (CommandSender) initialParent;
            TotalPermissions.getPlugin().getLogger().info("Checking if " + cs.getName() + " has " + perm.getName() + ": " + has);
        } else {
            TotalPermissions.getPlugin().getLogger().info("Checking for " + perm.getName() + ": " + has);
        }
        return has;
    }

    @Override
    public boolean hasPermission(String perm) {
        boolean has = super.hasPermission(perm);
        if (initialParent instanceof CommandSender) {
            CommandSender cs = (CommandSender) initialParent;
            TotalPermissions.getPlugin().getLogger().info("Checking if " + cs.getName() + " has " + perm + ": " + has);
        } else {
            TotalPermissions.getPlugin().getLogger().info("Checking for " + perm + ": " + has);
        }
        return has;
    }
}
