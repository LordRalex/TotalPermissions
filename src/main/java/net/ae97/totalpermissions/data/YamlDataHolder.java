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
import net.ae97.totalpermissions.TotalPermissions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class YamlDataHolder implements DataHolder {

    private YamlConfiguration yaml;
    private final TotalPermissions plugin;

    public YamlDataHolder() {
        plugin = TotalPermissions.getPlugin();
    }

    @Override
    public void load(InputStream in) throws InvalidConfigurationException, IOException {
        plugin.debugLog("Loading from InputStream: " + in.toString());
        yaml = YamlConfiguration.loadConfiguration(in);
    }

    @Override
    public void load(File file) throws InvalidConfigurationException, IOException {
        plugin.debugLog("Loading from File: " + file.getPath());
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void load(String string) throws InvalidConfigurationException {
        plugin.debugLog("Loading from String: " + string);
        yaml = YamlConfiguration.loadConfiguration(new File(string));
    }

    @Override
    public String getString(String key) {
        plugin.debugLog("Getting key: " + key);
        plugin.debugLog("Result: " + yaml.getString(key));
        return yaml.getString(key);
    }

    @Override
    public List<String> getStringList(String key) {
        plugin.debugLog("Getting key: " + key);
        plugin.debugLog("Result: " + yaml.getStringList(key));
        return yaml.getStringList(key);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String key) {
        plugin.debugLog("Getting key: " + key);
        plugin.debugLog("Result: " + yaml.getConfigurationSection(key));
        return yaml.getConfigurationSection(key);
    }

    @Override
    public Set<String> getKeys() {
        plugin.debugLog("Getting key list");
        plugin.debugLog("Result: " + yaml.getKeys(false));
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
        plugin.debugLog("Checking key if section: " + key);
        plugin.debugLog("Result: " + yaml.isConfigurationSection(key));
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
        plugin.debugLog("Getting if key exists: " + key);
        plugin.debugLog("Result: " + yaml.contains(key));
        return yaml.contains(key);
    }
}
