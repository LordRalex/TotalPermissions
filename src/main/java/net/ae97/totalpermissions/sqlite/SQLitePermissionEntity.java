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

import java.util.Map;
import java.util.Set;
import net.ae97.totalpermissions.base.PermissionEntity;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import net.ae97.totalpermissions.type.PermissionType;

/**
 * @author Lord_Ralex
 */
public class SQLitePermissionEntity extends SQLitePermissionBase implements PermissionEntity {

    @Override
    public void load() throws DataLoadFailedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save() throws DataSaveFailedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> getOptions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getDeclaredPermissions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionType getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> getOptions(String world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getOption(String option) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getOption(String option, String world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOption(String key, Object option) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOption(String key, Object option, String world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getDeclaredPermissions(String world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getPermissions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getPermissions(String world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addPermission(String perm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addPermission(String perm, String world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removePermission(String perm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removePermission(String perm, String world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
