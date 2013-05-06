/*
 * Copyright (C) 2013 Laptop
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

/**
 * @since 0.2
 * @author Lord_Ralex
 * @version 0.2
 */
public enum PermissionType {

    GROUP("group"),
    USER("user"),
    SPECIAL("special"),
    WORLD("world"),
    PERMISSIONS("permissions"),
    INHERITENCE("inheritence"),
    GROUPS("groups"),
    OPTIONS("options");
    private final String name;

    private PermissionType(String aName) {
        name = aName;
    }

    public static PermissionType getType(String aType) {
        for (PermissionType type : PermissionType.values()) {
            if (type.name.equalsIgnoreCase(aType)) {
                return type;
            }
        }
        return null;
    }
}
