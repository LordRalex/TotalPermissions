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
package net.ae97.totalpermissions.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @version 0.2
 * @author 1Rogue
 * @since 0.2
 */
public class Cipher {

    private FileConfiguration langFile;
    private final String langFileLoc = "https://raw.github.com/AE97/TotalPermissions/master/lang/";

    public Cipher(String language) {
        language += ".yml";
        Plugin plugin = TotalPermissions.getPlugin();
        //load file from github in preps for future use
        FileConfiguration github;
        try {
            github = getFromGithub(language);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Fatal error occured while retrieving lang files", e);
            github = null;
        }
        try {
            //first see if there is a lang file
            FileConfiguration file = this.getFromFolder(plugin, language);
            if (file != null) {
                int version = file.getInt("version", 0);
                int gitVersion = version;
                if (github != null) {
                    gitVersion = github.getInt("version", version);
                }
                if (gitVersion > version) {
                    plugin.getLogger().warning("Your language file is outdated, getting new file");
                    file = github;
                }
            } else {
                file = this.getFromJar(plugin, language);
                if (file == null) {
                    file = github;
                    if (file == null) {
                        throw new InvalidConfigurationException("The langauage " + language + " is unsupported");
                    }
                }
            }
            setLangFile(file);
        } catch (Exception e) {
            //and if we just completely crash and burn, then use en_US
            plugin.getLogger().log(Level.SEVERE, "Fatal error occured while loading lang files", e);
            plugin.getLogger().log(Level.SEVERE, "Defaulting to english (en_US)");
            setLangFile(YamlConfiguration.loadConfiguration(plugin.getResource("en_US.yml")));
        }
        try {
            langFile.save(new File(new File(plugin.getDataFolder(), "lang"), language));
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Fatal error occured while saving lang files", ex);
        }
    }

    private void setLangFile(FileConfiguration file) {
        langFile = file;
    }

    public FileConfiguration getLangFile() {
        return langFile;
    }

    public String getString(String path, Object... vars) {
        String string = langFile.getString(path);
        for (int i = 0; i < vars.length; i++) {
            string = string.replace("{" + i + "}", vars[i].toString());
        }
        return string;
    }

    private FileConfiguration getFromFolder(Plugin pl, String lang) {
        File langFolder = new File(pl.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }
        if (new File(langFolder, lang).exists()) {
            return YamlConfiguration.loadConfiguration(new File(langFolder, lang));
        } else {
            return null;
        }
    }

    private FileConfiguration getFromJar(Plugin plugin, String lang) {
        InputStream jarStream = plugin.getResource(lang);
        if (jarStream != null) {
            return YamlConfiguration.loadConfiguration(jarStream);
        } else {
            return null;
        }
    }

    private FileConfiguration getFromGithub(String lang) throws MalformedURLException, IOException {
        URL upstr = new URL(langFileLoc + lang);
        InputStream langs = upstr.openStream();
        if (langs != null) {
            return YamlConfiguration.loadConfiguration(langs);
        } else {
            return null;
        }
    }
}
