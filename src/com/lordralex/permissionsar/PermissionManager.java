package com.lordralex.permissionsar;

import com.lordralex.permissionsar.permission.PermissionGroup;
import com.lordralex.permissionsar.permission.PermissionUser;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public final class PermissionManager {

    private Map<String, PermissionGroup> groups = new HashMap<String, PermissionGroup>();
    private Map<String, PermissionUser> users = new HashMap<String, PermissionUser>();

    /**
     * Loads a player's {@link PermissionUser} object. This can return the one
     * that is stored in the cache if the player is online or has joined and
     * went offline.
     *
     * @param player The player's name to load
     * @return The {@link PermissionUser} for that player
     *
     * @since 1.0
     */
    public PermissionUser getUser(String player) {
        PermissionUser user = new PermissionUser(player);
        users.put(player.toLowerCase(), user);
        return user;
    }

    /**
     * Loads a player's {@link PermissionUser} object. This can return the one
     * that is stored in the cache if the player is online or has joined and
     * went offline.
     *
     * @param player The player's name to load
     * @return The {@link PermissionUser} for that player
     *
     * @since 1.0
     */
    public PermissionUser getUser(Player player) {
        return getUser(player.getName());
    }

    /**
     * Loads a group's {@link PermissionGroup} object. This can return the one
     * that is stored in the cache if there is one there.
     *
     * @param name The player's name to load
     * @return The {@link PermissionGroup} for that group
     *
     * @since 1.0
     */
    public PermissionGroup getGroup(String name) {
        PermissionGroup group = new PermissionGroup(name);
        groups.put(name.toLowerCase(), group);
        return group;
    }

    /**
     * Checks to see if a player has a specific permission.
     *
     * @param player Player to check
     * @param node Node
     * @return True if player has the perm, false otherwise
     */
    public boolean has(Player player, String node) {
        return false;
    }

    /**
     * Checks to see if a player has a specific permission.
     *
     * @param name Name of player to check
     * @param node Node
     * @return True if player has the perm, false otherwise
     */
    public boolean has(String name, String node) {
        return false;
    }
}
