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

import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionType;
import net.ae97.totalpermissions.sql.PermissionPersistance;
import com.avaje.ebean.EbeanServer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class MySQLDataHolder implements DataHolder {

    private final Map<PermissionType, Map<String, ConfigurationSection>> memory = new EnumMap<PermissionType, Map<String, ConfigurationSection>>(PermissionType.class);

    @Override
    public void load(PermissionType type, String name) {
        EbeanServer server = TotalPermissions.getPlugin().getDatabase();
        PermissionPersistance section = server.find(PermissionPersistance.class).where().ieq("type", type.toString()).ieq("name", name).findUnique();
        if (section == null) {
            section = new PermissionPersistance();
            section.setName(name);
            section.setType(type);
            YamlConfiguration cfg = new YamlConfiguration();
            section.setSection(cfg);
        }
    }

    @Override
    public void save(PermissionType type, String name) {
        EbeanServer server = TotalPermissions.getPlugin().getDatabase();
        PermissionPersistance section = server.find(PermissionPersistance.class).where().ieq("type", type.toString()).ieq("name", name).findUnique();
        if (section == null) {
            section = new PermissionPersistance();
            section.setName(name);
            section.setType(type);
        }
        Map<String, ConfigurationSection> map = memory.get(type);
        if (map == null) {
            map = new HashMap<String, ConfigurationSection>();
        }
        ConfigurationSection cfg = map.get(name);
        if (cfg == null) {
            cfg = new YamlConfiguration();
        }
        section.setSection(cfg);
        server.save(section);
    }

    @Override
    public ConfigurationSection getConfigurationSection(PermissionType type, String name) {
        return null;
    }

    @Override
    public Set<String> getKeys(PermissionType type) {
        return null;
    }

    @Override
    public void update(PermissionType type, String name, ConfigurationSection obj) {
    }

    @Override
    public ConfigurationSection create(PermissionType type, String name) {
        return null;
    }

    @Override
    public boolean contains(PermissionType type, String name) {
        return false;
    }
}
