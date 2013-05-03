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
package net.ae97.totalpermissions.permission;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.2
 */
public class PermissionOp extends PermissionBase {

    public PermissionOp() {
        super("special", "console");
        Map<String, Boolean> permMap = this.getPerms();
        for (String perm : permMap.keySet()) {
            Permission p = Bukkit.getPluginManager().getPermission(perm);
            if (p == null) {
                continue;
            } else {
                if (permMap.get(perm) == Boolean.TRUE) {
                    p.setDefault(PermissionDefault.OP);
                } else {
                    p.setDefault(PermissionDefault.FALSE);
                }
            }
        }
    }
}
