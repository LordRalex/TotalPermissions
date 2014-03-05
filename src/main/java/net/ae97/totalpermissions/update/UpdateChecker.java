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
package net.ae97.totalpermissions.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ae97.totalpermissions.TotalPermissions;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Lord_Ralex
 */
public class UpdateChecker implements Runnable {

    private final TotalPermissions plugin;
    private final boolean download;
    private final String apikey;
    private final boolean disabled;

    public UpdateChecker(TotalPermissions p, boolean d) {
        plugin = p;
        download = d;
        YamlConfiguration config = new YamlConfiguration();
        config.options().header("This configuration file affects all plugins using the Updater system (version 2+ - http://forums.bukkit.org/threads/96681/ )" + '\n'
                + "If you wish to use your API key, read http://wiki.bukkit.org/ServerMods_API and place it below." + '\n'
                + "Some updating systems will not adhere to the disabled value, but these may be turned off in their plugin's configuration.");
        config.addDefault("api-key", "PUT_API_KEY_HERE");
        config.addDefault("disable", false);

        File updaterFolder = new File(plugin.getDataFolder().getParentFile(), "Updater");
        File updaterConfigFile = new File(updaterFolder, "config.yml");

        updaterFolder.mkdirs();
        if (!updaterConfigFile.exists()) {
            config.options().copyDefaults(true);
            try {
                config.save(updaterConfigFile);
                config.load(updaterConfigFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Error saving default Updater config", e);
                apikey = null;
                disabled = true;
                return;
            } catch (InvalidConfigurationException e) {
                plugin.getLogger().log(Level.SEVERE, "Error loading default Updater config", e);
                apikey = null;
                disabled = true;
                return;
            }
        }
        apikey = config.getString("api-key", null);
        disabled = config.getBoolean("disable", false) || !plugin.getConfig().getBoolean("update.check", true);
    }

    @Override
    public void run() {
        if (disabled) {
            return;
        }
        String pluginVersion = plugin.getDescription().getVersion();
        try {
            URL url = new URL("https://api.curseforge.com/servermods/files?projectIds=54850");
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            if (apikey != null && !apikey.isEmpty() && !apikey.equals("PUT_API_KEY_HERE")) {
                conn.addRequestProperty("X-API-Key", apikey);
            }
            conn.addRequestProperty("User-Agent", "Updater - TotalPermissions-v" + pluginVersion);
            conn.setDoOutput(true);

            BufferedReader reader = null;
            JsonElement details = null;
            try {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String json = reader.readLine();
                JsonArray array = new JsonParser().parse(json).getAsJsonArray();
                details = array.get(array.size() - 1);
            } catch (IOException e) {
                if (e.getMessage().contains("HTTP response code: 403")) {
                    throw new IOException("CurseAPI rejected API-KEY", e);
                }
                throw e;
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            if (details == null) {
                return;
            }

            String onlineVersion = details.getAsJsonObject().get("name").getAsString();

            if (!checkForUpdate(pluginVersion, onlineVersion)) {
                return;
            }

            plugin.getLogger().log(Level.INFO, "Update found! Current: {0} Latest: {1}",
                    new String[]{
                        StringUtils.join(getVersionInts(pluginVersion), "."),
                        StringUtils.join(getVersionInts(onlineVersion), ".")
                    });

            if (!download) {
                return;
            }

            String downloadLink = details.getAsJsonObject().get("downloadUrl").getAsString();

            String pluginFileName = plugin.getFile().getName();

            if (!pluginFileName.equalsIgnoreCase(plugin.getName() + ".jar")) {
                plugin.getLogger().log(Level.WARNING, "FILE NAME IS NOT {0}.jar! Forcing rename", plugin.getName());
                plugin.getFile().deleteOnExit();
            }

            File output = new File(Bukkit.getUpdateFolderFile(), plugin.getName() + ".jar");
            download(downloadLink, output);

        } catch (MalformedURLException ex) {
            Logger.getLogger(UpdateChecker.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UpdateChecker.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkForUpdate(String current, String online) {
        if (current.equals(online)) {
            return false;
        }

        return checkUpdate(getVersionInts(current), getVersionInts(online));
    }

    private boolean checkUpdate(Integer[] current, Integer[] online) {
        if (current.length > online.length) {
            Integer[] newOnline = new Integer[current.length];
            System.arraycopy(online, 0, newOnline, 0, online.length);
            for (int i = online.length; i < newOnline.length; i++) {
                newOnline[i] = 0;
            }
            online = newOnline;
        } else if (online.length > current.length) {
            Integer[] newCurrent = new Integer[online.length];
            System.arraycopy(current, 0, newCurrent, 0, current.length);
            for (int i = current.length; i < newCurrent.length; i++) {
                newCurrent[i] = 0;
            }
            current = newCurrent;
        }
        for (int i = 0; i < current.length; i++) {
            if (online[i] > current[i]) {
                return true;
            }
        }
        return false;
    }

    private Integer[] getVersionInts(String versionString) {
        String bits;
        if (versionString.split("\\-v").length == 2) {
            bits = versionString.split("\\-v")[1];
        } else if (versionString.split(" v").length == 2) {
            bits = versionString.split(" v")[1];
        } else if (versionString.split(" ").length == 2) {
            bits = versionString.split(" ")[1];
        } else {
            bits = versionString;
        }

        Integer[] versionMapping = new Integer[bits.split("\\.").length];
        String[] nums = bits.split("\\.");
        for (int i = 0; i < nums.length; i++) {
            try {
                versionMapping[i] = Integer.parseInt(nums[i]);
            } catch (NumberFormatException e) {
                versionMapping[i] = 0;
            }
        }

        return versionMapping;
    }

    private boolean download(String link, File output) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            URL url = new URL(link);
            in = url.openStream();
            out = new FileOutputStream(output);
            byte[] buffer = new byte[1024];
            while (in.read(buffer) != -1) {
                out.write(buffer);
            }
            plugin.getLogger().info("Download complete, restart server to apply");
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
    }
}
