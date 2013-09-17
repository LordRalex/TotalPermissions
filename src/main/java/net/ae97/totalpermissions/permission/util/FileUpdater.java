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
package net.ae97.totalpermissions.permission.util;

import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.YamlDataHolder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Lord_Ralex
 * @version 0.1
 * @since 0.2
 * @deprecated Updater is not made to handle effectively, needs replacement
 */
public class FileUpdater {

    private boolean backupFiles = false;
    private final TotalPermissions plugin;

    /**
     * @deprecated Updater is not made to handle effectively, needs replacement
     */
    public FileUpdater(TotalPermissions p) {
        plugin = p;
    }

    /**
     * @deprecated Updater is not made to handle effectively, needs replacement
     */
    public void update() {
        backupFiles = true;
    }

    /**
     * Backs up the perms and config files if they have not been backed up
     * already
     *
     * @since 0.2
     * @deprecated Updater is not made to handle effectively, needs replacement
     */
    public void backup() {
        backup(backupFiles);
    }

    /**
     * Runs a backup of the permission and config files. If true, it will
     * backup, false will not.
     *
     * @param bu Whether to back up or not.
     *
     * @since 0.2
     * @deprecated Updater is not made to handle effectively, needs replacement
     */
    public void backup(boolean bu) {
        if (!(plugin.getDataHolder() instanceof YamlDataHolder)) {
            return;
        }
        if (bu == false) {
            return;
        }
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File backup = new File(dataFolder, "backup");
        if (backup.exists()) {
            backup.mkdirs();
        }
        int highest = 0;
        if (backup.listFiles() == null) {
            highest = 1;
        } else {
            for (File file : backup.listFiles()) {
                if (file.isDirectory()) {
                    try {
                        int num = Integer.parseInt(file.getName());
                        if (highest < num) {
                            highest = num;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        highest++;
        File backupFolder = new File(backup, Integer.toString(highest));
        backupFolder.mkdirs();
        try {
            plugin.getConfig().save(new File(backupFolder, "config.yml"));
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }
        try {
            if (plugin.getDataHolder() instanceof YamlDataHolder) {
                YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "permissions.yml")).save(new File(backupFolder, "permissions.yml"));
            }
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }
        backupFiles = false;
    }

    /**
     * Merges the groups, users, and update files with the permissions file.
     * This will first check to see if a backup is needed to be ran first. Once
     * complete, the update files will be placed into a backup folder.
     *
     * @since 0.2
     * @deprecated Updater is not made to handle effectively, needs replacement
     */
    public void runUpdate() {
        if (plugin.getDataHolder() instanceof YamlDataHolder) {
            backup();
            FileConfiguration updateFile;
            DataHolder perms = plugin.getDataHolder();
            File datafolder = plugin.getDataFolder();
            String[] updateFileNames = new String[]{
                "groups.yml",
                "users.yml",
                "update.yml"
            };
            for (String name : updateFileNames) {
                updateFile = YamlConfiguration.loadConfiguration(new File(datafolder, name));
                if (!(new File(datafolder, name).exists())) {
                    continue;
                }
                for (String key : updateFile.getKeys(false)) {
                    if (updateFile.isConfigurationSection(key)) {
                        //updateSection(updateFile.getConfigurationSection(key), (perms.getConfigurationSection(key) == null) ? perms.createSection(key) : perms.getConfigurationSection(key));
                    }
                }
            }
            try {
                //perms.save(new File(datafolder, "permissions.yml"));
                throw new IOException("FileUpdater is currently broken");
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Error occured on updating", ex);
            }
            moveFiles();
        }
    }

    private void updateSection(ConfigurationSection sec, ConfigurationSection destination) {
        for (String key : sec.getKeys(false)) {
            if (sec.isConfigurationSection(key)) {
                if (destination.isConfigurationSection(key)) {
                    updateSection(sec.getConfigurationSection(key), (destination.getConfigurationSection(key) == null) ? destination.createSection(key) : destination.getConfigurationSection(key));
                } else {
                    updateSection(sec.getConfigurationSection(key), destination.createSection(key));
                }
            } else {
                Object obj = sec.get(key);
                Object old = null;
                if (obj instanceof List) {
                    List<String> list = (List<String>) obj;
                    if (destination != null) {
                        old = destination.getStringList(key);
                    }
                    if (old == null) {
                        old = new ArrayList<String>();
                    }
                    ((List<String>) old).addAll(list);
                } else if (obj instanceof Integer) {
                    old = (Integer) obj;
                } else if (obj instanceof Double) {
                    old = (Double) obj;
                } else if (obj instanceof Boolean) {
                    old = (Boolean) obj;
                } else if (obj instanceof String) {
                    old = (String) obj;
                } else {
                    old = obj;
                }
                if (old != null) {
                    destination.set(key, old);
                }
            }
        }
    }

    private void moveFiles() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File backup = new File(dataFolder, "backup-updates");
        if (!backup.exists()) {
            backup.mkdirs();
        }
        int highest = 0;
        if (backup.listFiles() == null) {
            highest = 1;
        } else {
            for (File file : backup.listFiles()) {
                if (file.isDirectory()) {
                    try {
                        int num = Integer.parseInt(file.getName());
                        if (highest < num) {
                            highest = num;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        highest++;
        File backupFolder = new File(backup, Integer.toString(highest));
        backupFolder.mkdirs();
        String[] updateFileNames = new String[]{
            "groups.yml",
            "users.yml",
            "update.yml"
        };
        for (String name : updateFileNames) {
            if (!(new File(plugin.getDataFolder(), name).exists())) {
                continue;
            }
            try {
                YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), name)).save(new File(backupFolder, name));
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Error occured on loading", ex);
            }
            new File(plugin.getDataFolder(), name).delete();
        }
    }
}
