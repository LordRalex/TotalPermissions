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
import net.ae97.totalpermissions.listener.ListenerManager;
import net.ae97.totalpermissions.mcstats.Metrics;
import net.ae97.totalpermissions.mysql.MySQLDataHolder;
import net.ae97.totalpermissions.sqlite.SQLiteDataHolder;
import net.ae97.totalpermissions.update.UpdateChecker;
import net.ae97.totalpermissions.yaml.SingleYamlDataHolder;
import net.ae97.totalpermissions.yaml.SplitYamlDataHolder;
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
    private Thread updateChecker;

    @Override
    public void onLoad() {
        if (getConfig().getBoolean("update.check", true)) {
            updateChecker = new Thread(new UpdateChecker(this, getConfig().getBoolean("update.download", true)));
            updateChecker.start();
        }
    }

    @Override
    public void onEnable() {
        try {
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

            String storageType = getConfig().getString("storage", "yaml_shared");
            DataType type = DataType.valueOf(storageType.toUpperCase());
            if (type == null) {
                getLogger().log(Level.SEVERE, "{0} is not a known storage system, defaulting to YAML_SHARED", storageType);
                type = DataType.YAML_SHARED;
            }
            getLogger().finest("Creating data holder");
            getLogger().log(Level.FINEST, "Storage type to load: {0}", storageType);
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
                    dataHolder = new SQLiteDataHolder();
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
            dataManager.load();

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
            if (metrics.start()) {
                getLogger().info("Metrics stat collecting enabled");
            } else {
                getLogger().info("Metrics stat collecting is not enabled");
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "An error occured on enabled, please check your configs and other files", e);
        } catch (DataLoadFailedException e) {
            getLogger().log(Level.SEVERE, "An error occured on loading some data, please check the error and fix the issue:", e);
        }
    }

    @Override
    public void onDisable() {
        synchronized (updateChecker) {
            try {
                updateChecker.join();
            } catch (InterruptedException ex) {
                getLogger().log(Level.SEVERE, "Error while waiting for update check thread", ex);
            }
        }
        metrics.shutdown();
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
}
