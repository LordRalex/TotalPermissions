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
package net.ae97.totalpermissions.runnable;

import net.ae97.totalpermissions.TotalPermissions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 */
public class UpdateRunnable extends BukkitRunnable {

    protected static final String VERSION_URL = "https://raw.github.com/AE97/TotalPermissions/master/VERSION";
    protected final String version;
    protected final TotalPermissions plugin;
    protected final String build;

    public UpdateRunnable(TotalPermissions p) {
        super();
        plugin = p;
        version = plugin.getDescription().getVersion();
        YamlConfiguration pluginYml = YamlConfiguration.loadConfiguration(plugin.getResource("plugin.yml"));
        build = pluginYml.getString("build");
    }

    @Override
    public void run() {
        if (version.endsWith("SNAPSHOT") || version.endsWith("DEV") || build.equalsIgnoreCase("${build}")) {
            plugin.getLogger().warning(plugin.getLangFile().getString("update.dev"));
            return;
        }
        try {
            URL call = new URL(VERSION_URL);
            InputStream stream = call.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String latest = reader.readLine();
            String latestBuild = reader.readLine();
            reader.close();
            if (!latest.equalsIgnoreCase(version) || !build.equalsIgnoreCase(latestBuild)) {
                plugin.getLogger().info("[UPDATE] TotalPermissions has an update: " + latest + " (current: " + version + ")");
            }
        } catch (MalformedURLException ex) {
            plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("update.update-error"), ex);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("update.update-error"), ex);
        }
    }
}
