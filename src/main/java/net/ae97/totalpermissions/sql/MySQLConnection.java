package net.ae97.totalpermissions.sql;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class MySQLConnection implements SQLConnection {

    private final String hostname, username, password, database;
    private String url;
    private Connection connection;
    private final int port;

    public MySQLConnection(Map<String, String> params) throws SQLException {
        this(params.get("host"), params.get("user"), params.get("pass"), params.get("db"), Integer.parseInt(params.get("port")));
    }

    public MySQLConnection(String host, String user, String pass, String db, int p) throws SQLException {
        hostname = host;
        username = user;
        password = pass;
        database = db;
        port = p;
    }

    @Override
    public void connect() throws SQLException {
        url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
        connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public void disconnect() throws SQLException {
        connection.close();
    }

    @Override
    public ResultSet execute(String statement, Object... args) throws SQLException {
        PreparedStatement state = connection.prepareStatement(statement);
        for (int i = 0; i < args.length; i++) {
            state.setObject(i + 1, args[i]);
        }
        boolean ret = state.execute();
        if (ret) {
            return state.getResultSet();
        } else {
            return null;
        }
    }

    @Override
    public ResultSet executeQuery(String statement, Object... args) throws SQLException {
        PreparedStatement state = connection.prepareStatement(statement);
        for (int i = 0; i < args.length; i++) {
            state.setObject(i + 1, args[i]);
        }
        return state.executeQuery();
    }

    @Override
    public void executeUpdate(String statement, Object... args) throws SQLException {
        PreparedStatement state = connection.prepareStatement(statement);
        for (int i = 0; i < args.length; i++) {
            state.setObject(i + 1, args[i]);
        }
        state.executeUpdate();
    }
}
