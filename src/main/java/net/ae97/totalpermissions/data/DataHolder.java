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

import java.io.IOException;
import java.util.Set;
import net.ae97.totalpermissions.permission.PermissionType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * @version 1.1
 * @author Lord_Ralex
 */
public interface DataHolder {

    void setup() throws InvalidConfigurationException;

    void load(PermissionType type, String name) throws IOException, InvalidConfigurationException;

    ConfigurationSection getConfigurationSection(PermissionType type, String name);

    Set<String> getKeys(PermissionType type);

    void update(PermissionType type, String name, ConfigurationSection obj);

    ConfigurationSection create(PermissionType type, String name);

    void save(PermissionType type, String name) throws IOException;

    boolean contains(PermissionType type, String name);
}
