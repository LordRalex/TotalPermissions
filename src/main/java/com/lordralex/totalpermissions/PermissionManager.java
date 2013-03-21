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
package com.lordralex.totalpermissions;

import com.lordralex.totalpermissions.permission.PermissionConsole;
import com.lordralex.totalpermissions.permission.PermissionGroup;
import com.lordralex.totalpermissions.permission.PermissionRcon;
import com.lordralex.totalpermissions.permission.PermissionUser;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 */
public final class PermissionManager {

    protected final Map<String, PermissionGroup> groups = new ConcurrentHashMap<String, PermissionGroup>();
    protected final Map<String, PermissionUser> users = new ConcurrentHashMap<String, PermissionUser>();
    protected String defaultGroup;
    protected PermissionConsole console;
    protected PermissionRcon remote;

    public PermissionManager() {
    }

    public void load() throws InvalidConfigurationException {
        FileConfiguration perms = TotalPermissions.getPlugin().getPermFile();
        ConfigurationSection filegroups = perms.getConfigurationSection("groups");
        if (groups == null) {
            throw new InvalidConfigurationException("You must define at least one group");
        }
        Set<String> allGroups = filegroups.getKeys(false);
        for (String group : allGroups) {
            PermissionGroup temp = new PermissionGroup(group);
            groups.put(group.toLowerCase(), temp);
            if (temp.isDefault()) {
                defaultGroup = temp.getName();
            }
        }
        if (defaultGroup == null) {
            throw new InvalidConfigurationException("You must define at least one default group");
        }
        console = new PermissionConsole();
        remote = new PermissionRcon();
    }

    public void unload() {
        users.clear();
        groups.clear();
        TotalPermissions.getPlugin().getListener().clearPerms();
    }

    public String getDefaultGroup() {
        return defaultGroup;
    }

    /**
     * Gets the {@link PermissionConsole}. This is the console's perms.
     *
     * @return The {@link PermissionConsole} for the console
     *
     * @since 1.0
     */
    public PermissionConsole getConsole() {
        return console;
    }

    /**
     * Gets the {@link PermissionRcon}. This is the rcon's perms.
     *
     * @return The {@link PermissionRcon} for the console
     *
     * @since 1.0
     */
    public PermissionRcon getRcon() {
        return remote;
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
    public PermissionUser getUser(String player) {
        PermissionUser user;
        synchronized (users) {
            user = users.get(player.toLowerCase());
            if (user == null) {
                user = new PermissionUser(player);
            }
            users.put(player.toLowerCase(), user);
        }
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
        PermissionGroup group;
        synchronized (groups) {
            group = groups.get(name.toLowerCase());
            if (group == null) {
                group = new PermissionGroup(name);
            }
            groups.put(name.toLowerCase(), group);
        }
        return group;
    }

    /**
     *
     * @param player
     * @param perm
     * @return True if player has been given this perm, false otherwise
     */
    public boolean has(Player player, String perm) {
        return has(player.getName(), perm);
    }

    public boolean has(String player, String perm) {
        String name = player.toLowerCase();
        PermissionUser user;
        synchronized (users) {
            user = users.get(name);
            if (user == null) {
                user = new PermissionUser(name);
                users.put(name, user);
            }
        }
        return user.has(perm);
    }

    public boolean groupHas(String groupName, String perm) {
        String name = groupName.toLowerCase();
        synchronized (groups) {
            PermissionGroup group = groups.get(name);
            if (group == null) {
                group = new PermissionGroup(name);
                groups.put(name, group);
            }
        }
        return false;
    }

    public void addPerm(Player player, String perm, boolean allowance) {
        String name = player.getName().toLowerCase();
        synchronized (users) {
            PermissionUser user = users.get(name);
            if (user == null) {
                user = new PermissionUser(name);
                users.put(name, user);
            }
            if (!allowance) {
                perm = "-" + perm;
            }
            user.addPerm(perm);
        }
    }

    public synchronized void addPerm(OfflinePlayer player, String perm, boolean allowance) {
        String name = player.getName().toLowerCase();

        synchronized (users) {
            PermissionUser user = users.get(name);
            if (user == null) {
                user = new PermissionUser(name);
                users.put(name, user);
            }
            if (!allowance) {
                perm = "-" + perm;
            }
            user.addPerm(perm);
        }
    }

    public void addPermToGroup(String group, String perm, boolean allowance) {
        String name = group.toLowerCase();

        synchronized (groups) {
            PermissionGroup gr = groups.get(name);
            if (gr == null) {
                gr = new PermissionGroup(name);
                groups.put(name, gr);
            }
            if (!allowance) {
                perm = "-" + perm;
            }
            gr.addPerm(perm);
        }
    }

    public void addPermToUser(String user, String perm, boolean allowance) {
        String name = user.toLowerCase();

        synchronized (users) {
            PermissionUser gr = users.get(name);
            if (gr == null) {
                gr = new PermissionUser(name);
                users.put(name, gr);
            }
            if (!allowance) {
                perm = "-" + perm;
            }
            gr.addPerm(perm);
        }
    }
}
