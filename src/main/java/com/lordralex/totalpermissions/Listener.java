package com.lordralex.totalpermissions;

import com.lordralex.totalpermissions.permission.PermissionUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
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
        PermissionUser user = TotalPermissions.getManager().getUser(event.getPlayer());
        user.setPerms(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        //removes permissions from a player, leaves them in the cache though
        Player player = event.getPlayer();
        player.removeAttachment(TotalPermissions.getManager().getUser(player).getAtt());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerKickEvent event) {
        //removes permissions from a player, leaves them in the cache though
        Player player = event.getPlayer();
        player.removeAttachment(TotalPermissions.getManager().getUser(player).getAtt());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        PermissionUser user = TotalPermissions.getManager().getUser(player);
        user.changeWorld(player.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        PermissionUser user = TotalPermissions.getManager().getUser(event.getPlayer());
        if (!user.getDebugState()) {
            return;
        }
        try {
            String command = null;
            try {
                command = event.getMessage().split(" ", 2)[0].substring(1);
                Command cmd = Bukkit.getPluginCommand(command);
                if (cmd.testPermissionSilent(event.getPlayer())) {
                    TotalPermissions.getLog().info(event.getPlayer().getName() + " can use the command, has " + cmd.getPermission());
                } else {
                    TotalPermissions.getLog().info(event.getPlayer().getName() + " cannot use the command, does not have " + cmd.getPermission());
                }
            } catch (NullPointerException e) {
                TotalPermissions.getLog().info(command + " is not a registered command");
            }
        } catch (IndexOutOfBoundsException e) {
            TotalPermissions.getLog().info(event.getMessage() + " produced an IOoBE");
        }
    }
}