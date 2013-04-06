/*
 * Copyright (C) 2013 LordRalex
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
package com.lordralex.totalpermissions.configuration;

import com.lordralex.totalpermissions.TotalPermissions;
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

    public Configuration() {
        config = TotalPermissions.getPlugin().getConfigFile(); //loads the file from the plugin's view.
        configPath = new File(TotalPermissions.getPlugin().getDataFolder(), "config.yml");
    }

    public void loadDefaults() {
        if (!configPath.exists()) {
            TotalPermissions.getPlugin().saveDefaultConfig();
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
        if (!config.contains("language")) {
            config.set("language", "en_US");
        }

        try {
            config.save(configPath);
        } catch (IOException e) {
            TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, null, e);
        }
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public void disableRefection() {
        ConfigurationSection section = config.getConfigurationSection("reflection");
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            section.set(key, false);
        }
    }
}
