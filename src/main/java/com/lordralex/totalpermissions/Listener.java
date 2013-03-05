package com.lordralex.totalpermissions;

import com.lordralex.totalpermissions.permission.PermissionUser;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public final class Listener implements org.bukkit.event.Listener {

    TotalPermissions plugin;

    public Listener(TotalPermissions p) {
        plugin = p;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        //Adds a player to our cache and set up their permissions
        PermissionUser user = plugin.getManager().getUser(event.getPlayer());
        user.setPerms(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        //removes permissions from a player, leaves them in the cache though
        Player player = event.getPlayer();
        player.removeAttachment(plugin.getManager().getUser(player).getAtt());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        PermissionUser user = plugin.getManager().getUser(player);
        user.changeWorld(player.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        PermissionUser user = plugin.getManager().getUser(event.getPlayer());
        if (!user.getDebugState()) {
            return;
        }
        plugin.getLogger().info("Command used by " + event.getPlayer().getName() + ": " + event.getMessage());
        try {
            String command = event.getMessage().split(" ", 2)[0].substring(1);
            Command cmd = Bukkit.getPluginCommand(command);
            if (cmd.testPermissionSilent(event.getPlayer())) {
                plugin.getLogger().info(event.getPlayer().getName() + " can use the command, has " + cmd.getPermission());
            } else {
                plugin.getLogger().info(event.getPlayer().getName() + " cannot use the command, does not have " + cmd.getPermission());
            }
        } catch (NullPointerException e) {
            plugin.getLogger().log(Level.SEVERE, "The command used is not a registered command", e);
        } catch (IndexOutOfBoundsException e) {
            plugin.getLogger().log(Level.SEVERE, event.getMessage() + " produced an IOoBE", e);
        }
    }
}
