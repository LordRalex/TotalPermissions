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

import net.ae97.totalpermissions.yaml.YamlPermissionBase;
import java.util.LinkedList;
import java.util.List;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.base.PermissionGroup;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import net.ae97.totalpermissions.type.PermissionType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Lord_Ralex
 */
public class YamlPermissionGroup extends YamlPermissionBase implements PermissionGroup {

    protected final List<String> inheritence = new LinkedList<String>();
    protected int rank = 0;
    protected boolean defaultGroup = false;

    public YamlPermissionGroup(String n, YamlConfiguration config) {
        super(n, config);
    }

    @Override
    public void load() throws DataLoadFailedException {
        super.load();
        if (yamlConfiguration.contains("rank")) {
            rank = yamlConfiguration.getInt("rank", 0);
        } else if (yamlConfiguration.contains("options.rank")) {
            rank = yamlConfiguration.getInt("options.rank", 0);
        } else {
            rank = 0;
        }

        inheritence.clear();
        List<String> inher = yamlConfiguration.getStringList("inheritence");
        if (inher != null) {
            inheritence.addAll(inher);
        }
    }

    @Override
    public void save() throws DataSaveFailedException {
        super.save();
        yamlConfiguration.set("rank", rank);
        yamlConfiguration.set("inheritence", inheritence);
        if (defaultGroup) {
            yamlConfiguration.set("default", true);
        }
    }

    @Override
    public List<String> getInheritence() {
        return inheritence;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public PermissionType getType() {
        return PermissionType.GROUP;
    }

    @Override
    public boolean isDefault() {
        return defaultGroup;
    }

    @Override
    public boolean setDefault(boolean def) {
        return (defaultGroup = def);
    }

    @Override
    public List<String> getPermissions(String world) {
        List<String> perms = super.getPermissions(world);

        TotalPermissions plugin = (TotalPermissions) Bukkit.getPluginManager().getPlugin("TotalPermissions");
        for (String group : inheritence) {
            PermissionGroup permGroup = plugin.getDataManager().getGroup(group);
            if (permGroup != null) {
                perms.addAll(permGroup.getPermissions());
            }
        }

        return perms;
    }
}
