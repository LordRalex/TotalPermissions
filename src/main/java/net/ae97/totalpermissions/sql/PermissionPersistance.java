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

import net.ae97.totalpermissions.permission.PermissionType;
import com.avaje.ebean.validation.NotNull;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

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
    private String name = "";
    @NotNull
    private String type;
    @Column(columnDefinition = "TEXT")
    private String configSection;

    public void setId(int i) {
        id = i;
    }

    public int getId() {
        return id;
    }

    public void setName(String n) {
        name = n.toLowerCase();
    }

    public String getName() {
        return name.toLowerCase();
    }

    public void setConfigSection(String s) {
        configSection = s;
    }

    public String getConfigSection() {
        return configSection;
    }

    public void setConfig(ConfigurationSection cfg) {
        YamlConfiguration temp = new YamlConfiguration();
        if (cfg instanceof YamlConfiguration) {
            temp = (YamlConfiguration) cfg;
        } else {
            Set<String> keys = cfg.getKeys(true);
            for (String key : keys) {
                temp.set(key, cfg.get(key));
            }
        }
        configSection = temp.saveToString();
    }

    public ConfigurationSection getConfig() throws InvalidConfigurationException {
        YamlConfiguration temp = new YamlConfiguration();
        temp.loadFromString(configSection);
        return temp;
    }

    public void setType(PermissionType t) {
        setType(t.toString());
    }

    public void setType(String t) {
        type = t;
    }

    public String getType() {
        return type;
    }

    public PermissionType getPermissionType() {
        return PermissionType.getType(type);
    }
}
