/*
 * Copyright (C) 2014 AE97
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
package net.ae97.totalpermissions.listeners;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.lang.Lang;
import net.ae97.totalpermissions.permission.PermissionType;
import net.ae97.totalpermissions.permission.PermissionUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 */
public class PlayerListener implements Listener {

    protected final TotalPermissions plugin;

    public PlayerListener(TotalPermissions p) {
        plugin = p;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        try {
            plugin.getDataHolder().load(PermissionType.USER, event.getName());
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        } catch (InvalidConfigurationException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        try {
            plugin.debugLog("PlayerLoginEvent fired, handling");
            plugin.getManager().handleLoginEvent(event);
        } catch (StackOverflowError e) {
            plugin.getLogger().log(Level.SEVERE,
                    "Huge error on logging in player {0} during PlayerLoginEvent",
                    event.getPlayer().getName());
            StackTraceElement[] elementsInit = e.getStackTrace();
            plugin.getLogger().severe(e.getMessage());
            plugin.getLogger().severe("The debug file will contain the full stacktrace");
            for (StackTraceElement elementsInit1 : elementsInit) {
                plugin.debugLog(elementsInit1.toString());
            }
            Throwable cause = e.getCause();
            while (cause != null) {
                plugin.debugLog("Caused by: " + cause.getMessage());
                StackTraceElement[] elementsOrig = cause.getStackTrace();
                for (StackTraceElement elementsOrig1 : elementsOrig) {
                    plugin.debugLog(elementsOrig1.toString());
                }
                cause = cause.getCause();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        try {
            plugin.debugLog("PlayerJoinEvent-Lowest fired, handling");
            Player player = event.getPlayer();
            PermissionUser user = plugin.getManager().getUser(player);
            user.changeWorld(player, player.getWorld().getName(), plugin.getManager().getAttachment(player));
        } catch (StackOverflowError e) {
            plugin.getLogger().log(Level.SEVERE,
                    "Huge error on logging in player {0} during PlayerJoinEvent",
                    event.getPlayer().getName());
            StackTraceElement[] elementsInit = e.getStackTrace();
            plugin.getLogger().severe(e.getMessage());
            plugin.getLogger().severe("The debug file will contain the full stacktrace");
            for (StackTraceElement elementsInit1 : elementsInit) {
                plugin.debugLog(elementsInit1.toString());
            }
            Throwable cause = e.getCause();
            while (cause != null) {
                plugin.debugLog("Caused by: " + cause.getMessage());
                StackTraceElement[] elementsOrig = cause.getStackTrace();
                for (StackTraceElement elementsOrig1 : elementsOrig) {
                    plugin.debugLog(elementsOrig1.toString());
                }
                cause = cause.getCause();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEventMonitor(PlayerJoinEvent event) {
        try {
            plugin.debugLog("PlayerJoinEvent-Monitor fired, handling");
            Player player = event.getPlayer();
            PermissionUser user = plugin.getManager().getUser(player);
            user.changeWorld(player, player.getWorld().getName(), plugin.getManager().getAttachment(player));
            if (plugin.isDebugMode()) {
                plugin.debugLog(event.getPlayer().getName() + " has joined in world " + event.getPlayer().getWorld().getName());
                Set<PermissionAttachmentInfo> set = event.getPlayer().getEffectivePermissions();
                plugin.debugLog("Player: " + event.getPlayer().getName());
                for (PermissionAttachmentInfo perm : set) {
                    plugin.debugLog(" PermAttInfo: " + perm.getPermission());
                    PermissionAttachment att = perm.getAttachment();
                    if (att != null) {
                        Map<String, Boolean> map = att.getPermissions();
                        plugin.debugLog("  PermAtt: " + att.toString());
                        plugin.debugLog("  PermAttMap:");
                        for (String key : map.keySet()) {
                            plugin.debugLog("   - " + key + ": " + map.get(key));
                            Permission pe = Bukkit.getPluginManager().getPermission(key);
                            if (pe != null) {
                                for (String k : pe.getChildren().keySet()) {
                                    plugin.debugLog("     - " + k + ": " + pe.getChildren().get(k));
                                }
                            }
                        }
                    }
                }
            }
        } catch (StackOverflowError e) {
            plugin.getLogger().log(Level.SEVERE,
                    "Huge error on logging in player {0} during PlayerJoinEvent",
                    event.getPlayer().getName());
            StackTraceElement[] elementsInit = e.getStackTrace();
            plugin.getLogger().severe(e.getMessage());
            plugin.getLogger().severe("The debug file will contain the full stacktrace");
            for (StackTraceElement elementsInit1 : elementsInit) {
                plugin.debugLog(elementsInit1.toString());
            }
            Throwable cause = e.getCause();
            while (cause != null) {
                plugin.debugLog("Caused by: " + cause.getMessage());
                StackTraceElement[] elementsOrig = cause.getStackTrace();
                for (StackTraceElement elementsOrig1 : elementsOrig) {
                    plugin.debugLog(elementsOrig1.toString());
                }
                cause = cause.getCause();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.debugLog("PlayerQuitEvent fired, handling");
        plugin.getManager().handleLogoutEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        plugin.debugLog("PlayerChangedWorldEvent fired, handling");
        Player player = event.getPlayer();
        PermissionUser user = plugin.getManager().getUser(player);
        plugin.debugLog("Player " + player.getName() + " changed from " + event.getFrom().getName() + " to " + player.getWorld().getName());
        user.changeWorld(player, player.getWorld().getName(), plugin.getManager().getAttachment(player));
        if (plugin.isDebugMode()) {
            plugin.debugLog(event.getPlayer().getName() + " has joined in world " + event.getPlayer().getWorld().getName());
            Set<PermissionAttachmentInfo> set = event.getPlayer().getEffectivePermissions();
            plugin.debugLog("Player: " + event.getPlayer().getName());
            for (PermissionAttachmentInfo perm : set) {
                plugin.debugLog(" PermAttInfo: " + perm.getPermission());
                PermissionAttachment att = perm.getAttachment();
                if (att != null) {
                    Map<String, Boolean> map = att.getPermissions();
                    plugin.debugLog("  PermAtt: " + att.toString());
                    plugin.debugLog("  PermAttMap:");
                    for (String key : map.keySet()) {
                        plugin.debugLog("   - " + key + ": " + map.get(key));
                        Permission pe = Bukkit.getPluginManager().getPermission(key);
                        if (pe != null) {
                            for (String k : pe.getChildren().keySet()) {
                                plugin.debugLog("     - " + k + ": " + pe.getChildren().get(k));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocessDebugCheck(PlayerCommandPreprocessEvent event) {
        PermissionUser user = plugin.getManager().getUser(event.getPlayer());
        if (!user.getDebugState()) {
            return;
        }
        plugin.debugLog("Handling command preprocess for debug check for player " + event.getPlayer().getName());
        plugin.log(Level.INFO, Lang.LISTENER_TPLISTENER_PREPROCESS_ACTIVATE, event.getPlayer().getName(), event.getMessage());
        try {
            String command = event.getMessage().split(" ")[0].substring(1);
            Command cmd = Bukkit.getPluginCommand(command);
            if (cmd.testPermissionSilent(event.getPlayer())) {
                plugin.log(Level.INFO, Lang.LISTENER_TPLISTENER_PREPROCESS_ALLOW, event.getPlayer().getName(), cmd.getPermission());
            } else {
                plugin.log(Level.INFO, Lang.LISTENER_TPLISTENER_PREPROCESS_DENY, event.getPlayer().getName());
            }
        } catch (NullPointerException e) {
            plugin.log(Level.SEVERE, Lang.LISTENER_TPLISTENER_PREPROCESS_INVALID, event.getMessage());
        } catch (IndexOutOfBoundsException e) {
            plugin.log(Level.SEVERE, Lang.LISTENER_TPLISTENER_PREPROCESS_INDEX, event.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRemoteConsoleEvent(RemoteServerCommandEvent event) {
        plugin.debugLog("RemoteServerCommandEvent fired, handling");
        CommandSender sender = event.getSender();
        plugin.getManager().getRcon().setPerms(sender, plugin.getManager().getAttachment(sender.getName()), null);
    }
}
