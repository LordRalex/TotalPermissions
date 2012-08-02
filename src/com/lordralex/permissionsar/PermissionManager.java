package com.lordralex.permissionsar;

import com.lordralex.permissionsar.permission.PermissionGroup;
import com.lordralex.permissionsar.permission.PermissionUser;
import org.bukkit.entity.Player;

/**
 * @version 1.0
 * @author Joshua
 */
public final class PermissionManager {

    private static PermissionsAR plugin;

    public PermissionManager(PermissionsAR aP)
    {
        plugin = aP;
    }

    public PermissionUser getUser(String player)
    {
        return PermissionUser.loadUser(player);
    }

    public PermissionUser getUser(Player player)
    {
        return getUser(player.getName());
    }

    public PermissionsAR getPlugin()
    {
        return plugin;
    }

    public PermissionGroup getGroup(String name)
    {
        return PermissionGroup.loadGroup(name);
    }
}
