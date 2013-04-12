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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 */
public final class PermissionUser extends PermissionBase {

    private boolean isDebug = false;

    public PermissionUser(String aName) {
        super("users", aName);
    }

    /**
     * Creates a new PermissionUser with the given name. This will load all the
     * values.
     *
     * @param player The name of the user
     *
     * @since 1.0
     */
    public PermissionUser(Player player) {
        this(player.getName());
    }

    public void setDebug(boolean newState) {
        isDebug = newState;
    }

    public boolean getDebugState() {
        return isDebug;
    }

    public void changeWorld(Player cs, String name) {
        setPerms((CommandSender)cs, name);
    }
}
