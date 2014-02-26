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
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import net.ae97.totalpermissions.TotalPermissions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

public final class Metrics {

    private volatile BukkitTask task = null;
    private final int REVISION = 7;
    private final String BASE_URL = "http://report.mcstats.org";
    private final String REPORT_URL = "/plugin/%s";
    private final int PING_INTERVAL = 15;
    private final TotalPermissions plugin;
    private final Set<Graph> graphs = Collections.synchronizedSet(new HashSet<Graph>());
    private final YamlConfiguration configuration;
    private final File configurationFile;
    private final String guid;
    private final Object optOutLock = new Object();

    public Metrics(TotalPermissions p) throws IOException {
        if (p == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        plugin = p;

        configurationFile = getConfigFile();
        configuration = YamlConfiguration.loadConfiguration(configurationFile);
        configuration.addDefault("opt-out", !plugin.getConfig().getBoolean("metrics-report", true));
        configuration.addDefault("guid", UUID.randomUUID().toString());
        configuration.addDefault("debug", false);

        if (configuration.get("guid", null) == null) {
            configuration.options().header("http://mcstats.org").copyDefaults(true);
            configuration.save(configurationFile);
        }

        guid = configuration.getString("guid");
    }

    /**
     * Start measuring statistics. This will immediately create an async
     * repeating task as the plugin and send the initial data to the metrics
     * backend, and then after that it will post in increments of PING_INTERVAL
     * * 1200 ticks.
     *
     * @return True if statistics measuring is running, otherwise false.
     */
    public boolean start() {
        synchronized (optOutLock) {
            if (isOptOut()) {
                return false;
            }

            if (task != null) {
                return true;
            }

            task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
                private boolean firstPost = true;

                @Override
                public void run() {
                    try {
                        synchronized (optOutLock) {
                            if (isOptOut() && task != null) {
                                task.cancel();
                                task = null;
                                for (Graph graph : graphs) {
                                    graph.onOptOut();
                                }
                            }
                        }

                        postPlugin(!firstPost);

                        firstPost = false;
                    } catch (IOException e) {
                    }
                }
            }, 0, PING_INTERVAL * 1200);

            return true;
        }
    }

    /**
     * GZip compress a string of bytes
     *
     * @param input
     * @return
     */
    private byte[] gzip(String input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = null;

        try {
            gzos = new GZIPOutputStream(baos);
            gzos.write(input.getBytes("UTF-8"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred on gzipping from Metrics", e);
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

    /**
     * Appends a json encoded key/value pair to the given string builder.
     *
     * @param json
     * @param key
     * @param value
     * @throws UnsupportedEncodingException
     */
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

    /**
     * Escape a string to create a valid JSON string
     *
     * @param text
     * @return
     */
    private String escapeJSON(String text) {
        StringBuilder builder = new StringBuilder();

        builder.append('"');
        for (int index = 0; index < text.length(); index++) {
            char chr = text.charAt(index);

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

    /**
     * Encode text as UTF-8
     *
     * @param text the text to encode
     * @return the encoded text, as UTF-8
     */
    private String urlEncode(String text) throws UnsupportedEncodingException {
        return URLEncoder.encode(text, "UTF-8");
    }

    /**
     * Has the server owner denied plugin metrics?
     *
     * @return true if metrics should be opted out of it
     */
    private boolean isOptOut() {
        synchronized (optOutLock) {
            try {
                configuration.load(getConfigFile());
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "[Metrics] ", ex.getMessage());
                return true;
            } catch (InvalidConfigurationException ex) {
                plugin.getLogger().log(Level.SEVERE, "[Metrics] ", ex.getMessage());
                return true;
            }
            if (!plugin.getConfig().getBoolean("metrics-report", true)) {
                return true;
            } else {
                return configuration.getBoolean("opt-out", false);
            }
        }
    }

    /**
     * Gets the File object of the config file that should be used to store data
     * such as the GUID and opt-out status
     *
     * @return the File object for the config file
     */
    private File getConfigFile() {
        return new File(new File(plugin.getDataFolder().getParentFile(), "PluginMetrics"), "config.yml");
    }

    /**
     * Generic method that posts a plugin to the metrics website
     */
    private void postPlugin(boolean isPing) throws IOException {
        int playersOnline = Bukkit.getServer().getOnlinePlayers().length;

        StringBuilder json = new StringBuilder(1024);
        json.append('{');

        appendJSONPair(json, "guid", guid);
        appendJSONPair(json, "plugin_version", plugin.getDescription().getVersion());
        appendJSONPair(json, "server_version", Bukkit.getBukkitVersion());
        appendJSONPair(json, "players_online", Integer.toString(playersOnline));

        appendJSONPair(json, "auth_mode", Bukkit.getOnlineMode() ? "1" : "0");

        if (isPing) {
            appendJSONPair(json, "ping", "1");
        }

        if (graphs.size() > 0) {
            synchronized (graphs) {
                json.append(',');
                json.append('"');
                json.append("graphs");
                json.append('"');
                json.append(':');
                json.append('{');

                boolean firstGraph = true;

                final Iterator<Graph> iter = graphs.iterator();

                while (iter.hasNext()) {
                    Graph graph = iter.next();

                    StringBuilder graphJson = new StringBuilder();
                    graphJson.append('{');

                    for (Plotter plotter : graph.getPlotters()) {
                        appendJSONPair(graphJson, plotter.getColumnName(), Integer.toString(plotter.getValue()));
                    }

                    graphJson.append('}');

                    if (!firstGraph) {
                        json.append(',');
                    }

                    json.append(escapeJSON(graph.getName()));
                    json.append(':');
                    json.append(graphJson);

                    firstGraph = false;
                }

                json.append('}');
            }
        }

        json.append('}');

        URL url = new URL(BASE_URL + String.format(REPORT_URL, urlEncode(plugin.getName())));

        URLConnection connection;

        if (isMineshafterPresent()) {
            connection = url.openConnection(Proxy.NO_PROXY);
        } else {
            connection = url.openConnection();
        }

        byte[] compressed = gzip(json.toString());

        connection.addRequestProperty("User-Agent", "MCStats/" + REVISION);
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
        } else {
            if (response.equals("1") || response.contains("This is your first update this hour")) {
                synchronized (graphs) {
                    final Iterator<Graph> iter = graphs.iterator();

                    while (iter.hasNext()) {
                        final Graph graph = iter.next();

                        for (Plotter plotter : graph.getPlotters()) {
                            plotter.reset();
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if mineshafter is present. If it is, we need to bypass it to send
     * POST requests
     *
     * @return true if mineshafter is installed on the server
     */
    private boolean isMineshafterPresent() {
        try {
            Class.forName("mineshafter.MineServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
