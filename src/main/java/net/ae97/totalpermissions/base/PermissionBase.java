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
package net.ae97.totalpermissions.base;

import java.util.Map;
import java.util.Set;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import net.ae97.totalpermissions.type.PermissionType;

/**
 * @author Lord_Ralex
 */
public interface PermissionBase {

    public void load() throws DataLoadFailedException;

    public void save() throws DataSaveFailedException;

    public String getName();

    public PermissionType getType();

    public boolean isDebug();

    public boolean setDebug(boolean debug);

    public Map<String, Object> getOptions();

    public Map<String, Object> getOptions(String world);

    public Object getOption(String option);

    public Object getOption(String option, String world);

    public void setOption(String key, Object option);

    public void setOption(String key, Object option, String world);

    public Set<String> getDeclaredPermissions();

    public Set<String> getDeclaredPermissions(String world);

    public Set<String> getPermissions();

    public Set<String> getPermissions(String world);

    public boolean addPermission(String perm);

    public boolean addPermission(String perm, String world);

    public boolean removePermission(String perm);

    public boolean removePermission(String perm, String world);
}
