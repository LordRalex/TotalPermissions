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
package net.ae97.totalpermissions.data;

import java.util.Set;
import java.util.UUID;
import net.ae97.totalpermissions.base.PermissionBase;
import net.ae97.totalpermissions.base.PermissionConsole;
import net.ae97.totalpermissions.base.PermissionEntity;
import net.ae97.totalpermissions.base.PermissionGroup;
import net.ae97.totalpermissions.base.PermissionOp;
import net.ae97.totalpermissions.base.PermissionRcon;
import net.ae97.totalpermissions.base.PermissionUser;
import net.ae97.totalpermissions.base.PermissionWorld;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import net.ae97.totalpermissions.type.PermissionType;

/**
 * @author Lord_Ralex
 */
public interface DataHolder<T extends PermissionBase> {

    public void load() throws DataLoadFailedException;

    public void load(PermissionType type, String name) throws DataLoadFailedException;

    public void load(PermissionType type, UUID name) throws DataLoadFailedException;

    public void save(T holder) throws DataSaveFailedException;

    public void loadUser(String name) throws DataLoadFailedException;

    public void loadUser(UUID name) throws DataLoadFailedException;

    public void loadGroup(String name) throws DataLoadFailedException;

    public void loadGroup(UUID name) throws DataLoadFailedException;

    public void loadWorld(String name) throws DataLoadFailedException;

    public void loadWorld(UUID name) throws DataLoadFailedException;

    public void loadEntity(String name) throws DataLoadFailedException;

    public void loadEntity(UUID name) throws DataLoadFailedException;

    public void loadConsole() throws DataLoadFailedException;

    public void loadOp() throws DataLoadFailedException;

    public void loadRcon() throws DataLoadFailedException;

    public PermissionBase get(PermissionType type, String name) throws DataLoadFailedException;

    public PermissionBase get(PermissionType type, UUID name) throws DataLoadFailedException;

    public PermissionUser getUser(String name) throws DataLoadFailedException;

    public PermissionUser getUser(UUID name) throws DataLoadFailedException;

    public PermissionGroup getGroup(String name) throws DataLoadFailedException;

    public PermissionGroup getGroup(UUID name) throws DataLoadFailedException;

    public PermissionWorld getWorld(String name) throws DataLoadFailedException;

    public PermissionWorld getWorld(UUID name) throws DataLoadFailedException;

    public PermissionEntity getEntity(String name) throws DataLoadFailedException;

    public PermissionEntity getEntity(UUID name) throws DataLoadFailedException;

    public PermissionOp getOP() throws DataLoadFailedException;

    public PermissionConsole getConsole() throws DataLoadFailedException;

    public PermissionRcon getRcon() throws DataLoadFailedException;

    public Set<String> getGroups() throws DataLoadFailedException;

    public Set<String> getUsers() throws DataLoadFailedException;

    public Set<String> getWorlds() throws DataLoadFailedException;

    public Set<String> getEntities() throws DataLoadFailedException;
}
