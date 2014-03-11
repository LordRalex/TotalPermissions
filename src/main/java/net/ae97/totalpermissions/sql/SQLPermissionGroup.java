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
package net.ae97.totalpermissions.sql;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.base.PermissionGroup;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.type.PermissionType;
import org.bukkit.Bukkit;

/**
 * @author Lord_Ralex
 */
public class SQLPermissionGroup extends SQLPermissionBase implements PermissionGroup {

    private final List<String> inheritence = new LinkedList<String>();
    private int rank = 0;
    private boolean defaultGroup = false;

    public SQLPermissionGroup(String n) {
        super(n);
    }

    @Override
    public List<String> getInheritence() {
        return inheritence;
    }

    @Override
    public void removeInheritence(String inher) {
        do {
            inheritence.remove(inher);
        } while (inheritence.contains(inher));
    }

    @Override
    public void addInheritence(String inher) {
        if (!inheritence.contains(inher)) {
            inheritence.add(inher);
        }
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int newRank) {
        rank = newRank;
    }

    @Override
    public PermissionType getType() {
        return PermissionType.GROUP;
    }

    @Override
    public boolean isDefault() {
        return defaultGroup;
    }

    @Override
    public boolean setDefault(boolean def) {
        return (defaultGroup = def);
    }

    @Override
    public List<String> getPermissions(String world) {
        List<String> perms = super.getPermissions(world);

        TotalPermissions plugin = (TotalPermissions) Bukkit.getPluginManager().getPlugin("TotalPermissions");
        for (String group : inheritence) {
            try {
                PermissionGroup permGroup = plugin.getDataManager().getGroup(group);
                if (permGroup != null) {
                    perms.addAll(permGroup.getPermissions());
                }
            } catch (DataLoadFailedException ex) {
                plugin.getLogger().log(Level.SEVERE, "An error occured on loading " + group, ex);
            }
        }

        return perms;
    }

    @Override
    protected Map<String, Object> getSaveData() {
        Map<String, Object> mappings = super.getSaveData();
        mappings.put("inheritence", inheritence);
        mappings.put("rank", rank);
        mappings.put("default", defaultGroup);
        return mappings;
    }

    protected static Map<String, String> getColumns() {
        Map<String, String> columns = SQLPermissionBase.getColumns();
        columns.put("inheritence", "TEXT");
        columns.put("default", "BOOLEAN");
        columns.put("rank", "INTEGER");
        return columns;
    }
}
