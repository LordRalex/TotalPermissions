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
package net.ae97.totalpermissions.sql;

import net.ae97.totalpermissions.configuration.SerializedConfiguration;
import net.ae97.totalpermissions.permission.PermissionType;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import java.sql.SQLException;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
@Entity
@Table(name = "tp_perms")
public class PermissionPersistance implements Serializable {

    @Id
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String type;
    @NotNull
    private SerializedConfiguration configSection;

    public void setID(int i) {
        id = i;
    }

    public int getID() {
        return id;
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public void setSection(ConfigurationSection s) {
        configSection = new SerializedConfiguration(s);
    }

    public ConfigurationSection getSection() throws SQLException {
        return configSection;
    }

    public void setType(PermissionType t) {
        type = t.toString();
    }

    public PermissionType getType() {
        return PermissionType.getType(type);
    }
}
