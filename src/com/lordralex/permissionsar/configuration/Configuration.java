package com.lordralex.permissionsar.configuration;

import com.lordralex.permissionsar.PermissionsAR;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {

    FileConfiguration config;

    public Configuration() {
        //config = YamlConfiguration.loadConfiguration(new File("plugins/ListManager/config.yml"));
        config = PermissionsAR.getConfigFile(); //loads the file from the plugin's view.
    }

    public void loadDefaults() {
        if (!config.contains("random.value")) {
            config.set("random.value", "derp");
        }
        save();
    }

    public void save() {
        try {
            config.save(new File(PermissionsAR.getPlugin().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            PermissionsAR.log.log(Level.SEVERE, null, e);
        }
    }

    public String getRandomValue() {
        return config.getString("random.value");
    }

    public void setRandomValue(String value) {
        config.set("random.value", value);
    }
}