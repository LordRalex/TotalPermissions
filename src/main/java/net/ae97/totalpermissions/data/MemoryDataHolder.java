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
package net.ae97.totalpermissions.data;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public abstract class MemoryDataHolder implements DataHolder {

    protected final Map<PermissionType, Map<String, ConfigurationSection>> memory = new EnumMap<PermissionType, Map<String, ConfigurationSection>>(PermissionType.class);

    @Override
    public void setup() {
        for (PermissionType type : PermissionType.values()) {
            memory.put(type, new HashMap<String, ConfigurationSection>());
        }
    }

    @Override
    public ConfigurationSection getConfigurationSection(PermissionType type, String name) {
        Map<String, ConfigurationSection> map = memory.get(type);
        if (map == null) {
            map = new HashMap<String, ConfigurationSection>();
            memory.put(type, map);
        }
        ConfigurationSection cfg = map.get(name.toLowerCase());
        if (cfg == null) {
            try {
                load(type, name);
                map = memory.get(type);
                if (map == null) {
                    map = new HashMap<String, ConfigurationSection>();
                    memory.put(type, map);
                }
                cfg = map.get(name.toLowerCase());
            } catch (IOException ex) {
                TotalPermissions plugin = TotalPermissions.getPlugin();
                plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("error.generic"), ex);
            } catch (InvalidConfigurationException ex) {
                TotalPermissions plugin = TotalPermissions.getPlugin();
                plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("error.config", type + "." + name));
                plugin.getLogger().severe(ex.getMessage());
                plugin.debugLog(ex);
            }
        }
        return cfg;
    }

    @Override
    public void update(PermissionType type, String name, ConfigurationSection obj) {
        Map<String, ConfigurationSection> map = memory.get(type);
        if (map == null) {
            map = new HashMap<String, ConfigurationSection>();
        }
        map.put(name.toLowerCase(), obj);
        memory.put(type, map);
        try {
            save(type, name);
        } catch (IOException ex) {
            TotalPermissions plugin = TotalPermissions.getPlugin();
            plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("error.generic"), ex);
        }
    }

    @Override
    public ConfigurationSection create(PermissionType type, String name) {
        Map<String, ConfigurationSection> map = memory.get(type);
        if (map == null) {
            map = new HashMap<String, ConfigurationSection>();
        }
        map.put(name.toLowerCase(), new MemoryConfiguration());
        memory.put(type, map);
        try {
            save(type, name);
        } catch (IOException ex) {
            TotalPermissions plugin = TotalPermissions.getPlugin();
            plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("error.generic"), ex);
        }
        return getConfigurationSection(type, name);
    }

    @Override
    public boolean contains(PermissionType type, String name) {
        Map<String, ConfigurationSection> map = memory.get(type);
        if (map == null) {
            map = new HashMap<String, ConfigurationSection>();
        }
        if (map.containsKey(name.toLowerCase())) {
            return true;
        }
        try {
            load(type, name);
        } catch (IOException ex) {
            TotalPermissions plugin = TotalPermissions.getPlugin();
            plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("error.generic"), ex);
        } catch (InvalidConfigurationException ex) {
            TotalPermissions plugin = TotalPermissions.getPlugin();
            plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("error.config", type + "." + name));
            plugin.getLogger().severe(ex.getMessage());
            plugin.debugLog(ex);
        }
        map = memory.get(type);
        if (map == null) {
            map = new HashMap<String, ConfigurationSection>();
        }
        return map.containsKey(name.toLowerCase());
    }

    @Override
    public Set<String> getKeys(PermissionType type) {
        return memory.get(type).keySet();
    }
}
