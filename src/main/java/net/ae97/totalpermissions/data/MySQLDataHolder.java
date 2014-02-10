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

import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.lang.Lang;
import net.ae97.totalpermissions.permission.PermissionType;
import net.ae97.totalpermissions.util.DataHolderMerger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class MySQLDataHolder extends MemoryDataHolder {

    public MySQLDataHolder() throws SQLException {
    }

    @Override
    public void setup() {
        super.setup();
        TotalPermissions plugin = TotalPermissions.getPlugin();
        if (new File(plugin.getDataFolder(), "mysql.yml").exists()) {
            plugin.log(Level.INFO, Lang.DATAHOLDER_MYSQL_IMPORT);
            try {
                File yamlFile = new File(plugin.getDataFolder(), "mysql.yml");
                YamlDataHolder yaml = new YamlDataHolder(yamlFile);
                yaml.setup();
                new DataHolderMerger(plugin, this).merge(yaml);
                File imports = new File(plugin.getDataFolder(), "imports");
                imports.mkdirs();
                yamlFile.renameTo(new File(imports, "mysql.yml"));
                yamlFile.delete();
                plugin.log(Level.INFO, Lang.DATAHOLDER_MYSQL_IMPORTCOMPLETE);
            } catch (InvalidConfigurationException ex) {
                plugin.log(Level.SEVERE, Lang.DATAHOLDER_MYSQL_IMPORTINVALID);
                plugin.getLogger().severe(ex.getMessage());
                plugin.debugLog(ex);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, Lang.ERROR_GENERIC.getMessage(), ex);
            }
        }

        Map<String, ConfigurationSection> map = memory.get(PermissionType.GROUPS);
        memory.put(PermissionType.GROUPS, map);
    }

    @Override
    public void load(PermissionType type, String name) throws InvalidConfigurationException {
    }

    @Override
    public void save(PermissionType type, String name) {
    }

    @Override
    public Set<String> getKeys(PermissionType type) {
        return new HashSet<String>();
    }
}
