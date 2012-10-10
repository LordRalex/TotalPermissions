package com.lordralex.totalpermissions.permission.utils;

import com.lordralex.totalpermissions.TotalPermissions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class Update {

    private boolean backupFiles = false;

    public void Update() {
        backupFiles = true;
    }

    /**
     * Backs up the perms and config files if they have not been backed up
     * already
     *
     * @since 1.0
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
     * @since 1.0
     */
    public void backup(boolean bu) {
        if (bu == false) {
            return;
        }
        File dataFolder = TotalPermissions.getPlugin().getDataFolder();
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
            TotalPermissions.getConfigFile().save(new File(backupFolder, "config.yml"));
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            TotalPermissions.getPermFile().save(new File(backupFolder, "permissions.yml"));
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        backupFiles = false;
    }

    /**
     * Merges the groups, users, and update files with the permissions file.
     * This will first check to see if a backup is needed to be ran first. Once
     * complete, the update files will be placed into a backup folder.
     *
     * @since 1.0
     */
    public void runUpdate() {
        backup();
        FileConfiguration updateFile;
        FileConfiguration perms = TotalPermissions.getPermFile();
        File datafolder = TotalPermissions.getPlugin().getDataFolder();
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
                    updateSection(updateFile.getConfigurationSection(key), perms.getConfigurationSection(key));
                }
            }
        }
        try {
            perms.save(new File(datafolder, "permissions.yml"));
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        moveFiles();
    }

    private void updateSection(ConfigurationSection sec, ConfigurationSection destination) {
        for (String key : sec.getKeys(false)) {
            if (sec.isConfigurationSection(key)) {
                if (destination.isConfigurationSection(key)) {
                    updateSection(sec.getConfigurationSection(key), destination.getConfigurationSection(key));
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
        File dataFolder = TotalPermissions.getPlugin().getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File backup = new File(dataFolder, "backup-updates");
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
        String[] updateFileNames = new String[]{
            "groups.yml",
            "users.yml",
            "update.yml"
        };
        for (String name : updateFileNames) {
            if (!(new File(TotalPermissions.getPlugin().getDataFolder(), name).exists())) {
                continue;
            }
            try {
                YamlConfiguration.loadConfiguration(new File(TotalPermissions.getPlugin().getDataFolder(), name)).save(new File(backupFolder, name));
            } catch (IOException ex) {
                Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            }
            new File(TotalPermissions.getPlugin().getDataFolder(), name).delete();
        }
    }
}
