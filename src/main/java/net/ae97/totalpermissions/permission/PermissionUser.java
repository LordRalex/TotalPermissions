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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 */
public final class PermissionUser extends PermissionBase {

    private boolean isDebug = false;

    public PermissionUser(String aName) {
        super(PermissionType.USERS, aName);
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

    public void changeWorld(Player cs, String name, PermissionAttachment att) {
        setPerms((CommandSender) cs, att, name);
    }

    /**
     * Checks if the PermissionUser inherits from the specific group.
     *
     * @param group The group to check
     * @param world The world to check with, null if global
     *
     * @return Whether or not the user is in the group
     */
    public boolean inherits(String group, String world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Gets all the groups a user is in.
     *
     * @param world The world to check with, null if global
     *
     * @return All the groups the user is in
     */
    public List<String> getGroups(String world) {
        Set<String> groupList = new HashSet<String>();
        List<String> temp;
        temp = section.getStringList(((world == null) ? "" : "worlds." + world + ".") + "group");
        if (temp != null) {
            groupList.addAll(temp);
        }
        temp = section.getStringList(((world == null) ? "" : "worlds." + world + ".") + "groups");
        if (temp != null) {
            groupList.addAll(temp);
        }
        List<String> list = new ArrayList<String>(groupList.size());
        list.addAll(temp);
        return list;
    }
}
