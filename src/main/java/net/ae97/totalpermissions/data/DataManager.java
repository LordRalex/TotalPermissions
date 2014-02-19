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
public class DataManager implements DataHolder {

    private final DataHolder dataHolder;
    private final TotalPermissions plugin;

    public DataManager(TotalPermissions p, DataHolder holder) {
        dataHolder = holder;
        plugin = p;
    }

    public void saveData(PermissionBase data) throws DataSaveFailedException {

    }

    public void loadData(PermissionType type, String name) throws DataLoadFailedException {

    }

    public void reloadData(PermissionBase data) throws DataLoadFailedException, DataSaveFailedException {

    }

    public void reloadData(PermissionType type, String name) throws DataLoadFailedException, DataSaveFailedException {

    }

    public void apply(PermissionBase base, CommandSender sender, World world) {
        List<String> permissions = base.getPermissions(world == null ? null : world.getName());
        PermissionAttachment att = sender.addAttachment(plugin);
        for (String perm : permissions) {
            att.setPermission(perm.startsWith("-") ? perm.substring(1) : perm, !perm.startsWith("-"));
        }
    }

    @Override
    public void load() throws DataLoadFailedException {
        dataHolder.load();
    }

    @Override
    public PermissionUser getUser(String name) {
        return dataHolder.getUser(name);
    }

    @Override
    public PermissionGroup getGroup(String name) {
        return dataHolder.getGroup(name);
    }

    @Override
    public PermissionBase get(PermissionType type, String name) {
        return dataHolder.get(type, name);
    }

    @Override
    public PermissionWorld getWorld(String name) {
        return dataHolder.getWorld(name);
    }

    @Override
    public PermissionEntity getEntity(String name) {
        return dataHolder.getEntity(name);
    }

    @Override
    public PermissionOp getOP() {
        return dataHolder.getOP();
    }

    @Override
    public PermissionConsole getConsole() {
        return dataHolder.getConsole();
    }

    @Override
    public PermissionRcon getRcon() {
        return dataHolder.getRcon();
    }

    @Override
    public Set<String> getGroups() {
        return dataHolder.getGroups();
    }

    @Override
    public Set<String> getUsers() {
        return dataHolder.getUsers();
    }

    @Override
    public Set<String> getWorlds() {
        return dataHolder.getWorlds();
    }

    @Override
    public Set<String> getEntities() {
        return dataHolder.getEntities();
    }

    @Override
    public void save(PermissionBase holder) throws DataSaveFailedException {
        dataHolder.save(holder);
    }

    @Override
    public void loadUser(String name) throws DataLoadFailedException {
        dataHolder.loadUser(name);
    }

    @Override
    public void loadGroup(String name) throws DataLoadFailedException {
        dataHolder.loadGroup(name);
    }

    @Override
    public void loadWorld(String name) throws DataLoadFailedException {
        dataHolder.loadWorld(name);
    }

    @Override
    public void loadEntity(String name) throws DataLoadFailedException {
        dataHolder.loadEntity(name);
    }

    @Override
    public void load(PermissionType type, String name) throws DataLoadFailedException {
        dataHolder.load(type, name);
    }
}
