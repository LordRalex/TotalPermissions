package com.lordralex.totalpermissions;

import com.lordralex.totalpermissions.commands.CommandHandler;
import com.lordralex.totalpermissions.configuration.Configuration;
import com.lordralex.totalpermissions.permission.utils.Update;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.lr.mcstats.Metrics;

/**
 * @version 1.0
 * @author Lord_Ralex
 * @since 1.0
 */
public final class TotalPermissions extends JavaPlugin {

    protected static FileConfiguration permFile;
    private static FileConfiguration configFile;
    protected static Logger log;
    protected static PermissionManager manager;
    protected static TotalPermissions instance;
    protected static Configuration config;
    protected static Listener listener;
    protected static Metrics metrics;
    protected CommandHandler commands;

    @Override
    public void onLoad() {
        if (log == null) {
            log = this.getLogger();
        }
        if (instance == null) {
            log.info("Storing instance");
            instance = this;
        }
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

            configFile = new YamlConfiguration();
            configFile.load(new File(this.getDataFolder(), "config.yml"));
            try {
                permFile = new YamlConfiguration();
                permFile.load(new File(this.getDataFolder(), "permissions.yml"));
                Update update = new Update();
                update.backup(true);
                update.runUpdate();
            } catch (InvalidConfigurationException e) {
                log.log(Level.SEVERE, "YAML error in your permissions.yml file");
                log.log(Level.SEVERE, e.getMessage());
                log.log(Level.WARNING, "Attempting to load backup perms");
                try {
                    permFile = new YamlConfiguration();
                    permFile.load(new File(this.getLastBackupFolder(), "permissions.yml"));
                    log.log(Level.WARNING, "Loaded an older perms files. Please repair your permissions before doing anything else though.");
                } catch (InvalidConfigurationException e2) {
                    log.log(Level.SEVERE, "Errors in the last backup up, cannot load");
                    log.log(Level.SEVERE, "Shutting down perms plugin as there is nothing I can do ;~;");
                    throw e2;
                }
            }
            config = new Configuration();
            log.info("Initial preperations complete");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error in starting up " + getName() + " (Version " + this.getDescription().getVersion() + ")", e);
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        try {
            if (manager == null) {
                log.config("Creating perms manager");
                manager = new PermissionManager();
            }
            manager.load();
            if (listener == null) {
                log.config("Creating player listener");
                listener = new Listener();
            }
            Bukkit.getPluginManager().registerEvents(listener, this);
            if (commands == null) {
                log.config("Creating command handler");
                commands = new CommandHandler();
            }
            getCommand("totalpermissions").setExecutor(commands);
            metrics = new Metrics(this);
            if (metrics.start()) {
                log.info("Plugin Metrics is on, you can opt-out in the PluginMetrics config");
            }
        } catch (Exception e) {
            if (e instanceof InvalidConfigurationException) {
                log.log(Level.SEVERE, "YAML error in your permissions file");
                log.log(Level.SEVERE, ((InvalidConfigurationException) e).getMessage());
            } else {
                log.log(Level.SEVERE, "Error in starting up " + getName() + " (Version " + this.getDescription().getVersion() + ")", e);
            }
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (manager != null) {
            manager.unload();
        }
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
    public static TotalPermissions getPlugin() {
        return instance;
    }

    public static Listener getListener() {
        return listener;
    }

    /**
     * Returns the {@link Configuration} that is loaded
     *
     * @return The {@link Configuration} in use
     *
     * @since 1.0
     */
    public static Configuration getConfiguration() {
        return config;
    }

    /**
     * Returns the Logger instance in use by the plugin
     *
     * @return The instance of the logger
     *
     * @since 1.0
     */
    public static Logger getLog() {
        return instance.log;
    }

    /**
     * Returns the backup folder
     *
     * @return The backup folder
     *
     * @since 1.0
     */
    public File getBackupFolder() {
        return new File(this.getDataFolder(), "backup");
    }

    /**
     * Returns the location of the last folder used to back up perms. This may
     * not be exact, but uses the folder numbers.
     *
     * @return The File instance of the folder that contains the last backed up
     * files
     *
     * @since 1.0
     */
    public File getLastBackupFolder() {
        int highest = 0;
        File[] files = getBackupFolder().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file == null) {
                    continue;
                }
                if (!file.isDirectory()) {
                    continue;
                }
                try {
                    int num = Integer.parseInt(file.getName());
                    if (highest < num) {
                        highest = num;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return new File(getBackupFolder(), Integer.toString(highest));
    }
}
