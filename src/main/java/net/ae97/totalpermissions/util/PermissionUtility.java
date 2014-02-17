/*
 * Copyright (C) 2014 AE97
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
package net.ae97.totalpermissions.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * @author Lord_Ralex1
 */
public class PermissionUtility {

    public static List<String> handleWildcard() {
        return handleWildcard(false);
    }

    public static List<String> handleWildcard(boolean isAll) {
        List<String> perms = new ArrayList<String>();
        Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
        for (Permission permTest : permT) {
            if (permTest.getName().startsWith("totalpermissions.")) {
                continue;
            } else if (permTest.getDefault() != PermissionDefault.FALSE) {
                perms.add(permTest.getName());
            } else if (isAll) {
                perms.add(permTest.getName());
            }
        }
        return perms;
    }
}
