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
package net.ae97.totalpermissions.api;

import net.ae97.totalpermissions.TotalPermissions;

/**
 *
 * @since @author 1Rogue
 * @version
 */
public class ExternalAPI {

    public String getName() {
        return TotalPermissions.getPlugin().getName();
    }

    public boolean isEnabled() {
        return TotalPermissions.getPlugin().isEnabled();
    }

    public boolean playerHas(String world, String player, String permission) {
        //TODO: Add world support
        if (TotalPermissions.getPlugin().getManager().has(player, permission)) {
            return true;
        }
        return false;
    }

    public boolean playerAdd(String world, String player, String permission) {
        //TODO: Add world support and return false if not possible
        if (permission.startsWith("-")) {
            TotalPermissions.getPlugin().getManager().addPermToUser(player, permission, false);
            return true;
        }
        TotalPermissions.getPlugin().getManager().addPermToUser(player, permission, true);
        return true;
    }

    public boolean playerRemove(String world, String player, String permission) {
        //TotalPermissions.getPlugin().getManager();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean groupHas(String world, String group, String permission) {
        if (TotalPermissions.getPlugin().getManager().groupHas(group, permission)) {
            return true;
        }
        return false;
    }

    public boolean groupAdd(String world, String group, String permission) {
        if (permission.startsWith("-")) {
            TotalPermissions.getPlugin().getManager().addPermToGroup(group, permission, false);
            return true;
        }
        TotalPermissions.getPlugin().getManager().addPermToGroup(group, permission, true);
        return true;
    }

    public boolean groupRemove(String world, String group, String permission) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean playerInGroup(String world, String player, String group) {
        //Check if a player is in a group.
        //TotalPermissions.getPlugin().getManager().getUser(player).
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean playerAddGroup(String world, String player, String group) {
        //TotalPermissions.getPlugin().getManager().getUser(player).
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean playerRemoveGroup(String world, String player, String group) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] getPlayerGroups(String world, String player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPrimaryGroup(String world, String player) {
        return TotalPermissions.getPlugin().getManager().getDefaultGroup();
    }

    public String[] getGroups() {
        //return TotalPermissions.getPlugin().getManager().getGroups();
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
