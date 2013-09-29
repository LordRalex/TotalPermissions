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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class FlatFileDataHolder extends MemoryDataHolder {

    protected final File root;

    public FlatFileDataHolder(File file) {
        root = file;
    }

    @Override
    public void setup() {
        super.setup();
        root.mkdirs();
    }

    @Override
    public void save(PermissionType type, String name) throws IOException {
        name = name.toLowerCase();
        new File(root, type.toString()).mkdirs();
        Map<String, ConfigurationSection> map = memory.get(type);
        if (map == null) {
            return;
        }
        ConfigurationSection cfg = map.get(name);
        if (cfg == null) {
            return;
        }
        YamlConfiguration yaml = new YamlConfiguration();
        if (cfg instanceof YamlConfiguration) {
            yaml = (YamlConfiguration) cfg;
        } else {
            Set<String> keys = cfg.getKeys(true);
            for (String key : keys) {
                yaml.set(key, cfg.get(key));
            }
        }
        yaml.save(new File(new File(root, type.toString()), name + ".yml"));
    }

    @Override
    public void load(PermissionType type, String name) throws IOException, InvalidConfigurationException {
        name = name.toLowerCase();
        new File(root, type.toString()).mkdirs();
        YamlConfiguration data = new YamlConfiguration();
        data.load(new File(new File(root, type.toString()), name + ".yml"));
    }

    @Override
    public Set<String> getKeys(PermissionType type) {
        Set<String> names = new HashSet<String>();
        File dir = new File(root, type.toString());
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".yml")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        for (File file : files) {
            names.add(file.getName().substring(0, file.getName().length() - 4).toLowerCase());
        }
        return names;
    }
}
