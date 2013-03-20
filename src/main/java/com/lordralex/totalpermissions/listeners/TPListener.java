/*
 * Copyright (C) 2013 LordRalex
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
package com.lordralex.totalpermissions.listeners;

import com.lordralex.totalpermissions.TotalPermissions;
import com.lordralex.totalpermissions.permission.PermissionUser;
import com.lordralex.totalpermissions.reflection.TPPermissibleBase;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.permissions.PermissionAttachment;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 */
public class TPListener implements org.bukkit.event.Listener {

    TotalPermissions plugin;

    public TPListener(TotalPermissions p) {
        plugin = p;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        PermissionUser user = plugin.getManager().getUser(event.getPlayer());
        if (user.getDebugState()) {
            //forgive me for saying I did not want to do this
            //or as squid says
            //"Forgive me for my sins"
            Player player = event.getPlayer();
            try {
                Class cl = Class.forName("org.bukkit.craftbukkit.v1_4_R1.entity.CraftHumanEntity");
                Field field = cl.getDeclaredField("perm");
                field.setAccessible(true);
                field.set(player, new TPPermissibleBase(event.getPlayer()));
                plugin.getLogger().info("Reflection hook established");
            } catch (NoSuchFieldException ex) {
                plugin.getLogger().log(Level.SEVERE, "Error in reflecting in", ex);
            } catch (SecurityException ex) {
                plugin.getLogger().log(Level.SEVERE, "Error in reflecting in", ex);
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().log(Level.SEVERE, "Error in reflecting in", ex);
            } catch (IllegalAccessException ex) {
                plugin.getLogger().log(Level.SEVERE, "Error in reflecting in", ex);
            } catch (ClassNotFoundException ex) {
                plugin.getLogger().log(Level.SEVERE, "Error in reflecting in", ex);
            }
        }
        user.setPerms(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PermissionUser user = plugin.getManager().getUser(player);
        user.changeWorld(player.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        PermissionUser user = plugin.getManager().getUser(player);
        user.changeWorld(player.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
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
            plugin.getLogger().log(Level.SEVERE, "The command used (" + event.getMessage() + ") is not a registered command");
        } catch (IndexOutOfBoundsException e) {
            plugin.getLogger().log(Level.SEVERE, event.getMessage() + " produced an IOoBE");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRemoteConsoleEvent(RemoteServerCommandEvent event) {
        CommandSender sender = event.getSender();
        PermissionAttachment att = sender.addAttachment(plugin);
        List<String> perms = plugin.getManager().getRcon().getPerms();
        for (String perm : perms) {
            if (perm.startsWith("-")) {
                att.setPermission(perm.substring(1).trim(), false);
            } else {
                att.setPermission(perm, true);
            }
        }
    }
}
