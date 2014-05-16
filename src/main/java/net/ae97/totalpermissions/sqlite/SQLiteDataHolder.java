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
import java.sql.DriverManager;
import java.sql.SQLException;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.sql.SQLDataHolder;
import net.ae97.totalpermissions.sql.SQLPermissionBase;

/**
 * @author Lord_Ralex
 */
public class SQLiteDataHolder extends SQLDataHolder<SQLPermissionBase> {

    private final String path;

    public SQLiteDataHolder(File file) {
        path = file.getPath();
    }

    public SQLiteDataHolder(String p) {
        path = p;
    }

    @Override
    protected void loadDatabase() throws DataLoadFailedException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            throw new DataLoadFailedException(ex);
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException ex) {
            throw new DataLoadFailedException(ex);
        }
    }
}
