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
package net.ae97.totalpermissions.yaml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.ae97.totalpermissions.base.PermissionBase;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

/**
 * @author Lord_Ralex
 */
public abstract class YamlPermissionBase implements PermissionBase {

    protected final YamlConfiguration yamlConfiguration;
    protected final String name;
    protected final Map<String, LinkedList<String>> permissions = new HashMap<String, LinkedList<String>>();
    protected final Map<String, Map<String, Object>> options = new HashMap<String, Map<String, Object>>();
    private boolean debug = false;

    public YamlPermissionBase(String n, YamlConfiguration config) {
        name = n;
        yamlConfiguration = config;
    }

    @Override
    public final boolean isDebug() {
        return debug;
    }

    @Override
    public final boolean setDebug(boolean d) {
        return (debug = d);
    }

    @Override
    public void load() throws DataLoadFailedException {
        List<String> list = yamlConfiguration.getStringList("permissions");
        LinkedList<String> global = new LinkedList<String>();
        for (String l : list) {
            Permission p = Bukkit.getPluginManager().getPermission(l);
            if (p == null) {
                p = new Permission(l);
                Bukkit.getPluginManager().addPermission(p);
            }
            if (!global.contains(p.getName())) {
                global.add(p.getName());
            }
        }
        permissions.put(null, global);
        ConfigurationSection optionSec = yamlConfiguration.getConfigurationSection("options");
        if (optionSec != null) {
            Map<String, Object> mappings = optionSec.getValues(true);
            options.put(null, mappings);
        }
    }

    @Override
    public void save() throws DataSaveFailedException {
        for (String key : yamlConfiguration.getKeys(true)) {
            yamlConfiguration.set(key, null);
        }
        for (Entry<String, LinkedList<String>> entry : permissions.entrySet()) {
            yamlConfiguration.set(entry.getKey() == null ? "permissions" : "worlds." + entry.getKey().toLowerCase() + ".permissions", entry.getValue());
        }
        for (Entry<String, Map<String, Object>> entry : options.entrySet()) {
            yamlConfiguration.set(entry.getKey() == null ? "options" : "worlds." + entry.getKey().toLowerCase() + ".options", entry.getValue());
        }
    }

    @Override
    public Map<String, Object> getOptions() {
        return getOptions(null);
    }

    @Override
    public List<String> getDeclaredPermissions() {
        return getDeclaredPermissions(null);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getOptions(String world) {
        Map<String, Object> opts = options.get(world == null || world.isEmpty() ? null : world.toLowerCase());
        if (opts == null) {
            opts = new HashMap<String, Object>();
            options.put(world == null || world.isEmpty() ? null : world.toLowerCase(), opts);
        }
        return opts;
    }

    @Override
    public Object getOption(String option) {
        return getOption(option, null);
    }

    @Override
    public Object getOption(String option, String world) {
        Map<String, Object> map = getOptions(world);
        if (map != null) {
            return map.get(option);
        } else {
            return null;
        }
    }

    @Override
    public void setOption(String key, Object option) {
        setOption(key, option, null);
    }

    @Override
    public void setOption(String key, Object option, String world) {
        Map<String, Object> map = getOptions(world);
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        map.put(key, option);
        options.put(world == null || world.isEmpty() ? null : world.toLowerCase(), map);
    }

    @Override
    public List<String> getDeclaredPermissions(String world) {
        LinkedList<String> perms = permissions.get(world == null || world.isEmpty() ? null : world.toLowerCase());
        if (perms == null) {
            perms = new LinkedList<String>();
            permissions.put(world == null || world.isEmpty() ? null : world.toLowerCase(), perms);
        }
        return (LinkedList<String>) perms.clone();
    }

    @Override
    public List<String> getPermissions() {
        return getPermissions(null);
    }

    @Override
    public List<String> getPermissions(String world) {
        List<String> perms = getDeclaredPermissions(world);
        if (world != null && !world.isEmpty()) {
            perms.addAll(getDeclaredPermissions());
        }
        return perms;
    }

    @Override
    public boolean addPermission(String perm) {
        return addPermission(perm, null);
    }

    @Override
    public boolean addPermission(String perm, String world) {
        List<String> perms = getDeclaredPermissions(world);
        boolean added = perms.add(perm);
        LinkedList<String> newPerms = new LinkedList<String>();
        for (String p : perms) {
            newPerms.add(p);
        }
        permissions.put(world == null || world.isEmpty() ? null : world.toLowerCase(), newPerms);
        return added;
    }

    @Override
    public boolean removePermission(String perm) {
        return removePermission(perm, null);
    }

    @Override
    public boolean removePermission(String perm, String world) {
        List<String> perms = getDeclaredPermissions(world);
        boolean removed = perms.remove(perm);
        LinkedList<String> newPerms = new LinkedList<String>();
        for (String p : perms) {
            newPerms.add(p);
        }
        permissions.put(world == null || world.isEmpty() ? null : world.toLowerCase(), newPerms);
        return removed;
    }
}
