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
import net.ae97.totalpermissions.base.PermissionBase;

/**
 * @author Lord_Ralex
 */
public abstract class MySQLPermissionBase implements PermissionBase {

    protected final Connection connection;
    private boolean debug = false;

    public MySQLPermissionBase(Connection conn) {
        connection = conn;
    }

    @Override
    public final boolean isDebug() {
        return debug;
    }

    @Override
    public final boolean setDebug(boolean d) {
        return (debug = d);
    }

}
