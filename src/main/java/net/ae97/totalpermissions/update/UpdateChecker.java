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
import net.ae97.totalpermissions.TotalPermissions;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

/**
 * @author Lord_Ralex
 */
public class UpdateChecker implements Runnable {

    private final TotalPermissions plugin;
    private final boolean download;

    public UpdateChecker(TotalPermissions p, boolean d) {
        plugin = p;
        download = d;
    }

    @Override
    public void run() {
        String pluginName = plugin.getDescription().getName();
        String pluginVersion = plugin.getDescription().getVersion();
        try {
            URL rss = new URL("http://dev.bukkit.org/bukkit-plugins/totalpermissions/files.rss");
            String pageLink = null;
            String onlineVersion = pluginName + "-v" + pluginVersion;
            BufferedReader reader = null;
            String line;
            try {
                reader = new BufferedReader(new InputStreamReader(rss.openStream()));
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith("<item>")) {
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (line.startsWith("<title>")) {
                                onlineVersion = line.substring("<title>".length(), line.length() - "</title>".length() - 1);
                            }
                            if (line.startsWith("<link>")) {
                                pageLink = line.substring("<link>".length(), line.length() - "</link>".length() - 1);
                                break;
                            }
                        }
                        break;
                    }
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
            if (pageLink == null) {
                return;
            }
            String latest = checkForUpdate(pluginVersion, onlineVersion);
            if (latest == null || latest.equals(plugin.getDescription().getVersion())) {
                return;
            }

            plugin.getLogger().log(Level.INFO, "Update found! Current: {0} Latest: {1}",
                    new String[]{plugin.getDescription().getVersion(), latest});

            if (!download) {
                return;
            }

            URL filePage = new URL(pageLink);
            reader = null;
            String downloadLink = null;
            try {
                reader = new BufferedReader(new InputStreamReader(filePage.openStream()));
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("<li class=\"user-action user-action-download\">")) {
                        downloadLink = line.substring("<li class=\"user-action user-action-download\"><span><a href=\"".length(),
                                "\">Download</a></span></li>".length() - 1);
                        break;
                    }
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }

            String pluginFileName = plugin.getFile().getName();

            if (!pluginFileName.equalsIgnoreCase(plugin.getName() + ".jar")) {
                plugin.getLogger().log(Level.WARNING, "FILE NAME IS NOT {0}.jar! Forcing rename", plugin.getName());
                plugin.getFile().deleteOnExit();
            }

            File output = new File(Bukkit.getUpdateFolderFile(), plugin.getName() + ".jar");
            download(downloadLink, output);
        } catch (MalformedURLException ex) {
            Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String checkForUpdate(String current, String online) {
        if (current.equals(online)) {
            return current;
        }

        return StringUtils.join(checkUpdate(getVersionInts(current), getVersionInts(online)), ".");
    }

    private Integer[] checkUpdate(Integer[] current, Integer[] online) {
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
                return online;
            }
        }
        return current;
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
