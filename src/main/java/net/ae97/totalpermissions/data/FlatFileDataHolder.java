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
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class FlatFileDataHolder implements DataHolder {

    private File root;
    private final Map<PermissionType, Map<String, YamlConfiguration>> buffer = new EnumMap<PermissionType, Map<String, YamlConfiguration>>(PermissionType.class);

    @Override
    public void load(InputStream in) throws InvalidConfigurationException {
        throw new UnsupportedOperationException("Loading from an InputStream is not supported by this implentation");
    }

    @Override
    public void load(File file) throws InvalidConfigurationException {
        root = file;
        root.mkdirs();
        buffer.clear();
    }

    @Override
    public void load(String string) throws InvalidConfigurationException {
        root = new File(string);
        root.mkdirs();
        buffer.clear();
    }

    @Override
    public String getString(String key) {
        String[] split = key.split(".", 3);
        if (split.length != 3) {
            return null;
        }
        Map<String, YamlConfiguration> map = buffer.get(PermissionType.getType(split[0]));
        if (map == null) {
            File first = new File(root, split[0]);
            if (!first.exists()) {
                return null;
            }
            map = new HashMap<String, YamlConfiguration>();
            buffer.put(PermissionType.getType(split[0]), map);
        }
        YamlConfiguration section = map.get(split[1]);
        if (section == null) {
            File second = new File(new File(root, split[0]), split[1]);
            if (!second.exists()) {
                return null;
            }
            section = new YamlConfiguration();
            try {
                ((YamlConfiguration) section).load(second);
            } catch (IOException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return null;
            } catch (InvalidConfigurationException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return null;
            }
            map.put(split[1], section);
        }
        return section.getString(split[2]);
    }

    @Override
    public List<String> getStringList(String key) {
        String[] split = key.split(".", 3);
        if (split.length != 3) {
            return null;
        }
        Map<String, YamlConfiguration> map = buffer.get(PermissionType.getType(split[0]));
        if (map == null) {
            File first = new File(root, split[0]);
            if (!first.exists()) {
                return null;
            }
            map = new HashMap<String, YamlConfiguration>();
            buffer.put(PermissionType.getType(split[0]), map);
        }
        YamlConfiguration section = map.get(split[1]);
        if (section == null) {
            File second = new File(new File(root, split[0]), split[1]);
            if (!second.exists()) {
                return null;
            }
            section = new YamlConfiguration();
            try {
                ((YamlConfiguration) section).load(second);
            } catch (IOException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return null;
            } catch (InvalidConfigurationException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return null;
            }
            map.put(split[1], section);
        }
        return section.getStringList(split[2]);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String key) {
        String[] split = key.split(".", 3);
        if (split.length < 2) {
            throw new UnsupportedOperationException("This implentation cannot load a list");
        }
        Map<String, YamlConfiguration> map = buffer.get(PermissionType.getType(split[0]));
        if (map == null) {
            File first = new File(root, split[0]);
            if (!first.exists()) {
                return null;
            }
            map = new HashMap<String, YamlConfiguration>();
            buffer.put(PermissionType.getType(split[0]), map);
        }
        YamlConfiguration section = map.get(split[1]);
        if (section == null) {
            File second = new File(new File(root, split[0]), split[1]);
            if (!second.exists()) {
                return null;
            }
            section = new YamlConfiguration();
            try {
                ((YamlConfiguration) section).load(second);
            } catch (IOException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return null;
            } catch (InvalidConfigurationException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return null;
            }
            map.put(split[1], section);
        }
        if (split.length == 2) {
            return section;
        } else {
            return section.getConfigurationSection(split[2]);
        }
    }

    @Override
    public Set<String> getKeys() {
        Set set = new HashSet<String>();
        set.addAll(Arrays.asList(root.list()));
        return set;
    }

    @Override
    public void set(String key, Object obj) {
        String[] split = key.split(".", 3);
        if (split.length < 2) {
            throw new UnsupportedOperationException("This implentation cannot set " + key);
        }
        if (split.length == 2 && !(obj instanceof YamlConfiguration)) {
            throw new UnsupportedOperationException("This implentation cannot set " + key + " to be a " + obj.getClass().getSimpleName());
        }
        Map<String, YamlConfiguration> map = buffer.get(PermissionType.getType(split[0]));
        if (map == null) {
            map = new HashMap<String, YamlConfiguration>();
        }
        YamlConfiguration cfg;
        if (split.length == 2) {
            cfg = (YamlConfiguration) obj;
        } else {
            cfg = map.get(split[1]);
            if (cfg == null) {
                cfg = new YamlConfiguration();
            }
            cfg.set(split[2], obj);
        }
        map.put(split[1], cfg);
        buffer.put(PermissionType.getType(split[0]), map);
    }

    @Override
    public ConfigurationSection createSection(String key) {
        YamlConfiguration section = new YamlConfiguration();
        String[] split = key.split(".", 3);
        if (split.length <= 1) {
            throw new UnsupportedOperationException("This implentation cannot create a folder section");
        }
        if (split.length > 2) {
            throw new UnsupportedOperationException("This implentation cannot create sub-sections");
        }
        Map<String, YamlConfiguration> map = buffer.get(PermissionType.getType(split[0]));
        if (map == null) {
            File first = new File(root, split[0]);
            if (!first.exists()) {
                first.mkdirs();
            }
            map = new HashMap<String, YamlConfiguration>();
        }
        map.put(split[1], section);
        buffer.put(PermissionType.getType(split[0]), map);
        return section;
    }

    @Override
    public boolean isConfigurationSection(String key) {
        String[] split = key.split(".", 3);
        Map<String, YamlConfiguration> map = buffer.get(PermissionType.getType(split[0]));
        if (map == null) {
            File first = new File(root, split[0]);
            if (!first.exists()) {
                return false;
            }
            map = new HashMap<String, YamlConfiguration>();
            buffer.put(PermissionType.getType(split[0]), map);
        }
        if (split.length == 1) {
            return true;
        }
        YamlConfiguration section = map.get(split[1]);
        if (section == null) {
            File second = new File(new File(root, split[0]), split[1]);
            if (!second.exists()) {
                return false;
            }
            section = new YamlConfiguration();
            try {
                ((YamlConfiguration) section).load(second);
            } catch (IOException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return false;
            } catch (InvalidConfigurationException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return false;
            }
            map.put(split[1], section);
        }
        if (split.length == 2) {
            return true;
        }
        return section.isConfigurationSection(split[2]);
    }

    @Override
    public void save(PermissionType type, String name) throws IOException {
        new File(root, type.toString()).mkdirs();
        Map<String, YamlConfiguration> map = buffer.get(type);
        if (map == null) {
            return;
        }
        YamlConfiguration cfg = map.get(name);
        if (cfg == null) {
            return;
        }
        cfg.save(new File(new File(root, type.toString()), name + ".yml"));
    }

    @Override
    public boolean contains(String key) {
        String[] split = key.split(".", 3);
        Map<String, YamlConfiguration> map = buffer.get(PermissionType.getType(split[0]));
        if (map == null) {
            File first = new File(root, split[0]);
            if (!first.exists()) {
                return false;
            }
            map = new HashMap<String, YamlConfiguration>();
            buffer.put(PermissionType.getType(split[0]), map);
        }
        if (split.length == 1) {
            return true;
        }
        YamlConfiguration section = map.get(split[1]);
        if (section == null) {
            File second = new File(new File(root, split[0]), split[1]);
            if (!second.exists()) {
                return false;
            }
            section = new YamlConfiguration();
            try {
                ((YamlConfiguration) section).load(second);
            } catch (IOException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return false;
            } catch (InvalidConfigurationException ex) {
                TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Config for key " + key + "generated an error", ex);
                return false;
            }
            map.put(split[1], section);
        }
        if (split.length == 2) {
            return true;
        }
        return section.contains(split[2]);
    }
}
