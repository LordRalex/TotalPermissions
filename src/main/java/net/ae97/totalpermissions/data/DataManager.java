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

import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.ae97.totalpermissions.TotalPermissions;
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
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachment;

/**
 * @author Lord_Ralex
 */
public final class DataManager {

    private final DataHolder<PermissionBase> dataHolder;
    private final TotalPermissions plugin;

    public DataManager(TotalPermissions p, DataHolder< PermissionBase> holder) {
        dataHolder = holder;
        plugin = p;
    }

    public void apply(PermissionBase base, CommandSender sender, World world) {
        List<String> permissions = base.getPermissions(world == null ? null : world.getName());
        PermissionAttachment att = sender.addAttachment(plugin);
        for (String perm : permissions) {
            att.setPermission(perm.startsWith("-") ? perm.substring(1) : perm, !perm.startsWith("-"));
        }
    }

    public void load() throws DataLoadFailedException {
        dataHolder.load();
    }

    public PermissionBase get(PermissionType type, String name) throws DataLoadFailedException {
        return dataHolder.get(type, name);
    }

    public PermissionBase get(PermissionType type, UUID uuid) throws DataLoadFailedException {
        return dataHolder.get(type, uuid);
    }

    public PermissionUser getUser(String name) throws DataLoadFailedException {
        return dataHolder.getUser(name);
    }

    public PermissionUser getUser(UUID uuid) throws DataLoadFailedException {
        return dataHolder.getUser(uuid);
    }

    public PermissionGroup getGroup(String name) throws DataLoadFailedException {
        return dataHolder.getGroup(name);
    }

    public PermissionGroup getGroup(UUID uuid) throws DataLoadFailedException {
        return dataHolder.getGroup(uuid);
    }

    public PermissionWorld getWorld(String name) throws DataLoadFailedException {
        return dataHolder.getWorld(name);
    }

    public PermissionWorld getWorld(UUID uuid) throws DataLoadFailedException {
        return dataHolder.getWorld(uuid);
    }

    public PermissionEntity getEntity(String name) throws DataLoadFailedException {
        return dataHolder.getEntity(name);
    }

    public PermissionEntity getEntity(UUID uuid) throws DataLoadFailedException {
        return dataHolder.getEntity(uuid);
    }

    public PermissionOp getOP() throws DataLoadFailedException {
        return dataHolder.getOP();
    }

    public PermissionConsole getConsole() throws DataLoadFailedException {
        return dataHolder.getConsole();
    }

    public PermissionRcon getRcon() throws DataLoadFailedException {
        return dataHolder.getRcon();
    }

    public Set<String> getGroups() throws DataLoadFailedException {
        return dataHolder.getGroups();
    }

    public Set<String> getUsers() throws DataLoadFailedException {
        return dataHolder.getUsers();
    }

    public Set<String> getWorlds() throws DataLoadFailedException {
        return dataHolder.getWorlds();
    }

    public Set<String> getEntities() throws DataLoadFailedException {
        return dataHolder.getEntities();
    }

    public void save(PermissionBase holder) throws DataSaveFailedException {
        dataHolder.save(holder);
    }

    public void load(PermissionType type, String name) throws DataLoadFailedException {
        dataHolder.load(type, name);
    }

    public void load(PermissionType type, UUID uuid) throws DataLoadFailedException {
        dataHolder.load(type, uuid);
    }

    public void loadUser(String name) throws DataLoadFailedException {
        dataHolder.loadUser(name);
    }

    public void loadUser(UUID uuid) throws DataLoadFailedException {
        dataHolder.loadUser(uuid);
    }

    public void loadGroup(String name) throws DataLoadFailedException {
        dataHolder.loadGroup(name);
    }

    public void loadGroup(UUID uuid) throws DataLoadFailedException {
        dataHolder.loadGroup(uuid);
    }

    public void loadWorld(String name) throws DataLoadFailedException {
        dataHolder.loadWorld(name);
    }

    public void loadWorld(UUID uuid) throws DataLoadFailedException {
        dataHolder.loadWorld(uuid);
    }

    public void loadEntity(String name) throws DataLoadFailedException {
        dataHolder.loadEntity(name);
    }

    public void loadEntity(UUID uuid) throws DataLoadFailedException {
        dataHolder.loadEntity(uuid);
    }

    public void loadConsole() throws DataLoadFailedException {
        dataHolder.loadConsole();
    }

    public void loadOp() throws DataLoadFailedException {
        dataHolder.loadOp();
    }

    public void loadRcon() throws DataLoadFailedException {
        dataHolder.loadRcon();
    }
}
