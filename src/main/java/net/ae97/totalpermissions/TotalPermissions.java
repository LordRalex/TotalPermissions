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
package net.ae97.totalpermissions;

import net.ae97.totalpermissions.logger.DebugLogFormatter;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import net.ae97.totalpermissions.commands.CommandHandler;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.YamlDataHolder;
import net.ae97.totalpermissions.lang.Cipher;
import net.ae97.totalpermissions.listeners.TPListener;
import net.ae97.totalpermissions.mcstats.Metrics;
import net.ae97.totalpermissions.sql.PermissionPersistance;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import net.ae97.totalpermissions.data.DataType;
import net.ae97.totalpermissions.data.FlatFileDataHolder;
import net.ae97.totalpermissions.data.MySQLDataHolder;
import net.ae97.totalpermissions.data.SharedDataHolder;
import net.ae97.totalpermissions.lang.Lang;
import net.ae97.totalpermissions.updater.Updater;
import net.ae97.totalpermissions.updater.Updater.UpdateType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @version 0.2
 * @author Lord_Ralex
 * @since 0.1
 */
public final class TotalPermissions extends JavaPlugin {

    protected String BUKKIT_VERSION = "NONE";
    protected final String[] ACCEPTABLE_VERSIONS = new String[]{
        "v1_6_R3",
        "v1_6_R2",
        "v1_6_R1",
        "v1_5_R3",
        "v1_5_R2",
        "v1_5_R1",
        "v1_4_R1"
    };
    protected DataHolder dataHolder;
    protected PermissionManager manager;
    protected TPListener listener;
    protected Metrics metrics;
    protected CommandHandler commands;
    protected boolean loadingFailed = false;

    @Override
    public void onLoad() {
        try {
            getLogger().info("Beginning initial preperations");
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }

            new File(getDataFolder(), "logs").mkdirs();
            FileHandler debugHandler = new FileHandler(new File(new File(getDataFolder(), "logs"), "totalpermissions.log").getPath(), 0, 1, true);
            debugHandler.setFormatter(new DebugLogFormatter());
            getLogger().addHandler(debugHandler);
            debugLog("---------");

            if (!(new File(getDataFolder(), "config.yml").exists())) {
                debugLog("Saving default config");
                saveResource("config.yml", true);
            }
            if (!(new File(getDataFolder(), "permissions.yml").exists())) {
                debugLog("Saving default permissions.yml");
                saveResource("permissions.yml", true);
            }

            Lang.setLanguageConfig(YamlConfiguration.loadConfiguration(this.getResource("lang/" + getConfig().getString("language", "en_US") + ".yml")));

            for (String version : ACCEPTABLE_VERSIONS) {
                try {
                    Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftHumanEntity");
                    BUKKIT_VERSION = version;
                    break;
                } catch (ClassNotFoundException e) {
                }
            }
            debugLog("Detected bukkit version: " + BUKKIT_VERSION);
            if (BUKKIT_VERSION.equalsIgnoreCase("NONE")) {
                log(Level.SEVERE, Lang.MAIN_BADVERSION1);
                log(Level.SEVERE, Lang.MAIN_BADVERSION2);
                getConfig().set("reflection.debug", false);
                getConfig().set("reflection.starperm", false);
            }

            String storageType = getConfig().getString("storage", "yaml");
            DataType type = DataType.valueOf(storageType.toUpperCase());
            if (type == null) {
                log(Level.SEVERE, Lang.MAIN_STORAGEERROR, storageType);
                type = DataType.YAML;
            }
            debugLog("Creating data holder");
            debugLog("Storage type to load: " + storageType);
            switch (type) {
                case MYSQL: {
                    if (getDescription().isDatabaseEnabled()) {
                        debugLog("Using builtin system");
                        dataHolder = new MySQLDataHolder(getDatabase());
                    } else {
                        debugLog("Making our own");
                        dataHolder = new MySQLDataHolder(null);
                    }
                }
                break;

                case FLAT: {
                    dataHolder = new FlatFileDataHolder(new File(getDataFolder(), "data"));
                }
                break;

                case SHARED: {
                    dataHolder = new SharedDataHolder(this);
                }
                break;

                //default to use YAML if nothing is set up
                default:
                case YAML: {
                    dataHolder = new YamlDataHolder(new File(getDataFolder(), "permissions.yml"));
                }
                break;
            }

            debugLog("Loading permissions setup");
            try {
                dataHolder.setup();
            } catch (InvalidConfigurationException e) {
                log(Level.SEVERE, Lang.MAIN_YAMLERROR);
                getLogger().log(Level.SEVERE, "-> " + e.getMessage());
                debugLog(e);
                getLogger().log(Level.WARNING, Lang.MAIN_LOADBACKUP.getMessage());
                try {
                    if (dataHolder instanceof YamlDataHolder) {
                        dataHolder = new YamlDataHolder(new File(getLastBackupFolder(), "permissions.yml"));
                    } else {
                        dataHolder = new YamlDataHolder(new File(getDataFolder(), "permissions.yml"));
                    }
                    dataHolder.setup();
                    log(Level.WARNING, Lang.MAIN_LOADED1);
                    log(Level.WARNING, Lang.MAIN_LOADED2);
                } catch (InvalidConfigurationException e2) {
                    log(Level.SEVERE, Lang.MAIN_LOADFAILED1);
                    log(Level.SEVERE, Lang.MAIN_LOADFAILED2);
                    throw e2;
                }
            }

            getLogger().info("Initial preperations complete");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, Lang.MAIN_ERROR.getMessage(getName(), getDescription().getVersion()), e);
            loadingFailed = true;
        }
    }

    @Override
    public void onEnable() {
        try {
            if (loadingFailed) {
                log(Level.SEVERE, Lang.MAIN_LOADCRASH);
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            if (getConfig().getBoolean("update.check", true)) {
                UpdateType updatetype = UpdateType.NO_DOWNLOAD;
                if (getConfig().getBoolean("update.download", true)) {
                    updatetype = UpdateType.DEFAULT;
                }

                Updater updater = new Updater(this, "totalpermissions", this.getFile(), updatetype, true);
                updater.checkForUpdate();
            }

            debugLog("Creating permission manager");
            manager = new PermissionManager(this);
            debugLog("Loading permission manager");
            manager.load();

            debugLog("Creating listener");
            listener = new TPListener(this);
            debugLog("Registering listener");
            Bukkit.getPluginManager().registerEvents(listener, this);

            debugLog("Creating command handler");
            commands = new CommandHandler(this);
            debugLog("Registering command handler");
            getCommand("totalpermissions").setExecutor(commands);

            debugLog("Loading up metrics");
            metrics = new Metrics(this);
            if (metrics.start()) {
                log(Level.INFO, Lang.MAIN_METRICS);
            }
        } catch (Exception e) {
            if (e instanceof InvalidConfigurationException) {
                log(Level.SEVERE, Lang.MAIN_YAMLERROR);
                getLogger().log(Level.SEVERE, ((InvalidConfigurationException) e).getMessage());
            } else {
                getLogger().log(Level.SEVERE, Lang.MAIN_ERROR.getMessage(getName(), getDescription().getVersion()), e);
            }
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        debugLog("Disabling manager");
        if (manager != null) {
            debugLog("Unloading manager");
            manager.unload();
        }
        Handler[] handlers = getLogger().getHandlers();
        for (Handler handle : handlers) {
            if (handle.getFormatter() instanceof DebugLogFormatter) {
                getLogger().removeHandler(handle);
            }
        }
        File[] fileList = new File(getDataFolder(), "logs").listFiles();
        for (File file : fileList) {
            if (file.getName().endsWith("lck")) {
                file.delete();
            }
        }
    }

    /**
     * Gets the {@link PermissionManager} for this plugin.
     *
     * @return The {@link PermissionManager} that this is using
     *
     * @since 0.1
     */
    public PermissionManager getManager() {
        return manager;
    }

    /**
     * Gets the permissions file. This does not load the file, this only
     * provides the stored object.
     *
     * @return The perm file in the DataHolder form.
     *
     * @since 0.1
     * @deprecated Use getDataHolder() instead
     */
    public DataHolder getPermFile() {
        return dataHolder;
    }

    /**
     * Gets the currently active DataHolder
     *
     * @since 0.3
     * @return The DataHolder currently loaded
     */
    public DataHolder getDataHolder() {
        return dataHolder;
    }

    /**
     * Gets the instance of the plugin.
     *
     * @return Instance of the plugin
     *
     * @since 0.1
     */
    public static TotalPermissions getPlugin() {
        return (TotalPermissions) Bukkit.getPluginManager().getPlugin("TotalPermissions");
    }

    /**
     * Gets the listener in use
     *
     * @return The Listener
     *
     * @since 0.1
     */
    public TPListener getListener() {
        return listener;
    }

    /**
     * Returns the (@link Cipher) that is loaded
     * <strong>AS OF 0.4, THIS RETURNS NULL</strong>
     *
     * @return NULL
     *
     * @since 0.2
     *
     * @deprecated Use {@link Lang} class instead. This <strong>will</strong>
     * return null.
     */
    public Cipher getLangFile() {
        return null;
    }

    /**
     * Returns the backup folder
     *
     * @return The backup folder
     *
     * @since 0.1
     */
    public File getBackupFolder() {
        return new File(getDataFolder(), "backups");
    }

    /**
     * Returns the location of the last folder used to back up perms. This may
     * not be exact, but uses the folder numbers.
     *
     * @return The File instance of the folder that contains the last backed up
     * files
     *
     * @since 0.1
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
        File dir = new File(getBackupFolder(), Integer.toString(highest));
        if (dir.listFiles() == null || dir.listFiles().length == 0) {
            dir.delete();
            return getLastBackupFolder();
        }

        return new File(getBackupFolder(), Integer.toString(highest));
    }

    /**
     * Gets the Craftbukkit package version that the server is using
     *
     * @return Package CB/NMS files are in
     *
     * @since 0.1
     */
    public String getBukkitVersion() {
        return BUKKIT_VERSION;
    }

    /**
     * Gets the CommandHandler that is in use by this plugin
     *
     * @return The CommandHandler in use
     *
     * @since 0.1
     */
    public CommandHandler getCommandHandler() {
        return commands;
    }

    public void debugLog(Object... message) {
        for (Object m : message) {
            getLogger().log(Level.FINER, m.toString());
        }
    }

    /**
     * Gets the debug mode of the plugin. If this is true, the plugin is in
     * debug mode.
     *
     * @return True if in debug, otherwise false
     *
     * @since 0.1
     */
    public boolean isDebugMode() {
        return getConfig().getBoolean("angry-debug", false);
    }

    public void installDatabase(EbeanServer ebeanServer) {
        if (ebeanServer == null) {
            return;
        }
        try {
            ebeanServer.find(PermissionPersistance.class).findRowCount();
        } catch (PersistenceException ex) {
            debugLog("Installing database for " + getName() + " due to first time usage (I hope)");
            debugLog(ex);
            if (ebeanServer == getDatabase()) {
                debugLog("Using Bukkit integrated installer");
                installDDL();
            } else {
                debugLog("Using custom installer");
                SpiEbeanServer serv = (SpiEbeanServer) ebeanServer;
                DdlGenerator gen = serv.getDdlGenerator();
                gen.runScript(false, gen.generateCreateDdl());
            }
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(PermissionPersistance.class);
        return list;
    }

    @Override
    public File getFile() {
        return super.getFile();
    }

    public void log(Level level, Lang message, Object... args) {
        getLogger().log(level, message.getMessage(args));
    }
}
