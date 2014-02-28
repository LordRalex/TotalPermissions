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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.base.PermissionGroup;
import net.ae97.totalpermissions.base.PermissionUser;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.type.PermissionType;
import org.bukkit.Bukkit;

/**
 * @author Lord_Ralex
 */
public class SQLitePermissionUser extends SQLitePermissionBase implements PermissionUser {

    private final LinkedList<String> groups = new LinkedList<String>();

    public SQLitePermissionUser(String n) {
        super(n);
    }

    @Override
    public void load(Map<String, Object> map) throws DataLoadFailedException {
        super.load(map);
        Object obj = map.get("groups");
        if (obj instanceof List) {
            List list = (List) obj;
            for (Object o : list) {
                if (o instanceof String) {
                    String s = (String) o;
                    if (!groups.contains(s)) {
                        groups.add(s);
                    }
                }
            }
        }
    }

    @Override
    public List<String> getPermissions() {
        return getPermissions(null);
    }

    @Override
    public List<String> getPermissions(String world) {
        LinkedList<String> perms = new LinkedList<String>();
        perms.addAll(super.getPermissions(world));
        TotalPermissions plugin = (TotalPermissions) Bukkit.getPluginManager().getPlugin("TotalPermissions");
        for (String group : groups) {
            try {
                PermissionGroup permGroup = plugin.getDataManager().getGroup(group);
                List<String> groupPerms = permGroup.getPermissions();
                perms.addAll(groupPerms);
            } catch (DataLoadFailedException ex) {
                plugin.getLogger().log(Level.SEVERE, "An error occured on loading " + group, ex);
            }
        }
        return perms;
    }

    @Override
    public Map<String, Object> getOptions() {
        return getOptions(null);
    }

    @Override
    public Object getOption(String option) {
        return getOption(option, null);
    }

    @Override
    public Object getOption(String option, String world) {
        Object opt = super.getOption(option, world);
        if (opt == null) {
            TotalPermissions plugin = (TotalPermissions) Bukkit.getPluginManager().getPlugin("TotalPermissions");
            for (String group : groups) {
                try {
                    PermissionGroup permGroup = plugin.getDataManager().getGroup(group);
                    opt = permGroup.getOption(option, world);
                    if (opt != null) {
                        break;
                    }
                } catch (DataLoadFailedException ex) {
                    plugin.getLogger().log(Level.SEVERE, "An error occured on loading " + group, ex);
                }
            }
        }
        return opt;
    }

    @Override
    public List<String> getGroups() {
        return (LinkedList<String>) groups.clone();
    }

    @Override
    public boolean addGroup(String group) {
        if (groups.contains(group)) {
            return false;
        }
        return groups.add(group);
    }

    @Override
    public boolean removeGroup(String group) {
        return groups.remove(group);
    }

    @Override
    public PermissionType getType() {
        return PermissionType.USER;
    }

    @Override
    protected Map<String, Object> getSaveData() {
        Map<String, Object> mappings = super.getSaveData();
        mappings.put("groups", groups);
        return mappings;
    }

    protected static Map<String, String> getColumns() {
        Map<String, String> columns = SQLitePermissionBase.getColumns();
        columns.put("groups", "TEXT");
        return columns;
    }
}
