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
package net.ae97.totalpermissions.sqlite;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.ae97.totalpermissions.base.PermissionBase;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

/**
 * @author Lord_Ralex
 */
public abstract class SQLitePermissionBase implements PermissionBase {

    protected final String name;
    protected final Map<String, List<String>> permissions = new HashMap<String, List<String>>();
    protected final Map<String, Map<String, Object>> options = new HashMap<String, Map<String, Object>>();
    private boolean debug = false;

    public SQLitePermissionBase(String n) {
        name = n;
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
        load(new HashMap<String, Object>());
    }

    public void load(Map<String, Object> map) throws DataLoadFailedException {
        Object obj;
        obj = map.get("permissions");
        if (obj != null && obj instanceof String) {
            String data = (String) obj;
            String[] perms = data.split("\n");
            List<String> permList = new LinkedList<String>();
            for (String perm : perms) {
                Permission p = Bukkit.getPluginManager().getPermission(perm);
                if (p == null) {
                    p = new Permission(perm);
                    Bukkit.getPluginManager().addPermission(p);
                }
                if (!permList.contains(p.getName())) {
                    permList.add(p.getName());
                }
            }
            permissions.put(null, permList);
        }
        obj = map.get("options");
        if (obj != null && obj instanceof Map) {
            Map optionMap = (Map) obj;
            Map<String, Object> converted = new HashMap<String, Object>();
            for (Object k : optionMap.keySet()) {
                if (k != null && k instanceof String) {
                    converted.put((String) k, optionMap.get(k));
                }
            }
            options.put(null, converted);
        }
    }

    @Override
    public void save() {
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
        List<String> perms = permissions.get(world == null || world.isEmpty() ? null : world.toLowerCase());
        if (perms == null) {
            perms = new LinkedList<String>();
            permissions.put(world == null || world.isEmpty() ? null : world.toLowerCase(), perms);
        }
        return perms;
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
        permissions.put(world == null || world.isEmpty() ? null : world.toLowerCase(), perms);
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
        permissions.put(world == null || world.isEmpty() ? null : world.toLowerCase(), perms);
        return removed;
    }
}
