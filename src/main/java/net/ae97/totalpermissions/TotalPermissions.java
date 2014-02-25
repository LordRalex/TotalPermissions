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
import java.util.logging.Level;
import net.ae97.totalpermissions.commands.CommandHandler;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.DataManager;
import net.ae97.totalpermissions.data.DataType;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.listener.ListenerManager;
import net.ae97.totalpermissions.mcstats.Metrics;
import net.ae97.totalpermissions.updater.Updater;
import net.ae97.totalpermissions.updater.UpdateType;
import net.ae97.totalpermissions.yaml.SingleYamlDataHolder;
import net.ae97.totalpermissions.yaml.SplitYamlDataHolder;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Lord_Ralex
 */
public final class TotalPermissions extends JavaPlugin {

    protected DataHolder dataHolder;
    protected ListenerManager listenerManager;
    protected Metrics metrics;
    protected CommandHandler commands;
    protected DataManager dataManager;

    @Override
    public void onLoad() {
        if (getConfig().getBoolean("update.check", true)) {
            UpdateType updatetype = UpdateType.NO_DOWNLOAD;
            if (getConfig().getBoolean("update.download", true)) {
                updatetype = UpdateType.DEFAULT;
            }

            Updater updater = new Updater(this, "totalpermissions", this.getFile(), updatetype, true);
            updater.checkForUpdate();
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
                getLogger().severe(storageType + " is not a known storage system, defaulting to YAML_SHARED");
                type = DataType.YAML_SHARED;
            }
            getLogger().finest("Creating data holder");
            getLogger().finest("Storage type to load: " + storageType);
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
                }
                break;
                case MYSQL: {
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
}
