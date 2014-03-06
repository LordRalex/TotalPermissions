/*
 * Copyright 2011-2013 Tyler Blair. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */
package net.ae97.totalpermissions.mcstats;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import net.ae97.totalpermissions.TotalPermissions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Metrics {

    private final TotalPermissions plugin;
    private final String guid;
    private final URL url;
    private final int REVISION = 7;
    private final String USER_AGENT = "MCStats/" + REVISION;
    private final PostRunnable task = new PostRunnable();
    private final YamlConfiguration configuration = new YamlConfiguration();
    private final File configurationFile;

    public Metrics(TotalPermissions p) throws IOException {
        plugin = p;

        configurationFile = new File(new File(plugin.getDataFolder().getParentFile(), "PluginMetrics"), "config.yml");
        try {
            configuration.load(configurationFile);
        } catch (InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, "Metrics config cannot be loaded, going to continue with new config", e);
        }
        configuration.addDefault("opt-out", !plugin.getConfig().getBoolean("metrics-report", true));
        configuration.addDefault("guid", UUID.randomUUID().toString());
        configuration.addDefault("debug", false);

        if (configuration.get("guid", null) == null) {
            configuration.options().header("http://mcstats.org").copyDefaults(true);
            configuration.save(configurationFile);
        }

        guid = configuration.getString("guid");
        url = new URL("http://report.mcstats.org/plugin/" + URLEncoder.encode(plugin.getName(), "UTF-8"));
    }

    public boolean isOptOut() {
        if (!plugin.getConfig().getBoolean("metrics-report", true)) {
            return true;
        } else {
            return configuration.getBoolean("opt-out", false);
        }
    }

    public boolean start() {
        task.setMetrics(this);
        task.start();
        return true;
    }

    public void shutdown() throws InterruptedException {
        synchronized (task) {
            task.interrupt();
            task.join();
        }
    }

    protected void postPlugin(boolean isPing) throws IOException {

        StringBuilder json = new StringBuilder(1024);
        json.append('{');

        appendJSONPair(json, "guid", guid);
        appendJSONPair(json, "plugin_version", plugin.getDescription().getVersion());
        appendJSONPair(json, "server_version", Bukkit.getBukkitVersion());
        appendJSONPair(json, "players_online", Integer.toString(Bukkit.getServer().getOnlinePlayers().length));
        appendJSONPair(json, "auth_mode", Bukkit.getOnlineMode() ? "1" : "0");

        if (isPing) {
            appendJSONPair(json, "ping", "1");
        }

        json.append('}');

        byte[] compressed = gzip(json.toString());

        URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", USER_AGENT);
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("Content-Encoding", "gzip");
        connection.addRequestProperty("Content-Length", Integer.toString(compressed.length));
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.setDoOutput(true);

        OutputStream os = null;
        try {
            os = connection.getOutputStream();
            os.write(compressed);
            os.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (os != null) {
                os.close();
            }
        }

        BufferedReader reader = null;
        String response;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = reader.readLine();
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        if (response == null || response.startsWith("ERR") || response.startsWith("7")) {
            if (response == null) {
                response = "null";
            } else if (response.startsWith("7")) {
                response = response.substring(response.startsWith("7,") ? 2 : 1);
            }
            throw new IOException(response);
        }
    }

    private byte[] gzip(String input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = null;
        try {
            gzos = new GZIPOutputStream(baos);
            gzos.write(input.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new IOException("An error occurred on gzipping from Metrics", e);
        } finally {
            if (gzos != null) {
                try {
                    gzos.close();
                } catch (IOException ignore) {
                }
            }
        }
        return baos.toByteArray();
    }

    private void appendJSONPair(StringBuilder json, String key, String value) throws UnsupportedEncodingException {
        boolean isValueNumeric;
        try {
            Double.parseDouble(value);
            isValueNumeric = true;
        } catch (NumberFormatException e) {
            isValueNumeric = false;
        }
        if (json.charAt(json.length() - 1) != '{') {
            json.append(',');
        }
        json.append(escapeJSON(key));
        json.append(':');

        if (isValueNumeric) {
            json.append(value);
        } else {
            json.append(escapeJSON(value));
        }
    }

    private String escapeJSON(String text) {
        StringBuilder builder = new StringBuilder();
        builder.append('"');
        for (char chr : text.toCharArray()) {
            switch (chr) {
                case '"':
                case '\\':
                    builder.append('\\');
                    builder.append(chr);
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                default:
                    if (chr < ' ') {
                        String t = "000" + Integer.toHexString(chr);
                        builder.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        builder.append(chr);
                    }
                    break;
            }
        }
        builder.append('"');
        return builder.toString();
    }
}
