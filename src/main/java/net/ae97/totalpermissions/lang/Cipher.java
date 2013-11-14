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

import java.io.IOException;
import org.bukkit.ChatColor;
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

    private final FileConfiguration langFile;
    private final String language;

    public Cipher(Plugin plugin, String lang) throws InvalidConfigurationException, IOException {
        language = lang;
        langFile = new YamlConfiguration();
        langFile.load("lang/" + plugin.getResource(lang + ".yml"));
    }

    /**
     * Gets the message for this key in the used language. If the key does not
     * exist, this will default to use the en_US in the jarfile.
     *
     * @param path The path to the string
     * @param vars Any variables to add
     * @return The resulting String
     *
     * @since 0.2
     *
     * @deprecated Fully deprecated, replacing with new system
     */
    public String getString(String path, Object... vars) {
        String string = langFile.getString(path);
        if (string == null) {
            throw new NullPointerException("The language files are missing the path. Language: " + language + " Path: " + path);
        }
        for (int i = 0; i < vars.length; i++) {
            string = string.replace("{" + i + "}", vars[i].toString());
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
