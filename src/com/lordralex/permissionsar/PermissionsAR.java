package com.lordralex.permissionsar;

import com.lordralex.permissionsar.configuration.Configuration;
import com.lordralex.permissionsar.permission.utils.Update;
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
    public Logger log;
    private static PermissionManager manager;
    private static PermissionsAR instance;
    private static Configuration config;
    private static Listener listener;

    @Override
    public void onLoad() {
        if (log == null) {
            log = this.getLogger();
        }
        if (instance == null) {
            log.info("Storing instance");
            instance = this;
        }
        String fileLoading = null;
        try {
            log.info("Beginning initial preperations");
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            if (!(new File(getDataFolder(), "config.yml").exists())) {
                this.saveResource("config.yml", true);
            }
            if (!(new File(getDataFolder(), "permissions.yml").exists())) {
                this.saveResource("permissions.yml", true);
            }
            fileLoading = "config";
            configFile = new YamlConfiguration();
            configFile.load(new File(this.getDataFolder(), "config.yml"));
            fileLoading = "permissions";
            permFile = new YamlConfiguration();
            permFile.load(new File(this.getDataFolder(), "permissions.yml"));
            Update update = new Update();
            update.backup();
            update.runUpdate();
            config = new Configuration();
            log.info("Initial preperations complete");
        } catch (Exception e) {
            if (e instanceof InvalidConfigurationException) {
                log.log(Level.SEVERE, "YAML error in your " + fileLoading + " file");
                log.log(Level.SEVERE, ((InvalidConfigurationException) e).getMessage());
            } else {
                log.log(Level.SEVERE, "Error in starting up PermissionsAR (Version " + this.getDescription().getVersion() + ")", e);
            }
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        try {
            if (manager == null) {
                log.info("Creating perms manager");
                manager = new PermissionManager();
            }
            manager.load();
            if (listener == null) {
                log.info("Creating player listener");
                listener = new Listener();
            }
            Bukkit.getPluginManager().registerEvents(listener, this);
        } catch (Exception ex) {
            Logger.getLogger(PermissionsAR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onDisable() {
        manager.unload();
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

    public static Configuration getConfiguration() {
        return config;
    }

    public static Logger getLog() {
        return instance.log;
    }
}
