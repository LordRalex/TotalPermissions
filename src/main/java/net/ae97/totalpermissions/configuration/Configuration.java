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
package net.ae97.totalpermissions.configuration;

import net.ae97.totalpermissions.TotalPermissions;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @since 0.1
 * @author Lord_Ralex
 * @version 0.1
 */
public class Configuration {

    FileConfiguration config;
    File configPath;
    protected final TotalPermissions plugin;

    public Configuration(TotalPermissions p) {
        plugin = p;
        config = plugin.getConfigFile();
        configPath = new File(plugin.getDataFolder(), "config.yml");
    }

    /**
     * Loads the default config, adding missing keys if needed
     */
    public void loadDefaults() {
        if (!configPath.exists()) {
            plugin.saveDefaultConfig();
        }

        //whether this operates in a strict mode, ignoring perms given by default. True means use strict.
        if (!config.contains("strict-mode")) {
            config.set("strict-mode", false);
        }
        if (!config.contains("reflection.starperm")) {
            config.set("refection.starperm", false);
        }
        if (!config.contains("reflection.debug")) {
            config.set("refection.debug", false);
        }
        if (!config.contains("update-check")) {
            config.set("update-check", true);
        }
        if (!config.contains("angry-debug")) {
            config.set("angry-debug", false);
        }
        if (!config.contains("permissions.formatter")) {
            config.set("permissions.formatter", true);
        }
        if (!config.contains("permissions.updater")) {
            config.set("permissions.updater", true);
        }
        if (!config.contains("language")) {
            config.set("language", "en_US");
        }

        try {
            config.save(configPath);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, null, e);
        }
    }

    /**
     * Gets the String from the config with the specified path.
     *
     * @param path Path to String
     * @return The resulting String from this path, defaults to null.
     *
     * @since 0.1
     */
    public String getString(String path) {
        return config.getString(path, null);
    }

    /**
     * Gets the integer from the config with the specified path.
     *
     * @param path Path to integer
     * @return The resulting integer from this path, defaults to 0.
     *
     * @since 0.1
     */
    public int getInt(String path) {
        return config.getInt(path, 0);
    }

    /**
     * Gets the boolean from the config with the specified path.
     *
     * @param path Path to boolean
     * @return The resulting boolean from this path, defaults to false.
     *
     * @since 0.1
     */
    public boolean getBoolean(String path) {
        return config.getBoolean(path, false);
    }

    /**
     * Disables reflection.
     */
    public void disableReflection() {
        ConfigurationSection section = config.getConfigurationSection("reflection");
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            section.set(key, false);
        }
    }
}
