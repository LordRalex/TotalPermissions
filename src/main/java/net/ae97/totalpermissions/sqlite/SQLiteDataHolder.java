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
package net.ae97.totalpermissions.sqlite;

import java.io.File;
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
import net.ae97.totalpermissions.base.PermissionBase;
import net.ae97.totalpermissions.base.PermissionConsole;
import net.ae97.totalpermissions.base.PermissionEntity;
import net.ae97.totalpermissions.base.PermissionGroup;
import net.ae97.totalpermissions.base.PermissionOp;
import net.ae97.totalpermissions.base.PermissionRcon;
import net.ae97.totalpermissions.base.PermissionUser;
import net.ae97.totalpermissions.base.PermissionWorld;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import net.ae97.totalpermissions.type.PermissionType;
import org.bukkit.Bukkit;

/**
 * @author Lord_Ralex
 */
public class SQLiteDataHolder implements DataHolder {

    private Connection connection;
    private final EnumMap<PermissionType, HashMap<String, SQLitePermissionBase>> cache = new EnumMap<PermissionType, HashMap<String, SQLitePermissionBase>>(PermissionType.class);

    public SQLiteDataHolder() {
        connection = null;
    }

    @Override
    public void load() throws DataLoadFailedException {
        cache.clear();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            throw new DataLoadFailedException(ex);
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + new File(Bukkit.getPluginManager().getPlugin("TotalPermissions").getDataFolder(), "permissions.db").getPath());
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
        SQLitePermissionBase base = new SQLitePermissionUser(name);
        load(PermissionType.USER, base, data);
    }

    @Override
    public void loadGroup(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("groups", "name", name);
        SQLitePermissionBase base = new SQLitePermissionGroup(name);
        load(PermissionType.GROUP, base, data);
    }

    @Override
    public void loadWorld(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("worlds", "name", name);
        SQLitePermissionBase base = new SQLitePermissionWorld(name);
        load(PermissionType.WORLD, base, data);
    }

    @Override
    public void loadEntity(String name) throws DataLoadFailedException {
        Map<String, Object> data = getData("entities", "name", name);
        SQLitePermissionBase base = new SQLitePermissionEntity(name);
        load(PermissionType.ENTITY, base, data);
    }

    @Override
    public void loadConsole() throws DataLoadFailedException {
        Map<String, Object> data = getData("server", "name", "console");
        SQLitePermissionBase base = new SQLitePermissionConsole();
        load(PermissionType.CONSOLE, base, data);
    }

    @Override
    public void loadOp() throws DataLoadFailedException {
        Map<String, Object> data = getData("server", "name", "op");
        SQLitePermissionBase base = new SQLitePermissionOp();
        load(PermissionType.OP, base, data);
    }

    @Override
    public void loadRcon() throws DataLoadFailedException {
        Map<String, Object> data = getData("server", "name", "rcon");
        SQLitePermissionBase base = new SQLitePermissionRcon();
        load(PermissionType.RCON, base, data);
    }

    @Override
    public PermissionBase get(PermissionType type, String name) throws DataLoadFailedException {
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
    public PermissionUser getUser(String name) throws DataLoadFailedException {
        checkCache(PermissionType.USER, name);
        return (PermissionUser) cache.get(PermissionType.USER).get(name);
    }

    @Override
    public PermissionGroup getGroup(String name) throws DataLoadFailedException {
        checkCache(PermissionType.GROUP, name);
        return (PermissionGroup) cache.get(PermissionType.GROUP).get(name);
    }

    @Override
    public PermissionWorld getWorld(String name) throws DataLoadFailedException {
        checkCache(PermissionType.WORLD, name);
        return (PermissionWorld) cache.get(PermissionType.WORLD).get(name);
    }

    @Override
    public PermissionEntity getEntity(String name) throws DataLoadFailedException {
        checkCache(PermissionType.ENTITY, name);
        return (PermissionEntity) cache.get(PermissionType.ENTITY).get(name);
    }

    @Override
    public PermissionOp getOP() throws DataLoadFailedException {
        checkCache(PermissionType.OP, null);
        return (PermissionOp) cache.get(PermissionType.OP).get(null);
    }

    @Override
    public PermissionConsole getConsole() throws DataLoadFailedException {
        checkCache(PermissionType.CONSOLE, null);
        return (PermissionConsole) cache.get(PermissionType.CONSOLE).get(null);
    }

    @Override
    public PermissionRcon getRcon() throws DataLoadFailedException {
        checkCache(PermissionType.RCON, null);
        return (PermissionRcon) cache.get(PermissionType.RCON).get(null);
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
    public void save(PermissionBase holder) throws DataSaveFailedException {
        holder.save();
        if (!(holder instanceof SQLitePermissionBase)) {
            throw new DataSaveFailedException("SQLite cannot save a non-SQLite PermissionBase");
        }
        SQLitePermissionBase base = (SQLitePermissionBase) holder;
        Map<String, Object> saveData = base.getSaveData();
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
            switch (base.getType()) {
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
            statement.setString((keys.length * 2) + 1, base.getName());
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

    protected final void load(PermissionType type, SQLitePermissionBase base, Map<String, Object> data) throws DataLoadFailedException {
        HashMap<String, SQLitePermissionBase> baseMap = cache.get(type);
        if (baseMap == null) {
            baseMap = new HashMap<String, SQLitePermissionBase>();
            cache.put(type, baseMap);
        }
        base.load(data);
        baseMap.put(base.getName(), base);
    }
}
