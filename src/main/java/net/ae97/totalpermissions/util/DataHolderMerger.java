/*
 * Copyright (C) 2013 AE97
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
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.MySQLDataHolder;
import net.ae97.totalpermissions.data.YamlDataHolder;
import net.ae97.totalpermissions.lang.Lang;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class DataHolderMerger {

    protected final DataHolder parent;
    protected final TotalPermissions plugin;

    public DataHolderMerger(TotalPermissions pl, DataHolder p) {
        plugin = pl;
        parent = p;
    }

    public void merge(File mergeFrom) throws IOException, InvalidConfigurationException {
        YamlDataHolder newHolder = new YamlDataHolder(mergeFrom);
        merge(newHolder);
    }

    public void merge(DataHolder mergeFrom) throws IOException, InvalidConfigurationException {
        plugin.getLogger().info("Merging data holders, this might take a second or two");
        if (mergeFrom instanceof MySQLDataHolder) {
            plugin.log(Level.WARNING, Lang.MERGE_MYSQLFROM);
        }
        if (parent instanceof MySQLDataHolder) {
            plugin.log(Level.WARNING, Lang.MERGE_MYSQLTO);
        }
        for (PermissionType type : PermissionType.values()) {
            plugin.debugLog("Checking for: " + type.toString());
            Set<String> keys = mergeFrom.getKeys(type);
            for (String key : keys) {
                plugin.debugLog(" Merging in " + key);
                mergeFrom.load(type, key);
                ConfigurationSection sec = mergeFrom.getConfigurationSection(type, key);
                parent.update(type, key, sec);
                parent.save(type, key);
            }
        }

        plugin.log(Level.INFO, Lang.MERGE_COMPLETE);
    }
}
