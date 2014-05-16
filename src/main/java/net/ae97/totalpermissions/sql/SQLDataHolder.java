/*
 * Copyright (C) 2014 Lord_Ralex
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
package net.ae97.totalpermissions.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import net.ae97.totalpermissions.type.PermissionType;

/**
 * @author Lord_Ralex
 */
public abstract class SQLDataHolder<T> implements DataHolder<SQLPermissionBase> {

    protected Connection connection;
    private final EnumMap<PermissionType, HashMap<String, SQLPermissionBase>> cache = new EnumMap<PermissionType, HashMap<String, SQLPermissionBase>>(PermissionType.class);

    @Override
    public void load() throws DataLoadFailedException {
        cache.clear();
        loadDatabase();
        PreparedStatement createDatabase = null;
        try {
            connection = getConnection();
            createDatabase = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS totalpermissions ()");
            createDatabase.execute();
            updateTables();
        } catch (SQLException ex) {
            throw new DataLoadFailedException(ex);
        } finally {
            if (createDatabase != null) {
                try {
                    createDatabase.close();
                } catch (SQLException ex) {
                    throw new DataLoadFailedException(ex);
                }
            }
        }
    }

    protected abstract void loadDatabase() throws DataLoadFailedException;

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
    public void load(PermissionType type, UUID uuid) throws DataLoadFailedException {
        load(type, uuid.toString());
    }

    @Override
    public void loadUser(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("users", "name", name);
        load(PermissionType.USER, name, data);
    }

    @Override
    public void loadUser(UUID uuid) throws DataLoadFailedException {
        loadUser(uuid.toString());
    }

    @Override
    public void loadGroup(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("groups", "name", name);
        load(PermissionType.GROUP, name, data);
    }

    @Override
    public void loadGroup(UUID uuid) throws DataLoadFailedException {
        loadGroup(uuid.toString());
    }

    @Override
    public void loadWorld(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("worlds", "name", name);
        load(PermissionType.WORLD, name, data);
    }

    @Override
    public void loadWorld(UUID uuid) throws DataLoadFailedException {
        loadWorld(uuid.toString());
    }

    @Override
    public void loadEntity(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("entities", "name", name);
        load(PermissionType.ENTITY, name, data);
    }

    @Override
    public void loadEntity(UUID uuid) throws DataLoadFailedException {
        loadEntity(uuid.toString());
    }

    @Override
    public void loadConsole() throws DataLoadFailedException {
        Map<String, Object> data = getData("server", "name", "console");
        load(PermissionType.CONSOLE, "console", data);
    }

    @Override
    public void loadOp() throws DataLoadFailedException {
        Map<String, Object> data = getData("server", "name", "op");
        load(PermissionType.OP, "op", data);
    }

    @Override
    public void loadRcon() throws DataLoadFailedException {
        Map<String, Object> data = getData("server", "name", "rcon");
        load(PermissionType.RCON, "rcon", data);
    }

    @Override
    public SQLPermissionBase get(PermissionType type, String name) throws DataLoadFailedException {
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
    public SQLPermissionBase get(PermissionType type, UUID uuid) throws DataLoadFailedException {
        return get(type, uuid.toString());
    }

    @Override
    public SQLPermissionUser getUser(String name) throws DataLoadFailedException {
        checkCache(PermissionType.USER, name);
        return (SQLPermissionUser) cache.get(PermissionType.USER).get(name);
    }

    @Override
    public SQLPermissionUser getUser(UUID uuid) throws DataLoadFailedException {
        return getUser(uuid.toString());
    }

    @Override
    public SQLPermissionGroup getGroup(String name) throws DataLoadFailedException {
        checkCache(PermissionType.GROUP, name);
        return (SQLPermissionGroup) cache.get(PermissionType.GROUP).get(name);
    }

    @Override
    public SQLPermissionGroup getGroup(UUID uuid) throws DataLoadFailedException {
        return getGroup(uuid.toString());
    }

    @Override
    public SQLPermissionWorld getWorld(String name) throws DataLoadFailedException {
        checkCache(PermissionType.WORLD, name);
        return (SQLPermissionWorld) cache.get(PermissionType.WORLD).get(name);
    }

    @Override
    public SQLPermissionWorld getWorld(UUID uuid) throws DataLoadFailedException {
        return getWorld(uuid.toString());
    }

    @Override
    public SQLPermissionEntity getEntity(String name) throws DataLoadFailedException {
        checkCache(PermissionType.ENTITY, name);
        return (SQLPermissionEntity) cache.get(PermissionType.ENTITY).get(name);
    }

    @Override
    public SQLPermissionEntity getEntity(UUID uuid) throws DataLoadFailedException {
        return getEntity(uuid.toString());
    }

    @Override
    public SQLPermissionOp getOP() throws DataLoadFailedException {
        checkCache(PermissionType.OP, null);
        return (SQLPermissionOp) cache.get(PermissionType.OP).get(null);
    }

    @Override
    public SQLPermissionConsole getConsole() throws DataLoadFailedException {
        checkCache(PermissionType.CONSOLE, null);
        return (SQLPermissionConsole) cache.get(PermissionType.CONSOLE).get(null);
    }

    @Override
    public SQLPermissionRcon getRcon() throws DataLoadFailedException {
        checkCache(PermissionType.RCON, null);
        return (SQLPermissionRcon) cache.get(PermissionType.RCON).get(null);
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
    public void save(SQLPermissionBase holder) throws DataSaveFailedException {
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

    protected void checkCache(PermissionType type, String name) throws DataLoadFailedException {
        if (cache.get(type) == null || cache.get(type).get(name == null ? null : name.toLowerCase()) == null) {
            load(type, name);
        }
    }

    protected final void load(PermissionType type, String name, Map<String, Object> data) throws DataLoadFailedException {
        HashMap<String, SQLPermissionBase> baseMap = cache.get(type);
        if (baseMap == null) {
            baseMap = new HashMap<String, SQLPermissionBase>();
            cache.put(type, baseMap);
        }
        SQLPermissionBase base = getBaseClass(type, name);
        base.load(data);
        baseMap.put(base.getName(), base);
    }

    protected void updateTable(String name, Map<String, String> columns) throws SQLException {
        PreparedStatement statement = null;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ? (");
            for (int i = 0; i < columns.size(); i++) {
                builder.append("? ?");
                if (i + 1 < columns.size()) {
                    builder.append(", ");
                }
            }
            builder.append(")");
            statement = getConnection().prepareStatement(builder.toString());
            statement.execute();
        } catch (SQLException e) {
            throw e;
        } catch (DataLoadFailedException e) {
            throw new SQLException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    protected final Connection getConnection() throws DataLoadFailedException {
        if (connection == null) {
            throw new DataLoadFailedException("Connection has not been established to the database");
        }
        return connection;
    }

    protected void updateTables() throws SQLException {
        updateTable("users", SQLPermissionUser.getColumns());
        updateTable("entities", SQLPermissionEntity.getColumns());
        updateTable("groups", SQLPermissionGroup.getColumns());
        updateTable("worlds", SQLPermissionWorld.getColumns());
        updateTable("server", SQLPermissionBase.getColumns());
    }

    protected SQLPermissionBase getBaseClass(PermissionType type, String name) {
        switch (type) {
            case USER:
                return new SQLPermissionUser(name);
            case GROUP:
                return new SQLPermissionGroup(name);
            case WORLD:
                return new SQLPermissionWorld(name);
            case ENTITY:
                return new SQLPermissionEntity(name);
            case OP:
                return new SQLPermissionOp();
            case CONSOLE:
                return new SQLPermissionConsole();
            case RCON:
                return new SQLPermissionRcon();
            default:
                return null;
        }
    }
}
