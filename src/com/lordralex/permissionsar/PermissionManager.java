package com.lordralex.permissionsar;

import org.bukkit.entity.Player;

/**
 * @version 1.0
 * @author Joshua
 */
public class PermissionManager {

    private static PermissionsAR plugin;

    public PermissionManager(PermissionsAR aP)
    {
        plugin = aP;
    }

    public static PermissionUser getUser(String player)
    {
        return PermissionUser.loadUser(player);
    }

    public static PermissionUser getUser(Player player)
    {
        return getUser(player.getName());
    }

    public static PermissionsAR getPlugin()
    {
        return plugin;
    }

    public static PermissionGroup getGroup(String name)
    {
        return PermissionGroup.loadGroup(name);
    }
}
