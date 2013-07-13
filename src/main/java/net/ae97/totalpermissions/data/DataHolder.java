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
package net.ae97.totalpermissions.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public interface DataHolder {

    void load(InputStream in) throws InvalidConfigurationException, IOException;

    void load(File file) throws InvalidConfigurationException, IOException;

    void load(String string) throws InvalidConfigurationException, IOException;

    String getString(String key);

    List<String> getStringList(String key);

    ConfigurationSection getConfigurationSection(String key);

    Set<String> getKeys();

    void set(String key, Object obj);

    ConfigurationSection createSection(String key);

    boolean isConfigurationSection(String key);

    void save(File file) throws IOException;

    void save(String string) throws IOException;

    boolean contains(String key);
}
