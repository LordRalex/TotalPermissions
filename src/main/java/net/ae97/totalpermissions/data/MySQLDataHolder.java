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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.ae97.totalpermissions.permission.PermissionType;
import net.ae97.totalpermissions.sql.MySQLConnection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class MySQLDataHolder implements DataHolder {

    private MySQLConnection connection;
    private final Map<String, Map<String, ConfigurationSection>> memory = new ConcurrentHashMap<String, Map<String, ConfigurationSection>>();

    @Override
    public void load(InputStream in) throws InvalidConfigurationException {
        throw new UnsupportedOperationException("This implentation does not support loading from InputStreams");
    }

    @Override
    public void load(File file) throws InvalidConfigurationException {
        throw new UnsupportedOperationException("This implentation does not support loading from Files");
    }

    @Override
    public void load(String string) throws InvalidConfigurationException {
        Map<String, String> options = new HashMap<String, String>();
        String[] parts = string.split(" ");
        for (String part : parts) {
            String[] vars = part.split("=", 2);
            if (vars.length == 0) {
                continue;
            } else {
                options.put(vars[0], vars.length == 2 ? vars[1] : null);
            }
        }
        load(options);
    }

    public void load(Map<String, String> options) throws InvalidConfigurationException {
        try {
            connection = new MySQLConnection(options);
        } catch (SQLException ex) {
            throw new InvalidConfigurationException(ex);
        }
    }

    @Override
    public String getString(String key) {
        String[] split = key.split(".");
        if (split.length < 3) {
            throw new UnsupportedOperationException("This implentation cannot get a string for the key " + key);
        }
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

    /**
     * Only call this when you want to execute a MySQL database call!
     */
    public void load(PermissionType type, String name) throws IOException, SQLException {
        if (type == null || name == null) {
            throw new IOException("Cannot retrieve a null object");
        }
        ResultSet set = connection.executeQuery(MYSQLSTATEMENT.LOAD_ITEM.getStatement(), connection.getTable(), type.toString(), name);
        if (set == null) {
            return;
        }
        ConfigurationSection section = new MemoryConfiguration();
        //move data from result set into section
        Map<String, ConfigurationSection> sec = memory.get(type.toString());
        if(sec == null) {
            sec = new ConcurrentHashMap<String, ConfigurationSection>();
        }
        sec.put(name, section);
        memory.put(type.toString(), sec);
    }

    private enum MYSQLSTATEMENT {

        LOAD_ITEM("SELECT * FROM ? WHERE ?=?"),
        SAVE_ITEM("");
        private final String statement;

        private MYSQLSTATEMENT(String s) {
            statement = s;
        }

        public String getStatement() {
            return statement;
        }
    }
}
