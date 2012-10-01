package com.lordralex.permissionsar;

import com.lordralex.permissionsar.permission.PermissionUser;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public final class Listener implements org.bukkit.event.Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        //Adds a player to our cache and set up their permissions
        PermissionUser user = PermissionsAR.getManager().getUser(event.getPlayer());
        user.setPerms(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        //removes permissions from a player, leaves them in the cache though
        Player player = event.getPlayer();
        player.removeAttachment(PermissionsAR.getManager().getUser(player).getAtt());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerKickEvent event) {
        //removes permissions from a player, leaves them in the cache though
        Player player = event.getPlayer();
        player.removeAttachment(PermissionsAR.getManager().getUser(player).getAtt());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().startsWith("/")) {
            return;
        }
        PermissionUser user = PermissionsAR.getManager().getUser(event.getPlayer());
        if (!user.getDebugState()) {
            return;
        }
        try {
            String command = null;
            try {
                command = event.getMessage().split(" ", 2)[0].substring(1);
                Bukkit.getPluginCommand(command).testPermissionSilent(event.getPlayer());
            } catch (NullPointerException e) {
                PermissionsAR.getLog().log(Level.CONFIG, command + " is not a registered command");
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }
}