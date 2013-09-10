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
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
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
        for (PermissionType type : PermissionType.values()) {
            buffer.put(type, new ConcurrentHashMap<String, ConfigurationSection>());
        }
        root.mkdirs();
    }

    @Override
    public void save(PermissionType type, String name) throws IOException {
        name = name.toLowerCase();
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
        if (cfg instanceof YamlConfiguration) {
            yaml = (YamlConfiguration) cfg;
        } else {
            Set<String> keys = cfg.getKeys(true);
            for (String key : keys) {
                System.out.println("Adding key: " + key);
                yaml.set(key, cfg.get(key));
            }
        }
        yaml.save(new File(new File(root, type.toString()), name + ".yml"));
    }

    @Override
    public void load(PermissionType type, String name) {
        name = name.toLowerCase();
        new File(root, type.toString()).mkdirs();
        YamlConfiguration data = new YamlConfiguration();
        try {
            data.load(new File(new File(root, type.toString()), name + ".yml"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlatFileDataHolder.class.getName()).log(Level.SEVERE, "No " + name + ".yml file found", ex);
        } catch (IOException ex) {
            Logger.getLogger(FlatFileDataHolder.class.getName()).log(Level.SEVERE, "An IOException occured on reading " + name + ".yml", ex);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(FlatFileDataHolder.class.getName()).log(Level.SEVERE, "The file " + name + ".yml is not in proper YAML format", ex);
        }
    }

    @Override
    public ConfigurationSection getConfigurationSection(PermissionType type, String name) {
        name = name.toLowerCase();
        return buffer.get(type).get(name);
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

    @Override
    public void update(PermissionType type, String name, ConfigurationSection obj) {
        buffer.get(type).put(name, obj);
    }

    @Override
    public ConfigurationSection create(PermissionType type, String name) {
        YamlConfiguration cfg = new YamlConfiguration();
        buffer.get(type).put(name.toLowerCase(), cfg);
        return cfg;
    }

    @Override
    public boolean contains(PermissionType type, String name) {
        return buffer.get(type).containsKey(name.toLowerCase());
    }
}
