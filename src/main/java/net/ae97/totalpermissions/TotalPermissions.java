/*
 * Copyright (C) 2014 AE97
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.ae97.totalpermissions.commands.CommandHandler;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.DataManager;
import net.ae97.totalpermissions.data.DataType;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.importer.DataHolderImporter;
import net.ae97.totalpermissions.listener.ListenerManager;
import net.ae97.totalpermissions.mcstats.Metrics;
import net.ae97.totalpermissions.mysql.MySQLDataHolder;
import net.ae97.totalpermissions.sqlite.SQLiteDataHolder;
import net.ae97.totalpermissions.update.UpdateChecker;
import net.ae97.totalpermissions.yaml.SingleYamlDataHolder;
import net.ae97.totalpermissions.yaml.SplitYamlDataHolder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Lord_Ralex
 */
public final class TotalPermissions extends JavaPlugin {

    private DataHolder dataHolder;
    private ListenerManager listenerManager;
    private Metrics metrics;
    private CommandHandler commands;
    private DataManager dataManager;
    private Thread updateWaiter;

    @Override
    public void onLoad() {
        updateWaiter = new Waiter(this);
    }

    @Override
    public void onEnable() {
        getLogger().info("Beginning initial preperations");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        if (!(new File(getDataFolder(), "config.yml").exists())) {
            getLogger().finest("Saving default config");
            saveResource("config.yml", true);
        }
        if (!(new File(getDataFolder(), "permissions.yml").exists())) {
            getLogger().finest("Saving default permissions.yml");
            saveResource("permissions.yml", true);
        }

        DataType type = DataType.getType(getConfig().getString("storage", "yaml_shared").toUpperCase());
        if (type == null) {
            getLogger().log(Level.SEVERE, "{0} is not a known storage system, defaulting to YAML_SHARED", getConfig().getString("storage", "yaml_shared"));
            type = DataType.YAML_SHARED;
        }
        getLogger().finest("Creating data holder");
        getLogger().log(Level.FINEST, "Storage type to load: {0}", type);
        switch (type) {
            default:
            case YAML_SHARED: {
                dataHolder = new SingleYamlDataHolder(new File(getDataFolder(), "permissions.yml"));
            }
            break;
            case YAML_SPLIT: {
                dataHolder = new SplitYamlDataHolder(getDataFolder());
            }
            break;
            case SQLITE: {
                dataHolder = new SQLiteDataHolder(new File(getDataFolder(), "permissions.db"));
            }
            break;
            case MYSQL: {
                Map<String, String> dbInfo = new HashMap<String, String>();
                dbInfo.put("host", getConfig().getString("mysql.host", "localhost"));
                dbInfo.put("port", getConfig().getString("mysql.port", "3306"));
                dbInfo.put("db", getConfig().getString("mysql.database", "totalpermissions"));
                dbInfo.put("user", getConfig().getString("mysql.username", "root"));
                dbInfo.put("pass", getConfig().getString("mysql.password", ""));
                dataHolder = new MySQLDataHolder(dbInfo);
            }
            break;
        }

        getLogger().finest("Loading permissions setup");
        dataManager = new DataManager(this, dataHolder);
        try {
            dataManager.load();
        } catch (DataLoadFailedException ex) {
            getLogger().log(Level.SEVERE, "Error on loading permissions", ex);
            Bukkit.getPluginManager().disablePlugin(this);
        }

        DataHolderImporter importer = null;
        if (getConfig().getBoolean("import.import", false)) {
            DataType importType = DataType.valueOf(getConfig().getString("storage", "yaml_shared").toUpperCase());
            if (importType == null) {
                getLogger().log(Level.SEVERE, "{0} is not a known storage system, defaulting to YAML_SHARED", getConfig().getString("storage", "yaml_shared"));
                importType = DataType.YAML_SHARED;
            }
            getLogger().log(Level.FINEST, "Storage type to import from: {0}", importType);
            try {
                DataHolder importHolder;
                switch (importType) {
                    default:
                    case YAML_SHARED: {
                        importHolder = new SingleYamlDataHolder(new File(getDataFolder(), "import.yml"));
                    }
                    break;
                    case YAML_SPLIT: {
                        importHolder = new SplitYamlDataHolder(new File(getDataFolder(), "import"));
                    }
                    break;
                    case SQLITE: {
                        importHolder = new SQLiteDataHolder(new File(getDataFolder(), "import.db"));
                    }
                    break;
                    case MYSQL: {
                        Map<String, String> dbInfo = new HashMap<String, String>();
                        dbInfo.put("host", getConfig().getString("import.mysql.host", "localhost"));
                        dbInfo.put("port", getConfig().getString("import.mysql.port", "3306"));
                        dbInfo.put("db", getConfig().getString("import.mysql.database", "totalpermissions"));
                        dbInfo.put("user", getConfig().getString("import.mysql.username", "root"));
                        dbInfo.put("pass", getConfig().getString("import.mysql.password", ""));
                        importHolder = new MySQLDataHolder(dbInfo);
                    }
                    break;
                }
                importHolder.load();
                importer = new DataHolderImporter(importHolder, dataHolder);
                importer.start();
            } catch (DataLoadFailedException ex) {
                getLogger().log(Level.SEVERE, "Error occured on setting up importer", ex);
            }
        }

        getLogger().finest("Creating listener");
        listenerManager = new ListenerManager(this);
        getLogger().finest("Registering listeners");
        listenerManager.load();

        getLogger().finest("Creating command handler");
        commands = new CommandHandler(this);
        getLogger().finest("Registering command handler");
        getCommand("totalpermissions").setExecutor(commands);

        getLogger().finest("Loading up metrics");
        metrics = new Metrics(this);
        if (metrics.isOptOut()) {
            getLogger().info("Metrics stat collecting is not enabled");
            metrics = null;
        } else {
            getLogger().info("Metrics stat collecting enabled");
            metrics.start();
        }

        if (importer != null) {
            synchronized (importer) {
                try {
                    getLogger().log(Level.INFO, "Waiting for import process to complete before continuing");
                    importer.join();
                    if (importer.hasCrashed()) {
                        getLogger().log(Level.SEVERE, "Import process failed", importer.getException());
                    } else {
                        getLogger().log(Level.INFO, "Import complete");
                        getConfig().set("import.import", false);
                        saveConfig();
                    }
                } catch (InterruptedException ex) {
                    getLogger().log(Level.SEVERE, "Error while waiting for importer to complete", ex);
                }
                importer = null;
            }
        }
    }

    @Override
    public void onDisable() {
        if (updateWaiter != null) {
            synchronized (updateWaiter) {
                try {
                    getLogger().info("Waiting for updater to complete");
                    updateWaiter.join();
                } catch (InterruptedException ex) {
                    getLogger().log(Level.SEVERE, "Error while waiting for update check thread", ex);
                }
                updateWaiter = null;
            }
        }
        if (metrics != null) {
            synchronized (metrics) {
                try {
                    getLogger().info("Waiting for metrics to shut down");
                    metrics.shutdown();
                } catch (InterruptedException ex) {
                    getLogger().log(Level.SEVERE, "Error while waiting for Metrics to shutdown", ex);
                }
                metrics = null;
            }
        }
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public CommandHandler getCommandHandler() {
        return commands;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public boolean isDebugMode() {
        return getConfig().getBoolean("angry-debug", false);
    }

    @Override
    public File getFile() {
        return super.getFile();
    }

    private class Waiter extends Thread {

        private final Thread updateChecker;

        public Waiter(TotalPermissions p) {
            updateChecker = new Thread(new UpdateChecker(p));
        }

        @Override
        public void run() {
            updateChecker.start();
            try {
                updateChecker.join();
            } catch (InterruptedException ex) {
                getLogger().log(Level.SEVERE, "Error while waiting for update check thread", ex);
            }
        }
    }
}
