/*
 * Copyright (C) 2014 AE97
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
package net.ae97.totalpermissions.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import net.ae97.totalpermissions.type.PermissionType;

/**
 * @author Lord_Ralex
 */
public class MySQLDataHolder implements DataHolder<MySQLPermissionBase> {

    private Connection connection;
    private final EnumMap<PermissionType, HashMap<String, MySQLPermissionBase>> cache = new EnumMap<PermissionType, HashMap<String, MySQLPermissionBase>>(PermissionType.class);
    private final Map<String, String> dbInfo = new HashMap<String, String>();

    public MySQLDataHolder(Map<String, String> info) {
        connection = null;
        dbInfo.putAll(info);
    }

    @Override
    public void load() throws DataLoadFailedException {
        cache.clear();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new DataLoadFailedException(ex);
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + dbInfo.get("host") + ":" + dbInfo.get("port") + "/" + dbInfo.get("db"), dbInfo.get("user"), dbInfo.get("pass"));
        } catch (SQLException ex) {
            throw new DataLoadFailedException(ex);
        }
    }

    @Override
    public void load(PermissionType type, String name) throws DataLoadFailedException {
        switch (type) {
            case USER:
                loadUser(name);
                break;
            case GROUP:
                loadGroup(name);
                break;
            case WORLD:
                loadWorld(name);
                break;
            case ENTITY:
                loadEntity(name);
                break;
            case OP:
                loadOp();
                break;
            case RCON:
                loadRcon();
                break;
            case CONSOLE:
                loadConsole();
                break;
        }
    }

    @Override
    public void loadUser(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("users", "name", name);
        MySQLPermissionBase base = new MySQLPermissionUser(name);
        load(PermissionType.USER, base, data);
    }

    @Override
    public void loadGroup(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("groups", "name", name);
        MySQLPermissionBase base = new MySQLPermissionGroup(name);
        load(PermissionType.GROUP, base, data);
    }

    @Override
    public void loadWorld(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("worlds", "name", name);
        MySQLPermissionBase base = new MySQLPermissionWorld(name);
        load(PermissionType.WORLD, base, data);
    }

    @Override
    public void loadEntity(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("entities", "name", name);
        MySQLPermissionBase base = new MySQLPermissionEntity(name);
        load(PermissionType.ENTITY, base, data);
    }

    @Override
    public void loadConsole() throws DataLoadFailedException {
        Map<String, Object> data = getData("server", "name", "console");
        MySQLPermissionBase base = new MySQLPermissionConsole();
        load(PermissionType.CONSOLE, base, data);
    }

    @Override
    public void loadOp() throws DataLoadFailedException {
        Map<String, Object> data = getData("server", "name", "op");
        MySQLPermissionBase base = new MySQLPermissionOp();
        load(PermissionType.OP, base, data);
    }

    @Override
    public void loadRcon() throws DataLoadFailedException {
        Map<String, Object> data = getData("server", "name", "rcon");
        MySQLPermissionBase base = new MySQLPermissionRcon();
        load(PermissionType.RCON, base, data);
    }

    @Override
    public MySQLPermissionBase get(PermissionType type, String name) throws DataLoadFailedException {
        switch (type) {
            case USER:
                return getUser(name);
            case GROUP:
                return getGroup(name);
            case WORLD:
                return getWorld(name);
            case ENTITY:
                return getEntity(name);
            case OP:
                return getOP();
            case CONSOLE:
                return getConsole();
            case RCON:
                return getRcon();
            default:
                return null;
        }
    }

    @Override
    public MySQLPermissionUser getUser(String name) throws DataLoadFailedException {
        checkCache(PermissionType.USER, name);
        return (MySQLPermissionUser) cache.get(PermissionType.USER).get(name);
    }

    @Override
    public MySQLPermissionGroup getGroup(String name) throws DataLoadFailedException {
        checkCache(PermissionType.GROUP, name);
        return (MySQLPermissionGroup) cache.get(PermissionType.GROUP).get(name);
    }

    @Override
    public MySQLPermissionWorld getWorld(String name) throws DataLoadFailedException {
        checkCache(PermissionType.WORLD, name);
        return (MySQLPermissionWorld) cache.get(PermissionType.WORLD).get(name);
    }

    @Override
    public MySQLPermissionEntity getEntity(String name) throws DataLoadFailedException {
        checkCache(PermissionType.ENTITY, name);
        return (MySQLPermissionEntity) cache.get(PermissionType.ENTITY).get(name);
    }

    @Override
    public MySQLPermissionOp getOP() throws DataLoadFailedException {
        checkCache(PermissionType.OP, null);
        return (MySQLPermissionOp) cache.get(PermissionType.OP).get(null);
    }

    @Override
    public MySQLPermissionConsole getConsole() throws DataLoadFailedException {
        checkCache(PermissionType.CONSOLE, null);
        return (MySQLPermissionConsole) cache.get(PermissionType.CONSOLE).get(null);
    }

    @Override
    public MySQLPermissionRcon getRcon() throws DataLoadFailedException {
        checkCache(PermissionType.RCON, null);
        return (MySQLPermissionRcon) cache.get(PermissionType.RCON).get(null);
    }

    @Override
    public Set<String> getGroups() throws DataLoadFailedException {
        return getList("groups", "name");
    }

    @Override
    public Set<String> getUsers() throws DataLoadFailedException {
        return getList("users", "name");
    }

    @Override
    public Set<String> getWorlds() throws DataLoadFailedException {
        return getList("worlds", "name");
    }

    @Override
    public Set<String> getEntities() throws DataLoadFailedException {
        return getList("entities", "name");
    }

    @Override
    public void save(MySQLPermissionBase holder) throws DataSaveFailedException {
        holder.save();
        Map<String, Object> saveData = holder.getSaveData();
        PreparedStatement statement = null;
        try {
            Connection conn;
            try {
                conn = getConnection();
            } catch (DataLoadFailedException ex) {
                throw new DataSaveFailedException(ex);
            }
            String[] keys = saveData.keySet().toArray(new String[saveData.size()]);

            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO ");
            switch (holder.getType()) {
                case USER:
                    builder.append("'users'");
                    break;
                case GROUP:
                    builder.append("'groups'");
                    break;
                case WORLD:
                    builder.append("'worlds'");
                    break;
                case ENTITY:
                    builder.append("'entities'");
                    break;
                case OP:
                case CONSOLE:
                case RCON:
                    builder.append("'server'");
                    break;
            }
            builder.append(" (");
            for (int i = 0; i < keys.length; i++) {
                builder.append("?");
                if (i + 1 < keys.length) {
                    builder.append(",");
                }
            }
            builder.append(") VALUES (");
            for (int i = 0; i < keys.length; i++) {
                builder.append("?");
                if (i + 1 < keys.length) {
                    builder.append(",");
                }
            }
            builder.append(") WHERE name=?;");
            statement = conn.prepareStatement(builder.toString());
            for (int i = 0; i < keys.length; i++) {
                statement.setString(i + 1, keys[i]);
            }
            for (int i = 0; i < keys.length; i++) {
                statement.setObject(i + 1 + keys.length, saveData.get(keys[i]));
            }
            statement.setString((keys.length * 2) + 1, holder.getName());
            statement.execute();
        } catch (SQLException ex) {
            throw new DataSaveFailedException(ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    protected final Connection getConnection() throws DataLoadFailedException {
        if (connection == null) {
            throw new DataLoadFailedException("Connection not established");
        }
        return connection;
    }

    protected final Map<String, Object> getData(String table, String key, String value) throws DataLoadFailedException {
        Connection conn = getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            statement = conn.prepareStatement("SELECT * FROM ? WHERE ?=?");
            statement.setString(1, table);
            statement.setString(2, key);
            statement.setString(3, value);
            set = statement.executeQuery();
            int size = set.getMetaData().getColumnCount();
            HashMap<String, Object> mappings = new HashMap<String, Object>();
            for (int i = 1; i <= size; i++) {
                Object obj = set.getObject(i);
                String name = set.getMetaData().getColumnName(i);
                mappings.put(name, obj);
            }
            return mappings;
        } catch (SQLException ex) {
            throw new DataLoadFailedException(ex);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (SQLException ex) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    protected final Set<String> getList(String table, String column) throws DataLoadFailedException {
        Connection conn = getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        try {
            statement = conn.prepareStatement("SELECT ? FROM ?");
            statement.setString(1, column);
            statement.setString(2, table);
            set = statement.executeQuery();
            Set<String> nameSet = new HashSet<String>();
            while (set.next()) {
                nameSet.add(set.getString(1));
            }
            return nameSet;
        } catch (SQLException ex) {
            throw new DataLoadFailedException(ex);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (SQLException ex) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                }
            }
        }
    }

    protected final void checkCache(PermissionType type, String name) throws DataLoadFailedException {
        if (cache.get(type) == null || cache.get(type).get(name == null ? null : name.toLowerCase()) == null) {
            load(type, name);
        }
    }

    protected final void load(PermissionType type, MySQLPermissionBase base, Map<String, Object> data) throws DataLoadFailedException {
        HashMap<String, MySQLPermissionBase> baseMap = cache.get(type);
        if (baseMap == null) {
            baseMap = new HashMap<String, MySQLPermissionBase>();
            cache.put(type, baseMap);
        }
        base.load(data);
        baseMap.put(base.getName(), base);
    }
}
