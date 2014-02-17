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
package net.ae97.totalpermissions.listener;

import net.ae97.totalpermissions.TotalPermissions;
import org.bukkit.Bukkit;

/**
 * @author Lord_Ralex
 */
public final class ListenerManager {

    protected final TotalPermissions plugin;
    protected final PlayerListener playerListener;
    protected final WorldListener worldListener;
    protected final EntityListener entityListener;
    protected final ConsoleListener consoleListener;

    public ListenerManager(TotalPermissions p) {
        plugin = p;
        playerListener = new PlayerListener(plugin);
        worldListener = new WorldListener(plugin);
        entityListener = new EntityListener(plugin);
        consoleListener = new ConsoleListener(plugin);
    }

    public void load() {
        Bukkit.getPluginManager().registerEvents(playerListener, plugin);
        Bukkit.getPluginManager().registerEvents(entityListener, plugin);
        Bukkit.getPluginManager().registerEvents(worldListener, plugin);
        Bukkit.getPluginManager().registerEvents(consoleListener, plugin);
    }

    public PlayerListener getPlayerListener() {
        return playerListener;
    }

    public WorldListener getWorldListener() {
        return worldListener;
    }

    public EntityListener getEntityListener() {
        return entityListener;
    }

    public ConsoleListener getConsoleListener() {
        return consoleListener;
    }

}
