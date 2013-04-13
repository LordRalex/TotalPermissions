/*
 * Copyright (C) 2013 Lord_Ralex
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 */
public class FileConverter {

    protected final FileConfiguration config;
    protected File path;

    public FileConverter(FileConfiguration file, File f) {
        config = file;
        path = f;
    }

    public FileConfiguration convert() throws InvalidConfigurationException, IOException {
        FileConfiguration newVersion = new YamlConfiguration();

        if (!config.isConfigurationSection("groups")) {
            config.createSection("groups");
        }
        ConfigurationSection groupSection = config.getConfigurationSection("groups");
        Set<String> groupNames = groupSection.getKeys(false);

        ConfigurationSection newGroupSection = newVersion.createSection("groups");

        for (String group : groupNames) {
            ConfigurationSection oldGroup = groupSection.getConfigurationSection(group);

            ConfigurationSection newGroup = newGroupSection.createSection(group);
            ConfigurationSection newOptions = newGroup.createSection("options");
            ConfigurationSection newWorlds = newGroup.createSection("worlds");

            Set<String> parts = oldGroup.getKeys(false);
            for (String section : parts) {
                if (section.equalsIgnoreCase("permissions")) {
                    List<String> newOld = newGroup.getStringList("permissions");
                    if (newOld == null) {
                        newOld = new ArrayList<String>();
                    }
                    List<String> oldPerms = oldGroup.getStringList("permissions");
                    if (oldPerms == null) {
                        oldPerms = new ArrayList<String>();
                    }
                    Set<String> combine = new HashSet<String>();
                    combine.addAll(newOld);
                    combine.addAll(oldPerms);
                    boolean addOne = false;
                    boolean addTwo = false;
                    if (combine.contains("*")) {
                        addOne = true;
                        combine.remove("*");
                    }
                    if (combine.contains("**")) {
                        addTwo = true;
                        combine.remove("**");
                    }
                    ArrayList<String> notSet = new ArrayList<String>(combine);
                    Collections.sort(notSet);
                    if (addOne) {
                        notSet.add("*");
                    }
                    if (addTwo) {
                        notSet.add("**");
                    }

                    newGroup.set("permissions", notSet);
                } else if (section.equalsIgnoreCase("groups") || section.equalsIgnoreCase("group")) {
                    List<String> newOld = newGroup.getStringList("groups");
                    if (newOld == null) {
                        newOld = new ArrayList<String>();
                    }
                    List<String> oldGroups = oldGroup.getStringList("groups");
                    if (oldGroups == null) {
                        oldGroups = new ArrayList<String>();
                    }
                    List<String> oldGroups2 = oldGroup.getStringList("group");
                    if (oldGroups2 == null) {
                        oldGroups2 = new ArrayList<String>();
                    }
                    Set<String> combine = new HashSet<String>();
                    combine.addAll(newOld);
                    combine.addAll(oldGroups);
                    combine.addAll(oldGroups2);
                    ArrayList<String> notSet = new ArrayList<String>(combine);

                    newGroup.set("groups", notSet);
                } else if (section.equalsIgnoreCase("worlds")) {
                    newGroup.set("worlds", oldGroup.getConfigurationSection("worlds"));
                    //this will be added on once multiworld is more supported
                } else if (section.equalsIgnoreCase("inheritance")) {
                    List<String> newOld = newGroup.getStringList("inheritance");
                    if (newOld == null) {
                        newOld = new ArrayList<String>();
                    }
                    List<String> oldInherit = oldGroup.getStringList("inheritance");
                    if (oldInherit == null) {
                        oldInherit = new ArrayList<String>();
                    }
                    Set<String> combine = new HashSet<String>();
                    combine.addAll(newOld);
                    combine.addAll(oldInherit);
                    ArrayList<String> notSet = new ArrayList<String>(combine);

                    newGroup.set("inheritance", notSet);
                } else if (section.equalsIgnoreCase("options")) {
                    ConfigurationSection cfg = oldGroup.getConfigurationSection("options");
                    for (String key : cfg.getKeys(true)) {
                        newOptions.set(key, cfg.get(key));
                    }
                } else {
                    Object old = oldGroup.get(section);
                    newGroup.set("options." + section, old);
                }
            }
        }

        String string = newVersion.saveToString();
        config.loadFromString(string);
        save();
        return config;
    }

    public void save() throws IOException {
        config.save(path);
    }
}
