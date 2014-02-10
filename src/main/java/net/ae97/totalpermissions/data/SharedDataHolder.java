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
package net.ae97.totalpermissions.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class SharedDataHolder implements DataHolder {

    private final List<DataHolder> dataHolders = new ArrayList<DataHolder>();
    private final TotalPermissions plugin;

    public SharedDataHolder(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public void setup() throws InvalidConfigurationException {
        List<String> types = plugin.getConfig().getStringList("shared-list");
        if (types == null || types.isEmpty()) {
            throw new InvalidConfigurationException("Cannot create shared data holder with no holders");
        }
        for (String holder : types) {
            DataHolder dataHolder = null;
            DataType type = DataType.valueOf(holder.toUpperCase());
            switch (type) {
                case MYSQL: {
                    dataHolder = new MySQLDataHolder();
                }
                break;

                case FLAT: {
                    dataHolder = new FlatFileDataHolder(new File(plugin.getDataFolder(), "data"));
                }
                break;
                case YAML: {
                    dataHolder = new YamlDataHolder(new File(plugin.getDataFolder(), "permissions.yml"));
                }
                break;
            }
            if (dataHolder != null) {
                dataHolder.setup();
                dataHolders.add(dataHolder);
            }
        }
        if (dataHolders.isEmpty()) {
            throw new InvalidConfigurationException("Cannot create shared data holder with no holders");
        }
    }

    @Override
    public void load(PermissionType type, String name) throws IOException, InvalidConfigurationException {
        for (DataHolder holder : dataHolders) {
            if (!holder.contains(type, name)) {
                continue;
            }
            holder.load(type, name);
            return;
        }
    }

    @Override
    public ConfigurationSection getConfigurationSection(PermissionType type, String name) {
        for (DataHolder holder : dataHolders) {
            if (!holder.contains(type, name)) {
                continue;
            }
            return holder.getConfigurationSection(type, name);
        }
        return dataHolders.get(0).create(type, name);
    }

    @Override
    public Set<String> getKeys(PermissionType type) {
        Set<String> keyList = new HashSet<String>();
        for (DataHolder holder : dataHolders) {
            Set<String> temp = holder.getKeys(type);
            if (temp != null) {
                keyList.addAll(temp);
            }
        }
        return keyList;
    }

    @Override
    public void update(PermissionType type, String name, ConfigurationSection obj) {
        for (DataHolder holder : dataHolders) {
            if (!holder.contains(type, name)) {
                continue;
            }
            holder.update(type, name, obj);
            return;
        }
    }

    @Override
    public ConfigurationSection create(PermissionType type, String name) {
        DataHolder holder = dataHolders.get(0);
        return holder.create(type, name);
    }

    @Override
    public void save(PermissionType type, String name) throws IOException {
        for (DataHolder holder : dataHolders) {
            if (!holder.contains(type, name)) {
                continue;
            }
            holder.save(type, name);
            return;
        }
    }

    @Override
    public boolean contains(PermissionType type, String name) {
        for (DataHolder holder : dataHolders) {
            if (holder.contains(type, name)) {
                return true;
            }
        }
        return false;
    }

}
