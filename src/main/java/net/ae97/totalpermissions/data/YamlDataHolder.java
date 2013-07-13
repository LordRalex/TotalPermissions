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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class YamlDataHolder implements DataHolder {

    private YamlConfiguration yaml;

    @Override
    public void load(InputStream in) throws InvalidConfigurationException, IOException {
        yaml = new YamlConfiguration();
        yaml.load(in);
    }

    @Override
    public void load(File file) throws InvalidConfigurationException, IOException {
        yaml = new YamlConfiguration();
        yaml.load(file);
    }

    @Override
    public void load(String string) throws InvalidConfigurationException {
        yaml = new YamlConfiguration();
        yaml.loadFromString(string);
    }

    @Override
    public String getString(String key) {
        return yaml.getString(key);
    }

    @Override
    public List<String> getStringList(String key) {
        return yaml.getStringList(key);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String key) {
        return yaml.getConfigurationSection(key);
    }

    @Override
    public Set<String> getKeys() {
        return yaml.getKeys(false);
    }

    @Override
    public void set(String key, Object obj) {
        yaml.set(key, obj);
    }

    @Override
    public ConfigurationSection createSection(String key) {
        return yaml.createSection(key);
    }

    @Override
    public boolean isConfigurationSection(String key) {
        return yaml.isConfigurationSection(key);
    }

    @Override
    public void save(File file) throws IOException {
        yaml.save(file);
    }

    @Override
    public void save(String string) throws IOException {
        yaml.save(string);
    }

    @Override
    public boolean contains(String key) {
        return yaml.contains(key);
    }
}
