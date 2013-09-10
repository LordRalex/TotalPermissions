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

import com.avaje.ebean.EbeanServer;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import net.ae97.totalpermissions.commands.CommandHandler;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.YamlDataHolder;
import net.ae97.totalpermissions.lang.Cipher;
import net.ae97.totalpermissions.listeners.TPListener;
import net.ae97.totalpermissions.mcstats.Metrics;
import net.ae97.totalpermissions.runnable.UpdateRunnable;
import net.ae97.totalpermissions.sql.PermissionPersistance;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import net.ae97.totalpermissions.data.DataType;
import net.ae97.totalpermissions.data.FlatFileDataHolder;
import net.ae97.totalpermissions.data.MySQLDataHolder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @version 0.2
 * @author Lord_Ralex
 * @since 0.1
 */
public final class TotalPermissions extends JavaPlugin {

    private String BUKKIT_VERSION = "NONE";
    private final String[] ACCEPTABLE_VERSIONS = new String[]{
        "v1_6_R2",
        "v1_6_R1",
        "v1_5_R3",
        "v1_5_R2",
        "v1_5_R1",
        "v1_4_R1"
    };
    protected DataHolder permFile;
    protected PermissionManager manager;
    protected TPListener listener;
    protected Metrics metrics;
    protected CommandHandler commands;
    protected Cipher cipher;
    private boolean loadingFailed = false;

    @Override
    public void onLoad() {
        try {
            getLogger().info("Beginning initial preperations");
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            if (!(new File(getDataFolder(), "config.yml").exists())) {
                saveResource("config.yml", true);
            }
            if (!(new File(getDataFolder(), "permissions.yml").exists())) {
                saveResource("permissions.yml", true);
            }

            cipher = new Cipher(this, getConfig().getString("language", "en_US"));

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
                getLogger().severe(getLangFile().getString("main.bad-version1"));
                getLogger().severe(getLangFile().getString("main.bad-version2"));
                getConfig().set("reflection.debug", false);
                getConfig().set("reflection.starperm", false);
            }

            String storageType = getConfig().getString("storage", "yaml");
            DataType type = DataType.valueOf(storageType.toUpperCase());
            if (type == null) {
                getLogger().severe("Could not determine type of data storage from " + storageType + "! Default to YAML");
                type = DataType.YAML;
            }
            debugLog("Storage type to load: " + storageType);
            switch (type) {
                case MYSQL: {
                    if (getDescription().isDatabaseEnabled()) {
                        getLogger().info("Using builtin system");
                        permFile = new MySQLDataHolder(this.getDatabase());
                    } else {
                        getLogger().info("Making our own");
                        permFile = new MySQLDataHolder(null);
                    }
                }
                break;

                case FLAT: {
                    permFile = new FlatFileDataHolder(new File(getDataFolder(), "data"));
                }
                break;

                //default to use YAML if nothing is set up
                default:
                case YAML: {
                    permFile = new YamlDataHolder(new File(this.getDataFolder(), "permissions.yml"));
                }
                break;
            }
            getLogger().info("Loading permissions setup");
            try {
                permFile.setup();
            } catch (InvalidConfigurationException e) {
                getLogger().log(Level.SEVERE, getLangFile().getString("main.yaml-error"));
                getLogger().log(Level.SEVERE, "-> " + e.getMessage());
                getLogger().log(Level.WARNING, getLangFile().getString("main.load-backup"));
                try {
                    permFile = new YamlDataHolder(new File(getLastBackupFolder(), "permissions.yml"));
                    permFile.setup();
                    getLogger().log(Level.WARNING, getLangFile().getString("main.loaded1"));
                    getLogger().log(Level.WARNING, getLangFile().getString("main.loaded2"));
                } catch (InvalidConfigurationException e2) {
                    getLogger().log(Level.SEVERE, getLangFile().getString("main.load-failed1"));
                    getLogger().log(Level.SEVERE, getLangFile().getString("main.load-failed2"));
                    throw e2;
                }
            }

            getLogger().info("Initial preperations complete");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, getLangFile().getString("main.error", getName(), getDescription().getVersion()), e);
            loadingFailed = true;
        }
    }

    @Override
    public void onEnable() {
        try {
            if (loadingFailed) {
                getLogger().log(Level.SEVERE, getLangFile().getString("main.loadcrash"));
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            if (getConfig().getBoolean("update-check", true)) {
                Bukkit.getScheduler().runTaskLater(this, new UpdateRunnable(this), 1);
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
                getLogger().info(getLangFile().getString("main.metrics"));
            }
        } catch (Exception e) {
            if (e instanceof InvalidConfigurationException) {
                getLogger().log(Level.SEVERE, getLangFile().getString("main.yaml-error"));
                getLogger().log(Level.SEVERE, ((InvalidConfigurationException) e).getMessage());
            } else {
                getLogger().log(Level.SEVERE, getLangFile().getString("main.error", getName(), getDescription().getVersion()), e);
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
    }

    /**
     * Gets the {@link PermissionManager} for this plugin.
     *
     * @return The {@link PermissionManager} that this is using
     *
     * @since 0.1
     */
    public PermissionManager getManager() {
        return this.manager;
    }

    /**
     * Gets the permissions file. This does not load the file, this only
     * provides the stored object.
     *
     * @return The perm file in the DataHolder form.
     *
     * @since 0.1
     */
    public DataHolder getPermFile() {
        return this.permFile;
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
        return this.listener;
    }

    /**
     * Returns the (@link Cipher) that is loaded
     *
     * @return the (@link Cipher) in use
     *
     * @since 0.2
     */
    public Cipher getLangFile() {
        return this.cipher;
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
        return this.commands;
    }

    public void debugLog(Object... message) {
        if (!isDebugMode()) {
            return;
        }
        for (Object m : message) {
            getLogger().info("[Debug] " + m.toString());
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
            getLogger().info("Installing database for " + getName() + " due to first time usage");
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
}
