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

import net.ae97.totalpermissions.permission.PermissionType;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class FlatFileDataHolder implements DataHolder {

    private final File root;
    private final Map<PermissionType, Map<String, ConfigurationSection>> buffer = new EnumMap<PermissionType, Map<String, ConfigurationSection>>(PermissionType.class);

    public FlatFileDataHolder(File file) {
        root = file;
    }

    @Override
    public void setup() {
        root.mkdirs();
    }

    @Override
    public void save(PermissionType type, String name) throws IOException {
        new File(root, type.toString()).mkdirs();
        Map<String, ConfigurationSection> map = buffer.get(type);
        if (map == null) {
            return;
        }
        ConfigurationSection cfg = map.get(name);
        if (cfg == null) {
            return;
        }
        YamlConfiguration yaml = new YamlConfiguration();
        //convert cfg to yaml
        yaml.save(new File(new File(root, type.toString()), name + ".yml"));
    }

    @Override
    public void load(PermissionType type, String name) {
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
