package com.lordralex.permissionsar;

import com.lordralex.permissionsar.permission.PermissionUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @version 1.0
 * @author Joshua
 */
public final class Listener implements org.bukkit.event.Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        PermissionUser user = PermissionUser.loadUser(event.getPlayer().getName());
        user.setPerms(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.removeAttachment(PermissionsAR.getManager().getUser(player).getAtt());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerKickEvent event) {
        Player player = event.getPlayer();
        player.removeAttachment(PermissionsAR.getManager().getUser(player).getAtt());
    }
}
