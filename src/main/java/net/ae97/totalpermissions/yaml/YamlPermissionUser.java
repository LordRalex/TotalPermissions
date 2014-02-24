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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.base.PermissionGroup;
import net.ae97.totalpermissions.base.PermissionUser;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import net.ae97.totalpermissions.type.PermissionType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Lord_Ralex
 */
public class YamlPermissionUser extends YamlPermissionBase implements PermissionUser {

    private final LinkedList<String> groups = new LinkedList<String>();

    public YamlPermissionUser(String n, ConfigurationSection config) {
        super(n, config);
    }

    @Override
    public void load() throws DataLoadFailedException {
        super.load();
        groups.clear();
        List<String> groupList = yamlConfiguration.getStringList("groups");
        if (groupList != null) {
            groups.addAll(groupList);
        }
    }

    @Override
    public void save() throws DataSaveFailedException {
        super.save();
        yamlConfiguration.set("groups", groups);
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
                plugin.log(Level.SEVERE, "An error occured on loading " + group, ex);
            }
        }
        return perms;
    }

    @Override
    public Map<String, Object> getOptions() {
        return getOptions(null);
    }

    @Override
    public Map<String, Object> getOptions(String world) {
        //TODO: Have this provide all options, not just declared ones
        return super.getOptions(world);
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
                    plugin.log(Level.SEVERE, "An error occured on loading " + group, ex);
                }
            }
        }
        return opt;
    }

    @Override
    public List<String> getGroups() {
        return (List<String>) groups.clone();
    }

    @Override
    public PermissionType getType() {
        return PermissionType.USER;
    }

    @Override
    public boolean addGroup(String group) {
        if (!groups.contains(group)) {
            groups.add(group);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeGroup(String group) {
        if (groups.contains(group)) {
            groups.remove(group);
            return true;
        }
        return false;
    }
}
