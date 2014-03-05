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
package net.ae97.totalpermissions.importer;

import java.util.Map;
import java.util.Set;
import net.ae97.totalpermissions.base.PermissionBase;
import net.ae97.totalpermissions.base.PermissionGroup;
import net.ae97.totalpermissions.base.PermissionUser;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;

/**
 * @author Lord_Ralex
 */
public class DataHolderImporter extends Thread {

    private final DataHolder source, target;
    private Exception ex = null;

    public DataHolderImporter(DataHolder s, DataHolder t) {
        source = s;
        target = t;
    }

    @Override
    public void run() {
        try {
            PermissionBase sourceBase;
            PermissionBase targetBase;
            sourceBase = source.getConsole();
            targetBase = target.getConsole();
            importBasics(sourceBase, targetBase);
            targetBase.save();
            sourceBase = source.getRcon();
            targetBase = target.getRcon();
            importBasics(sourceBase, targetBase);
            targetBase.save();
            sourceBase = source.getOP();
            targetBase = target.getOP();
            importBasics(sourceBase, targetBase);
            targetBase.save();

            for (String name : (Set<String>) source.getUsers()) {
                PermissionUser sourceUser = source.getUser(name);
                PermissionUser targetUser = target.getUser(name);
                importBasics(sourceUser, targetUser);
                for (String group : sourceUser.getGroups()) {
                    if (group.startsWith("-")) {
                        targetUser.removeGroup(group.substring(1));
                    } else {
                        targetUser.addGroup(group);
                    }
                }
                targetUser.save();
            }

            for (String name : (Set<String>) source.getGroups()) {
                PermissionGroup sourceGroup = source.getGroup(name);
                PermissionGroup targetGroup = target.getGroup(name);
                importBasics(sourceGroup, targetGroup);
                for (String inheritence : sourceGroup.getInheritence()) {
                    if (inheritence.startsWith("-")) {
                        targetGroup.removeInheritence(inheritence.substring(1));
                    } else {
                        targetGroup.addInheritence(inheritence);
                    }
                }
                targetGroup.setRank(sourceGroup.getRank());
                targetGroup.save();
            }

            for (String user : (Set<String>) source.getWorlds()) {
                sourceBase = source.getWorld(user);
                targetBase = target.getWorld(user);
                importBasics(sourceBase, targetBase);
                targetBase.save();
            }

            for (String name : (Set<String>) source.getEntities()) {
                sourceBase = source.getEntity(name);
                targetBase = target.getEntity(name);
                importBasics(sourceBase, targetBase);
                targetBase.save();
            }

        } catch (DataLoadFailedException e) {
            ex = e;
        } catch (DataSaveFailedException e) {
            ex = e;
        }
    }

    public Exception getException() {
        return ex;
    }

    public boolean hasCrashed() {
        return ex == null;
    }

    private void importBasics(PermissionBase s, PermissionBase t) {
        for (String perm : s.getDeclaredPermissions()) {
            t.addPermission(perm);
        }
        for (String world : s.getOptions().keySet()) {
            Map<String, Object> options = s.getOptions(world);
            for (String key : options.keySet()) {
                t.setOption(key, options.get(key), world);
            }
        }
    }
}
