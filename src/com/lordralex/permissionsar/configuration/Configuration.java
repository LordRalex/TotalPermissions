package com.lordralex.permissionsar.configuration;

import com.lordralex.permissionsar.PermissionsAR;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {

    FileConfiguration config;
    File configPath;

    public Configuration() {
        config = PermissionsAR.getConfigFile(); //loads the file from the plugin's view.
        configPath = new File(PermissionsAR.getPlugin().getDataFolder(), "config.yml");
    }

    public void loadDefaults() {
        if (!configPath.exists()) {
            PermissionsAR.getPlugin().saveDefaultConfig();
        }
        
        //whether this operates in a strict mode, ignoring perms given by default. True means use strict.
        if (!config.contains("strict-mode")) {
            config.set("strict-mode", false);
        }
        
        try {
            config.save(configPath);
        } catch (IOException e) {
            PermissionsAR.log.log(Level.SEVERE, null, e);
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
}