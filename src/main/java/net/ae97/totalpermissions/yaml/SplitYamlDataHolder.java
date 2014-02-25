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
package net.ae97.totalpermissions.yaml;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Set;
import net.ae97.totalpermissions.base.PermissionBase;
import net.ae97.totalpermissions.base.PermissionConsole;
import net.ae97.totalpermissions.base.PermissionEntity;
import net.ae97.totalpermissions.base.PermissionGroup;
import net.ae97.totalpermissions.base.PermissionOp;
import net.ae97.totalpermissions.base.PermissionRcon;
import net.ae97.totalpermissions.base.PermissionUser;
import net.ae97.totalpermissions.base.PermissionWorld;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.exceptions.DataSaveFailedException;
import net.ae97.totalpermissions.type.PermissionType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Lord_Ralex
 */
public class SplitYamlDataHolder implements DataHolder {

    private final File folder, userFile, groupFile, worldFile, entityFile, specialFile;
    private final YamlConfiguration userYamlConfiguration = new YamlConfiguration();
    private final YamlConfiguration groupYamlConfiguration = new YamlConfiguration();
    private final YamlConfiguration worldYamlConfiguration = new YamlConfiguration();
    private final YamlConfiguration entityYamlConfiguration = new YamlConfiguration();
    private final YamlConfiguration specialYamlConfiguration = new YamlConfiguration();
    private final EnumMap<PermissionType, HashMap<String, YamlPermissionBase>> cache = new EnumMap<PermissionType, HashMap<String, YamlPermissionBase>>(PermissionType.class);

    public SplitYamlDataHolder(File f) {
        folder = f;
        userFile = new File(folder, "users.yml");
        groupFile = new File(folder, "groups.yml");
        worldFile = new File(folder, "worlds.yml");
        entityFile = new File(folder, "entities.yml");
        specialFile = new File(folder, "specials.yml");
    }

    @Override
    public void load() throws DataLoadFailedException {
        try {
            groupYamlConfiguration.load(groupFile);
            userYamlConfiguration.load(userFile);
            cache.clear();
        } catch (IOException ex) {
            throw new DataLoadFailedException(ex);
        } catch (InvalidConfigurationException ex) {
            throw new DataLoadFailedException(ex);
        }
    }

    @Override
    public void load(PermissionType type, String name) throws DataLoadFailedException {
        switch (type) {
            case USER:
                loadUser(name);
                break;
            case GROUP:
                loadGroup(name);
                break;
            case WORLD:
                loadWorld(name);
                break;
            case ENTITY:
                loadEntity(name);
                break;
            case OP:
                loadOp();
                break;
            case RCON:
                loadRcon();
                break;
            case CONSOLE:
                loadConsole();
                break;
        }
    }

    @Override
    public void loadUser(String name) throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.USER);
        ConfigurationSection sec = userYamlConfiguration.getConfigurationSection(name);
        if (sec == null) {
            sec = userYamlConfiguration.createSection(name);
        }
        YamlPermissionBase permBase = new YamlPermissionUser(name.toLowerCase(), sec);
        permBase.load();
        if (base == null) {
            base = new HashMap<String, YamlPermissionBase>();
            cache.put(PermissionType.USER, base);
        }
        base.put(name.toLowerCase(), permBase);
    }

    @Override
    public void loadGroup(String name) throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.GROUP);
        ConfigurationSection sec = groupYamlConfiguration.getConfigurationSection(name);
        if (sec == null) {
            sec = groupYamlConfiguration.createSection(name);
        }
        YamlPermissionBase permBase = new YamlPermissionGroup(name.toLowerCase(), sec);
        permBase.load();
        if (base == null) {
            base = new HashMap<String, YamlPermissionBase>();
            cache.put(PermissionType.GROUP, base);
        }
        base.put(name.toLowerCase(), permBase);
    }

    @Override
    public void loadWorld(String name) throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.WORLD);
        ConfigurationSection sec = worldYamlConfiguration.getConfigurationSection(name);
        if (sec == null) {
            sec = worldYamlConfiguration.createSection(name);
        }
        YamlPermissionBase permBase = new YamlPermissionWorld(name.toLowerCase(), sec);
        permBase.load();
        if (base == null) {
            base = new HashMap<String, YamlPermissionBase>();
            cache.put(PermissionType.WORLD, base);
        }
        base.put(name.toLowerCase(), permBase);
    }

    @Override
    public void loadEntity(String name) throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.ENTITY);
        ConfigurationSection sec = entityYamlConfiguration.getConfigurationSection(name);
        if (sec == null) {
            sec = entityYamlConfiguration.createSection(name);
        }
        YamlPermissionBase permBase = new YamlPermissionEntity(name.toLowerCase(), sec);
        permBase.load();
        if (base == null) {
            base = new HashMap<String, YamlPermissionBase>();
            cache.put(PermissionType.ENTITY, base);
        }
        base.put(name.toLowerCase(), permBase);
    }

    @Override
    public void loadConsole() throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.CONSOLE);
        ConfigurationSection sec = specialYamlConfiguration.getConfigurationSection("console");
        if (sec == null) {
            sec = specialYamlConfiguration.createSection("console");
        }
        YamlPermissionConsole permBase = new YamlPermissionConsole(sec);
        permBase.load();
        if (base == null) {
            base = new HashMap<String, YamlPermissionBase>();
            cache.put(PermissionType.CONSOLE, base);
        }
        base.put(null, permBase);
    }

    @Override
    public void loadOp() throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.OP);
        ConfigurationSection sec = specialYamlConfiguration.getConfigurationSection("op");
        if (sec == null) {
            sec = specialYamlConfiguration.createSection("op");
        }
        YamlPermissionOp permBase = new YamlPermissionOp(sec);
        permBase.load();
        if (base == null) {
            base = new HashMap<String, YamlPermissionBase>();
            cache.put(PermissionType.OP, base);
        }
        base.put(null, permBase);
    }

    @Override
    public void loadRcon() throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.RCON);
        ConfigurationSection sec = specialYamlConfiguration.getConfigurationSection("rcon");
        if (sec == null) {
            sec = specialYamlConfiguration.createSection("rcon");
        }
        YamlPermissionRcon permBase = new YamlPermissionRcon(sec);
        permBase.load();
        if (base == null) {
            base = new HashMap<String, YamlPermissionBase>();
            cache.put(PermissionType.RCON, base);
        }
        base.put(null, permBase);
    }

    @Override
    public PermissionBase get(PermissionType type, String name) throws DataLoadFailedException {
        switch (type) {
            case USER:
                return getUser(name);
            case GROUP:
                return getGroup(name);
            case WORLD:
                return getWorld(name);
            case ENTITY:
                return getEntity(name);
            case OP:
                return getOP();
            case CONSOLE:
                return getConsole();
            case RCON:
                return getRcon();
            default:
                return null;
        }
    }

    @Override
    public PermissionUser getUser(String name) throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.GROUP);
        if (base == null || base.isEmpty() || !base.containsKey(name.toLowerCase()) || base.get(name.toLowerCase()) == null) {
            ConfigurationSection sec = userYamlConfiguration.getConfigurationSection(name);
            if (sec == null) {
                sec = userYamlConfiguration.createSection(name);
            }
            YamlPermissionUser user = new YamlPermissionUser(name.toLowerCase(), sec);
            user.load();
            if (base == null) {
                base = new HashMap<String, YamlPermissionBase>();
                cache.put(PermissionType.USER, base);
            }
            base.put(name.toLowerCase(), user);
            return user;
        }
        return (PermissionUser) base.get(name.toLowerCase());
    }

    @Override
    public PermissionGroup getGroup(String name) throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.GROUP);
        if (base == null || base.isEmpty() || !base.containsKey(name.toLowerCase()) || base.get(name.toLowerCase()) == null) {
            ConfigurationSection sec = groupYamlConfiguration.getConfigurationSection(name);
            if (sec == null) {
                sec = groupYamlConfiguration.createSection(name);
            }
            YamlPermissionGroup group = new YamlPermissionGroup(name.toLowerCase(), sec);
            group.load();
            if (base == null) {
                base = new HashMap<String, YamlPermissionBase>();
                cache.put(PermissionType.GROUP, base);
            }
            base.put(name.toLowerCase(), group);
            return group;
        }
        return (PermissionGroup) base.get(name.toLowerCase());
    }

    @Override
    public PermissionWorld getWorld(String name) throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.WORLD);
        if (base == null || base.isEmpty() || !base.containsKey(name.toLowerCase()) || base.get(name.toLowerCase()) == null) {
            ConfigurationSection worldSec = worldYamlConfiguration.getConfigurationSection(name);
            if (worldSec == null) {
                worldSec = worldYamlConfiguration.createSection(name);
            }
            YamlPermissionWorld world = new YamlPermissionWorld(name.toLowerCase(), worldSec);
            world.load();
            if (base == null) {
                base = new HashMap<String, YamlPermissionBase>();
                cache.put(PermissionType.WORLD, base);
            }
            base.put(name.toLowerCase(), world);
            return world;
        }
        return (PermissionWorld) base.get(name.toLowerCase());
    }

    @Override
    public PermissionEntity getEntity(String name) throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.ENTITY);
        if (base == null || base.isEmpty() || !base.containsKey(name.toLowerCase()) || base.get(name.toLowerCase()) == null) {
            ConfigurationSection entitySec = entityYamlConfiguration.getConfigurationSection(name);
            if (entitySec == null) {
                entitySec = entityYamlConfiguration.createSection(name);
            }
            YamlPermissionEntity entity = new YamlPermissionEntity(name.toLowerCase(), entitySec);
            entity.load();
            if (base == null) {
                base = new HashMap<String, YamlPermissionBase>();
                cache.put(PermissionType.ENTITY, base);
            }
            base.put(name.toLowerCase(), entity);
            return entity;
        }
        return (PermissionEntity) base.get(name.toLowerCase());
    }

    @Override
    public PermissionOp getOP() throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.OP);
        if (base == null || base.isEmpty() || !base.containsKey(null) || base.get(null) == null) {
            ConfigurationSection opSec = specialYamlConfiguration.getConfigurationSection("op");
            if (opSec == null) {
                opSec = specialYamlConfiguration.createSection("op");
            }
            YamlPermissionOp op = new YamlPermissionOp(opSec);
            op.load();
            if (base == null) {
                base = new HashMap<String, YamlPermissionBase>();
                cache.put(PermissionType.OP, base);
            }
            base.put(null, op);
            return op;
        }
        return (PermissionOp) base.get(null);
    }

    @Override
    public PermissionConsole getConsole() throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.CONSOLE);
        if (base == null || base.isEmpty() || !base.containsKey(null) || base.get(null) == null) {
            ConfigurationSection consoleSec = specialYamlConfiguration.getConfigurationSection("console");
            if (consoleSec == null) {
                consoleSec = specialYamlConfiguration.createSection("console");
            }
            YamlPermissionConsole console = new YamlPermissionConsole(consoleSec);
            console.load();
            if (base == null) {
                base = new HashMap<String, YamlPermissionBase>();
                cache.put(PermissionType.CONSOLE, base);
            }
            base.put(null, console);
            return console;
        }
        return (PermissionConsole) base.get(null);
    }

    @Override
    public PermissionRcon getRcon() throws DataLoadFailedException {
        HashMap<String, YamlPermissionBase> base = cache.get(PermissionType.RCON);
        if (base == null || base.isEmpty() || !base.containsKey(null) || base.get(null) == null) {
            ConfigurationSection rconSec = specialYamlConfiguration.getConfigurationSection("rcon");
            if (rconSec == null) {
                rconSec = specialYamlConfiguration.createSection("rcon");
            }
            YamlPermissionRcon rcon = new YamlPermissionRcon(rconSec);
            rcon.load();
            if (base == null) {
                base = new HashMap<String, YamlPermissionBase>();
                cache.put(PermissionType.RCON, base);
            }
            base.put(null, rcon);
            return rcon;
        }
        return (PermissionRcon) base.get(null);
    }

    @Override
    public Set<String> getGroups() {
        return groupYamlConfiguration.getKeys(false);
    }

    @Override
    public Set<String> getUsers() {
        return userYamlConfiguration.getKeys(false);
    }

    @Override
    public Set<String> getWorlds() {
        return worldYamlConfiguration.getKeys(false);
    }

    @Override
    public Set<String> getEntities() {
        return entityYamlConfiguration.getKeys(false);
    }

    @Override
    public void save(PermissionBase holder) throws DataSaveFailedException {
        holder.save();
        try {
            userYamlConfiguration.save(userFile);
        } catch (IOException ex) {
            throw new DataSaveFailedException(ex);
        }
        try {
            groupYamlConfiguration.save(groupFile);
        } catch (IOException ex) {
            throw new DataSaveFailedException(ex);
        }
        try {
            worldYamlConfiguration.save(worldFile);
        } catch (IOException ex) {
            throw new DataSaveFailedException(ex);
        }
        try {
            entityYamlConfiguration.save(entityFile);
        } catch (IOException ex) {
            throw new DataSaveFailedException(ex);
        }
        try {
            specialYamlConfiguration.save(specialFile);
        } catch (IOException ex) {
            throw new DataSaveFailedException(ex);
        }
    }
}
