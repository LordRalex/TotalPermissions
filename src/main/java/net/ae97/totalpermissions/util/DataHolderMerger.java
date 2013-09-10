/*
 * Copyright (C) 2013 Lord_Ralex
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
package net.ae97.totalpermissions.util;

import java.io.File;
import java.util.Set;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.MySQLDataHolder;
import net.ae97.totalpermissions.data.YamlDataHolder;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class DataHolderMerger {

    private final DataHolder parent;
    private final Plugin plugin;

    public DataHolderMerger(Plugin pl, DataHolder p) {
        plugin = pl;
        parent = p;
    }

    public void merge(File mergeFrom) {
        YamlDataHolder newHolder = new YamlDataHolder(mergeFrom);
        merge(newHolder);
    }

    public void merge(DataHolder mergeFrom) {
        plugin.getLogger().info("Merging data holders, this might take a second or two");
        if (mergeFrom instanceof MySQLDataHolder) {
            plugin.getLogger().warning("Merging from MySQL can take some time, so this is your warning");
        }
        if (parent instanceof MySQLDataHolder) {
            plugin.getLogger().warning("First time merging into MySQL can take some time, so be prepared");
        }
        for (PermissionType type : PermissionType.values()) {
            Set<String> keys = mergeFrom.getKeys(type);
            for (String key : keys) {
                ConfigurationSection sec = mergeFrom.getConfigurationSection(type, key);
                parent.update(type, key, sec);
            }
        }

        plugin.getLogger().info("Merge complete");
    }
}
