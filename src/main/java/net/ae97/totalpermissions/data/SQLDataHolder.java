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
import net.ae97.totalpermissions.sql.SQLConnection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class SQLDataHolder implements DataHolder {

    private SQLConnection connection;

    @Override
    public void load(InputStream in) throws InvalidConfigurationException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void load(File file) throws InvalidConfigurationException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void load(String string) throws InvalidConfigurationException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getString(String key) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public List<String> getStringList(String key) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public ConfigurationSection getConfigurationSection(String key) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Set<String> getKeys() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void set(String key, Object obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public ConfigurationSection createSection(String key) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean isConfigurationSection(String key) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void save(File file) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void save(String string) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean contains(String key) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
