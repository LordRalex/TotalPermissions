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
import java.util.HashSet;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class YamlDataHolder implements DataHolder {

    protected final YamlConfiguration yaml = new YamlConfiguration();
    protected final File savePath;

    public YamlDataHolder(File save) {
        savePath = save;
    }

    @Override
    public void setup() throws InvalidConfigurationException {
        try {
            yaml.load(savePath);
        } catch (IOException ex) {
            throw new InvalidConfigurationException(ex);
        }
    }

    @Override
    public void save(PermissionType type, String name) throws IOException {
        yaml.save(savePath);
    }

    @Override
    public void load(PermissionType type, String name) {
    }

    @Override
    public ConfigurationSection getConfigurationSection(PermissionType type, String name) {
        return yaml.getConfigurationSection(type.toString() + "." + getCorrectKey(type, name));
    }

    @Override
    public Set<String> getKeys(PermissionType type) {
        return yaml.contains(type.toString()) ? yaml.getConfigurationSection(type.toString()).getKeys(false) : new HashSet<String>();
    }

    @Override
    public void update(PermissionType type, String name, ConfigurationSection obj) {
        yaml.set(type.toString() + "." + getCorrectKey(type, name), obj);
    }

    @Override
    public ConfigurationSection create(PermissionType type, String name) {
        return yaml.createSection(type.toString() + "." + getCorrectKey(type, name));
    }

    @Override
    public boolean contains(PermissionType type, String name) {
        return yaml.contains(type.toString() + "." + getCorrectKey(type, name));
    }

    protected String getCorrectKey(PermissionType type, String name) {
        Set<String> keys = getKeys(type);
        for (String key : keys) {
            if (key.equalsIgnoreCase(name)) {
                return key;
            }
        }
        return name;
    }
}
