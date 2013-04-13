/*
 * Copyright (C) 2013 LordRalex
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
package com.lordralex.totalpermissions.permission;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 */
public class PermissionGroup extends PermissionBase {

    protected boolean isDefault = false;

    /**
     * Creates a new PermissionGroup with the given name.
     *
     * @param name The name of the group
     *
     * @since 1.0
     */
    public PermissionGroup(String name) {
        super("groups", name);
        if (name.equalsIgnoreCase("default")) {
            isDefault = true;
        }
        if (!isDefault) {
            if (section != null) {
                if (section.getBoolean("default", false) || section.getBoolean("options.default", false)) {
                    isDefault = true;
                }
            }
        }
    }

    /**
     * Checks to see if this group is the default. If so, this returns true,
     * otherwise false.
     *
     * @return True if this group is the default group, otherwise false
     */
    public boolean isDefault() {
        return isDefault;
    }
}
