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

import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.YamlDataHolder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 * @deprecated Currently completely screwed, new ones will be made
 */
public class FileConverter {

    protected DataHolder config;
    protected File path;

    /**
     * @deprecated Currently completely screwed, new ones will be made
     */
    public FileConverter(DataHolder file, File f) {
        config = file;
        path = f;
    }

    /**
     * @deprecated Currently completely screwed, new ones will be made
     */
    public DataHolder convert() throws InvalidConfigurationException, IOException {
        return config;
    }

    /**
     * @deprecated Currently completely screwed, new ones will be made
     */
    public void save() throws IOException {
    }
}
