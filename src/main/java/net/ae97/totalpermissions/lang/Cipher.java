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
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ae97.totalpermissions.TotalPermissions;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * @version 0.2
 * @author 1Rogue
 * @since 0.2
 */
public class Cipher {

    private YamlConfiguration langFile;
    private final String langFileLoc = "https://raw.github.com/AE97/TotalPermissions/master/lang/";

    public Cipher(String language) {
        language += ".yml";
        Plugin plugin = TotalPermissions.getPlugin();
        try {
            //first see if there is a lang file
            if (new File(new File(plugin.getDataFolder(), "lang"), language).exists()) {
                setLangFile(YamlConfiguration.loadConfiguration(new File(new File(plugin.getDataFolder(), "lang"), language)));
            } else {
                //if not, then load file jar
                InputStream jarStream = plugin.getResource(language);
                if (jarStream != null) {
                    setLangFile(YamlConfiguration.loadConfiguration(jarStream));
                } else {
                    //if that does not work, then load from github
                    URL upstr = new URL(langFileLoc + language);
                    InputStream langs = upstr.openStream();
                    setLangFile(YamlConfiguration.loadConfiguration(langs));
                }
            }
        } catch (Exception e) {
            //and if we just completely crash and burn, then use en_US
            TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Fatal error occured while loading lang files", e);
            TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Defaulting to english (en_US)");
            this.setLangFile(YamlConfiguration.loadConfiguration(TotalPermissions.getPlugin().getResource("en_US.yml")));
        }
        try {
            langFile.save(new File(new File(plugin.getDataFolder(), "lang"), language));
        } catch (IOException ex) {
            TotalPermissions.getPlugin().getLogger().log(Level.SEVERE, "Fatal error occured while saving lang files", ex);
        }
    }

    private void setLangFile(YamlConfiguration file) {
        langFile = file;
    }

    public FileConfiguration getLangFile() {
        return langFile;
    }

    public String getString(String path, String... vars) {
        String string = langFile.getString(path);
        for (int i = 0; i < vars.length; i++) {
            string = string.replace("{" + i + "}", vars[i]);
        }
        return string;
    }
}
