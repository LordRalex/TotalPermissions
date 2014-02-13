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
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import net.ae97.totalpermissions.commands.CommandHandler;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.DataManager;
import net.ae97.totalpermissions.data.DataType;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.lang.Lang;
import net.ae97.totalpermissions.listener.ListenerManager;
import net.ae97.totalpermissions.logger.DebugLogFormatter;
import net.ae97.totalpermissions.mcstats.Metrics;
import net.ae97.totalpermissions.updater.Updater;
import net.ae97.totalpermissions.updater.UpdateType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Lord_Ralex
 */
public final class TotalPermissions extends JavaPlugin {

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

            String storageType = getConfig().getString("storage", "yaml_shared");
            DataType type = DataType.valueOf(storageType.toUpperCase());
            if (type == null) {
                log(Level.SEVERE, Lang.MAIN_STORAGEERROR, storageType);
                type = DataType.YAML_SHARED;
            }
            debugLog("Creating data holder");
            debugLog("Storage type to load: " + storageType);
            switch (type) {
                default:
                case YAML_SHARED: {
                }
                break;
                case YAML_SPLIT: {
                }
                break;
                case SQLITE: {
                }
                break;
                case MYSQL: {
                }
                break;
            }

            debugLog("Loading permissions setup");
            dataManager.load();

            debugLog("Creating listener");
            listenerManager = new ListenerManager(this);
            debugLog("Registering listeners");
            listenerManager.load();

            debugLog("Creating command handler");
            commands = new CommandHandler(this);
            debugLog("Registering command handler");
            getCommand("totalpermissions").setExecutor(commands);

            debugLog("Loading up metrics");
            metrics = new Metrics(this);
            if (metrics.start()) {
                log(Level.INFO, Lang.MAIN_METRICS);
            } else {
                log(Level.INFO, Lang.MAIN_METRICSOFF);
            }
        } catch (IOException e) {
            log(Level.SEVERE, Lang.MAIN_ERROR, e);
        } catch (DataLoadFailedException e) {
            log(Level.SEVERE, Lang.MAIN_ERROR, e);
        }
    }

    @Override
    public void onDisable() {
        Handler[] handlers = getLogger().getHandlers();
        for (Handler handle : handlers) {
            if (handle.getFormatter() instanceof DebugLogFormatter) {
                getLogger().removeHandler(handle);
            }
        }
        File[] fileList = new File(getDataFolder(), "logs").listFiles();
        for (File file : fileList) {
            if (file.getName().endsWith(".lck")) {
                file.delete();
            }
        }
    }

    /**
     * Gets the ListenerManager in use
     *
     * @return The ListenerManager loaded
     *
     * @since 0.4.0
     */
    public ListenerManager getListenerManager() {
        return listenerManager;
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

    public DataManager getDataManager() {
        return dataManager;
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

    public void log(Level level, Lang message, Object... args) {
        getLogger().log(level, message.getMessage(args));
    }

    public void debugLog(Object... message) {
        for (Object m : message) {
            getLogger().log(Level.FINER, m.toString());
        }
    }
}
