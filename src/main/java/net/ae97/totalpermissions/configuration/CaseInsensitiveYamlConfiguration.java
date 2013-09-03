/*
 * Copyright (C) 2013 Lord_Ralex
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
package net.ae97.totalpermissions.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class CaseInsensitiveYamlConfiguration extends YamlConfiguration {

    @Override
    public void addDefault(String path, Object value) {
        path = path.toLowerCase();
        super.addDefault(path, value);
    }

    @Override
    public void addDefaults(Map<String, Object> defaults) {
        Map<String, Object> clone = new HashMap<String, Object>();
        for (Entry<String, Object> entry : defaults.entrySet()) {
            clone.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        super.addDefaults(clone);
    }

    @Override
    public boolean contains(String path) {
        path = path.toLowerCase();
        return super.contains(path);
    }

    @Override
    public ConfigurationSection createSection(String path) {
        path = path.toLowerCase();
        return super.createSection(path);
    }

    @Override
    public ConfigurationSection createSection(String path, Map<?, ?> map) {
        path = path.toLowerCase();
        return super.createSection(path, map);
    }

    @Override
    public Object get(String path) {
        path = path.toLowerCase();
        return super.get(path);
    }

    @Override
    public Object get(String path, Object def) {
        path = path.toLowerCase();
        return super.get(path, def);
    }

    @Override
    public boolean getBoolean(String path) {
        path = path.toLowerCase();
        return super.getBoolean(path);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        path = path.toLowerCase();
        return super.getBoolean(path, def);
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        path = path.toLowerCase();
        return super.getBooleanList(path);
    }

    @Override
    public List<Byte> getByteList(String path) {
        path = path.toLowerCase();
        return super.getByteList(path);
    }

    @Override
    public List<Character> getCharacterList(String path) {
        path = path.toLowerCase();
        return super.getCharacterList(path);
    }

    @Override
    public Color getColor(String path) {
        path = path.toLowerCase();
        return super.getColor(path);
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        super.loadFromString(contents);
        updateMap();
    }

    @Override
    public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        super.load(file);
        updateMap();
    }

    @Override
    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        super.load(stream);
        updateMap();
    }

    @Override
    public void load(String file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        super.load(file);
        updateMap();
    }

    @Override
    public boolean isSet(String path) {
        path = path.toLowerCase();
        return super.isSet(path);
    }

    @Override
    public void set(String path, Object value) {
        path = path.toLowerCase();
        super.set(path, value);
    }

    @Override
    public String getString(String path) {
        path = path.toLowerCase();
        return super.getString(path);
    }

    @Override
    public String getString(String path, String def) {
        path = path.toLowerCase();
        return super.getString(path, def);
    }

    @Override
    public boolean isString(String path) {
        path = path.toLowerCase();
        return super.isString(path);
    }

    @Override
    public int getInt(String path) {
        path = path.toLowerCase();
        return super.getInt(path);
    }

    @Override
    public int getInt(String path, int def) {
        path = path.toLowerCase();
        return super.getInt(path, def);
    }

    @Override
    public boolean isInt(String path) {
        path = path.toLowerCase();
        return super.isInt(path);
    }

    @Override
    public boolean isBoolean(String path) {
        path = path.toLowerCase();
        return super.isBoolean(path);
    }

    @Override
    public double getDouble(String path) {
        path = path.toLowerCase();
        return super.getDouble(path);
    }

    @Override
    public double getDouble(String path, double def) {
        path = path.toLowerCase();
        return super.getDouble(path, def);
    }

    @Override
    public boolean isDouble(String path) {
        path = path.toLowerCase();
        return super.isDouble(path);
    }

    @Override
    public long getLong(String path) {
        path = path.toLowerCase();
        return super.getLong(path);
    }

    @Override
    public long getLong(String path, long def) {
        path = path.toLowerCase();
        return super.getLong(path, def);
    }

    @Override
    public boolean isLong(String path) {
        path = path.toLowerCase();
        return super.isLong(path);
    }

    @Override
    public List<?> getList(String path) {
        path = path.toLowerCase();
        return super.getList(path);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        path = path.toLowerCase();
        return super.getList(path, def);
    }

    @Override
    public boolean isList(String path) {
        path = path.toLowerCase();
        return super.isList(path);
    }

    @Override
    public List<String> getStringList(String path) {
        path = path.toLowerCase();
        return super.getStringList(path);
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        path = path.toLowerCase();
        return super.getIntegerList(path);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        path = path.toLowerCase();
        return super.getDoubleList(path);
    }

    @Override
    public List<Float> getFloatList(String path) {
        path = path.toLowerCase();
        return super.getFloatList(path);
    }

    @Override
    public List<Long> getLongList(String path) {
        path = path.toLowerCase();
        return super.getLongList(path);
    }

    @Override
    public List<Short> getShortList(String path) {
        path = path.toLowerCase();
        return super.getShortList(path);
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        path = path.toLowerCase();
        return super.getMapList(path);
    }

    @Override
    public Vector getVector(String path) {
        path = path.toLowerCase();
        return super.getVector(path);
    }

    @Override
    public Vector getVector(String path, Vector def) {
        path = path.toLowerCase();
        return super.getVector(path, def);
    }

    @Override
    public boolean isVector(String path) {
        path = path.toLowerCase();
        return super.isVector(path);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String path) {
        path = path.toLowerCase();
        return super.getOfflinePlayer(path);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        path = path.toLowerCase();
        return super.getOfflinePlayer(path, def);
    }

    @Override
    public boolean isOfflinePlayer(String path) {
        path = path.toLowerCase();
        return super.isOfflinePlayer(path);
    }

    @Override
    public ItemStack getItemStack(String path) {
        path = path.toLowerCase();
        return super.getItemStack(path);
    }

    @Override
    public ItemStack getItemStack(String path, ItemStack def) {
        path = path.toLowerCase();
        return super.getItemStack(path, def);
    }

    @Override
    public boolean isItemStack(String path) {
        path = path.toLowerCase();
        return super.isItemStack(path);
    }

    @Override
    public Color getColor(String path, Color def) {
        path = path.toLowerCase();
        return super.getColor(path, def);
    }

    @Override
    public boolean isColor(String path) {
        path = path.toLowerCase();
        return super.isColor(path);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        path = path.toLowerCase();
        return super.getConfigurationSection(path);
    }

    @Override
    public boolean isConfigurationSection(String path) {
        path = path.toLowerCase();
        return super.isConfigurationSection(path);
    }

    @Override
    protected Object getDefault(String path) {
        path = path.toLowerCase();
        return super.getDefault(path);
    }

    protected void updateMap() {
        Map<String, Object> clone = new HashMap<String, Object>();
        for (Entry<String, Object> entry : map.entrySet()) {
            clone.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        map.clear();
        map.putAll(clone);
    }
}
