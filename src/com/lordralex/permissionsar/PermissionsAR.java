package com.lordralex.permissionsar;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Joshua
 */
public final class PermissionsAR extends JavaPlugin {

    private static FileConfiguration permFile;
    private static FileConfiguration configFile;
    public static final Logger log = Bukkit.getLogger();
    private static PermissionManager manager;

    @Override
    public void onLoad() {
        try {
            log.info("[PAR] Beginning initial preperations");
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            if (!(new File(getDataFolder(), "config.yml").exists())) {
                this.saveResource("config.yml", true);
            }
            if (!(new File(getDataFolder(), "permissions.yml").exists())) {
                this.saveResource("permissions.yml", true);
            }
            configFile = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "config.yml"));
            permFile = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "permissions.yml"));
            log.info("[PAR] Initial preperations complete");
        } catch (Exception e) {
            if (e instanceof InvalidConfigurationException) {
                log.log(Level.SEVERE, "[PAR] YAML error in your file", e);
            } else {
                log.log(Level.SEVERE, "Error in starting up PermissionsAR (Version " + this.getDescription().getVersion() + ")", e);
            }
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        manager = new PermissionManager(this);
    }

    @Override
    public void onDisable() {
    }

    public static PermissionManager getManager() {
        return manager;
    }

    public static FileConfiguration getPermFile() {
        return permFile;
    }

    public static FileConfiguration getConfigFile() {
        return configFile;
    }
}
