package com.lordralex.permissionsar;

import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * @version 1.0
 * @author Joshua
 */
public class Listener implements org.bukkit.event.Listener{

    @EventHandler (priority=EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        PermissionUser user = PermissionUser.loadUser(event.getPlayer().getName());
        user.setPerms(event.getPlayer());
    }

}
