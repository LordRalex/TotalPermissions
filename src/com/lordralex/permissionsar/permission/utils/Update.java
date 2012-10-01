/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.permissionsar.permission.utils;

import com.lordralex.permissionsar.PermissionsAR;
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
 *
 * @author Joshua
 */
public class Update {

    boolean backupFiles = false;

    public void Update() {
        backupFiles = true;
    }

    public void backup() {
        backup(backupFiles);
    }

    public void backup(boolean bu) {
        if (bu == false) {
            return;
        }
        File dataFolder = PermissionsAR.getPlugin().getDataFolder();
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
            PermissionsAR.getConfigFile().save(new File(backupFolder, "config.yml"));
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PermissionsAR.getPermFile().save(new File(backupFolder, "permissions.yml"));
        } catch (IOException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
        backupFiles = false;
    }

    public void runUpdate() {
        backup();
        FileConfiguration updateFile;
        FileConfiguration perms = PermissionsAR.getPermFile();
        File datafolder = PermissionsAR.getPlugin().getDataFolder();
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
        File dataFolder = PermissionsAR.getPlugin().getDataFolder();
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
            if (!(new File(PermissionsAR.getPlugin().getDataFolder(), name).exists())) {
                continue;
            }
            try {
                YamlConfiguration.loadConfiguration(new File(PermissionsAR.getPlugin().getDataFolder(), name)).save(new File(backupFolder, name));
            } catch (IOException ex) {
                Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
            }
            new File(PermissionsAR.getPlugin().getDataFolder(), name).delete();
        }
    }
}
