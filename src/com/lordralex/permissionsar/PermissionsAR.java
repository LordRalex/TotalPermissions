package com.lordralex.permissionsar;

import com.lordralex.permissionsar.configuration.Configuration;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public final class PermissionsAR extends JavaPlugin {

    private static FileConfiguration permFile;
    private static FileConfiguration configFile;
    public static final Logger log = Bukkit.getLogger();
    private static PermissionManager manager;
    private static PermissionsAR instance;
    private static Configuration config;

    @Override
    public void onLoad() {
        instance = this;
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
            config = new Configuration();
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
        manager = new PermissionManager();
    }

    @Override
    public void onDisable() {
    }

    /**
     * Gets the {@link PermissionManager} for this plugin.
     *
     * @return The {@link PermissionManager} that this is using
     *
     * @since 1.0
     */
    public static PermissionManager getManager() {
        return manager;
    }

    /**
     * Gets the permissions file. This does not load the file, this only
     * provides the stored object.
     *
     * @return The perm file in the FileConfiguration form.
     *
     * @since 1.0
     */
    public static FileConfiguration getPermFile() {
        return permFile;
    }

    /**
     * Gets the configuration file. This does not load the file, this only
     * provides the stored object.
     *
     * @return The configuration file in the FileConfiguration form.
     *
     * @since 1.0
     */
    public static FileConfiguration getConfigFile() {
        return configFile;
    }

    /**
     * Gets the instance of the plugin.
     *
     * @return Instance of the plugin
     */
    public static PermissionsAR getPlugin() {
        return instance;
    }
}
