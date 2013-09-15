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

import java.io.BufferedInputStream;
import net.ae97.totalpermissions.TotalPermissions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @version 0.1
 * @author Lord_Ralex
 * @since 0.1
 */
public class UpdateRunnable extends BukkitRunnable {

    protected static final String VERSION_URL = "https://raw.github.com/AE97/TotalPermissions/master/VERSION";
    protected static final String DOWNLOAD_URL = "http://dev.bukkit.org/media/files/{id1}/{id2}/{filename}";
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
        BufferedReader reader = null;
        try {
            URL call = new URL(VERSION_URL);
            InputStream stream = call.openStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            String latest = reader.readLine();
            String latestBuild = reader.readLine();
            String[] dlParts = reader.readLine().split(" ");
            if (!latest.equalsIgnoreCase(version) || !build.equalsIgnoreCase(latestBuild)) {
                plugin.getLogger().info("[UPDATE] TotalPermissions has an update: " + latest + " (current: " + version + ")");
            }
            if (plugin.getConfig().getBoolean("update.download", true)) {
                downloadUpdate(dlParts);
            }
        } catch (MalformedURLException ex) {
            plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("update.update-error"), ex);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("update.update-error"), ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("update.update-error"), ex);
            }
        }
    }

    private void downloadUpdate(String[] params) {
        plugin.getLogger().info("[UPDATE] Downloading is enabled, downloading new update");
        BufferedInputStream in = null;
        FileOutputStream out = null;
        try {
            URL dlURL = new URL(DOWNLOAD_URL.replace("{id1}", params[0]).replace("{id2}", params[1]).replace("{filename}", params[2]));
            File destination = new File(Bukkit.getUpdateFolderFile(), "TotalPermissions.jar");
            if (!plugin.getFile().getName().equalsIgnoreCase("TotalPermissions.jar")) {
                plugin.getLogger().warning("The plugin jar is not named in a safe way, please name it TotalPermissions.jar");
            }
            destination.mkdirs();
            if (destination.exists()) {
                destination.delete();
            }
            out = new FileOutputStream(destination);
            in = new BufferedInputStream(dlURL.openStream());
            int fileLength = dlURL.openConnection().getContentLength();
            byte[] data = new byte[1024];
            int count;
            long downloaded = 0;
            while ((count = in.read(data, 0, 1024)) != -1) {
                downloaded += count;
                out.write(data, 0, count);
                int percent = (int) (downloaded * 100 / fileLength);
                if (percent % 10 == 0) {
                    plugin.getLogger().info("Downloading update: " + percent + "% of " + fileLength + " bytes.");
                }
            }
            plugin.getLogger().info("Download complete. Please restart your server to install");
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("update.download-error"), ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("update.download-error"), ex);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("update.download-error"), ex);
                }
            }
        }
    }
}
