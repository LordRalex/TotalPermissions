/*
 * Updater for Bukkit.
 *
 * This class provides the means to safely and easily update a plugin,
 * or check to see if it is updated using dev.bukkit.org
 */
package net.ae97.totalpermissions.updater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class Updater {

    private final String DBOUrl = "http://dev.bukkit.org/bukkit-plugins/";
    private final int BYTE_SIZE = 1024;
    private final String TITLE = "title";
    private final String LINK = "link";
    private final String ITEM = "item";
    private final Plugin plugin;
    private final UpdateType type;
    private final boolean announce;
    private final URL url;
    private final File file;
    private final Thread thread;
    private final String[] noUpdateTag = {"-DEV", "-PRE", "-SNAPSHOT", "-BETA"};
    private final File updateFolder = Bukkit.getUpdateFolderFile();
    private String versionTitle;
    private String versionLink;
    private long totalSize;
    private int sizeLine;
    private int multiplier;
    private UpdateResult result = null;

    public Updater(Plugin plugin, String slug, File file, UpdateType type, boolean announce) {
        this.plugin = plugin;
        this.type = type;
        this.announce = announce;
        this.file = file;
        URL temp = null;
        try {
            temp = new URL(DBOUrl + slug + "/files.rss");
        } catch (MalformedURLException ex) {
            plugin.getLogger().log(Level.WARNING,
                    "The project slug given (''{0}'') is invalid.", slug);
            result = UpdateResult.FAIL_BADSLUG;
        }
        url = temp;
        if (result == null || result != UpdateResult.FAIL_BADSLUG) {
            thread = new Thread(new UpdateRunnable());
        } else {
            thread = null;
        }
    }

    public void checkForUpdate() {
        if (thread != null && !thread.isAlive()) {
            thread.start();
        }
    }

    public UpdateResult getResult() {
        waitForThread();
        return result;
    }

    public long getFileSize() {
        waitForThread();
        return totalSize;
    }

    public String getLatestVersionString() {
        waitForThread();
        return versionTitle;
    }

    public void waitForThread() {
        if (thread == null) {
            return;
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            plugin.getLogger().log(Level.SEVERE, "Unhandled exception", e);
        }

    }

    private void saveFile(File folder, String file, String u) {
        if (!folder.exists()) {
            folder.mkdir();
        }
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            URL link = new URL(u);
            int fileLength = link.openConnection().getContentLength();
            in = new BufferedInputStream(link.openStream());
            fout = new FileOutputStream(folder.getAbsolutePath() + "/" + file);

            byte[] data = new byte[BYTE_SIZE];
            int count;
            if (announce) {
                plugin.getLogger().log(Level.INFO,
                        "About to download a new update: {0}", versionTitle);
            }
            long downloaded = 0;
            while ((count = in.read(data, 0, BYTE_SIZE)) != -1) {
                downloaded += count;
                fout.write(data, 0, count);
                int percent = (int) (downloaded * 100 / fileLength);
                if (announce & (percent % 10 == 0)) {
                    plugin.getLogger().log(Level.INFO,
                            "Downloading update: {0}% of {1} bytes.", new Object[]{percent, fileLength});
                }
            }
            for (File xFile : updateFolder.listFiles()) {
                if (xFile.getName().endsWith(".zip")) {
                    xFile.delete();
                }
            }
            File dFile = new File(folder.getAbsolutePath() + "/" + file);
            if (dFile.getName().endsWith(".zip")) {
                unzip(dFile.getCanonicalPath());
            }
            if (announce) {
                plugin.getLogger().info("Finished updating.");
            }
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, "The auto-updater tried to download a new update, but was unsuccessful.", ex);
            result = UpdateResult.FAIL_DOWNLOAD;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private void unzip(String file) {
        try {
            File fSourceZip = new File(file);
            String zipPath = file.substring(0, file.length() - 4);
            ZipFile zipFile = new ZipFile(fSourceZip);
            Enumeration<? extends ZipEntry> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                File destinationFilePath = new File(zipPath, entry.getName());
                destinationFilePath.getParentFile().mkdirs();
                if (!entry.isDirectory()) {
                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                    int b;
                    byte buffer[] = new byte[BYTE_SIZE];
                    FileOutputStream fos = new FileOutputStream(destinationFilePath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, BYTE_SIZE);
                    while ((b = bis.read(buffer, 0, BYTE_SIZE)) != -1) {
                        bos.write(buffer, 0, b);
                    }
                    bos.flush();
                    bos.close();
                    bis.close();
                    String name = destinationFilePath.getName();
                    if (name.endsWith(".jar") && pluginFile(name)) {
                        destinationFilePath.renameTo(new File(updateFolder, name));
                    }
                }
            }
            zipFile.close();
            for (File dFile : new File(zipPath).listFiles()) {
                if (dFile.isDirectory()) {
                    if (pluginFile(dFile.getName())) {
                        File oFile = new File("plugins/" + dFile.getName());
                        File[] contents = oFile.listFiles();
                        for (File cFile : dFile.listFiles()) {
                            boolean found = false;
                            for (File xFile : contents) {
                                if (xFile.getName().equals(cFile.getName())) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                cFile.renameTo(new File(oFile.getCanonicalFile() + "/" + cFile.getName()));
                            } else {
                                cFile.delete();
                            }
                        }
                    }
                }
                dFile.delete();
            }
            new File(zipPath).delete();
            fSourceZip.delete();
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, "The auto-updater tried to unzip a new update file, but was unsuccessful.", ex);
            result = UpdateResult.FAIL_DOWNLOAD;
        }
        new File(file).delete();
    }

    public boolean pluginFile(String name) {
        for (File f : plugin.getDataFolder().getParentFile().listFiles()) {
            if (f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private String getFile(String link) {
        String download = null;
        BufferedReader buff = null;
        try {
            URL u = new URL(link);
            URLConnection urlConn = u.openConnection();
            InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
            buff = new BufferedReader(inStream);

            int counter = 0;
            String line;
            while ((line = buff.readLine()) != null) {
                counter++;
                if (line.contains("<li class=\"user-action user-action-download\">")) {
                    download = line.split("<a href=\"")[1].split("\">Download</a>")[0];
                } else if (line.contains("<dt>Size</dt>")) {
                    sizeLine = counter + 1;
                } else if (counter == sizeLine) {
                    String size = line.replaceAll("<dd>", "").replaceAll("</dd>", "");
                    multiplier = size.contains("MiB") ? 1048576 : 1024;
                    size = size.replace(" KiB", "").replace(" MiB", "");
                    totalSize = (long) (Double.parseDouble(size) * multiplier);
                }
            }
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, "The auto-updater tried to contact dev.bukkit.org, but was unsuccessful.", ex);
            result = UpdateResult.FAIL_DBO;
            return null;
        } catch (NumberFormatException ex) {
            plugin.getLogger().log(Level.WARNING, "The auto-updater tried to contact dev.bukkit.org, but was unsuccessful.", ex);
            result = UpdateResult.FAIL_DBO;
            return null;
        } finally {
            try {
                if (buff != null) {
                    buff.close();
                }
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not close buffer", ex);
            }
        }
        return download;
    }

    private boolean versionCheck(String title) {
        if (type != UpdateType.NO_VERSION_CHECK) {
            String version = plugin.getDescription().getVersion();
            if (title.split(" v").length == 2) {
                String remoteVersion = title.split(" v")[1].split(" ")[0];
                int remVer, curVer = 0;
                try {
                    remVer = calVer(remoteVersion);
                    curVer = calVer(version);
                } catch (NumberFormatException nfe) {
                    remVer = -1;
                }
                if (hasTag(version) || version.equalsIgnoreCase(remoteVersion) || curVer >= remVer) {
                    result = UpdateResult.NO_UPDATE;
                    return false;
                }
            } else {
                plugin.getLogger().warning("Files uploaded to BukkitDev should contain the version number, "
                        + "seperated from the name by a 'v', such as PluginName v1.0");
                plugin.getLogger().warning("Please notify the author of this error.");
                result = UpdateResult.FAIL_NOVERSION;
                return false;
            }
        }
        return true;
    }

    private Integer calVer(String s) throws NumberFormatException {
        if (s.contains(".")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                Character c = s.charAt(i);
                if (Character.isLetterOrDigit(c)) {
                    sb.append(c);
                }
            }
            return Integer.parseInt(sb.toString());
        }
        return Integer.parseInt(s);
    }

    private boolean hasTag(String version) {
        for (String string : noUpdateTag) {
            if (version.contains(string)) {
                return true;
            }
        }
        return false;
    }

    private boolean readFeed() {
        try {
            String title = "";
            String link = "";
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = read();
            if (in != null) {
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
                while (eventReader.hasNext()) {
                    XMLEvent event = eventReader.nextEvent();
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart().equals(TITLE)) {
                            event = eventReader.nextEvent();
                            title = event.asCharacters().getData();
                        } else if (event.asStartElement().getName().getLocalPart().equals(LINK)) {
                            event = eventReader.nextEvent();
                            link = event.asCharacters().getData();
                        }
                    } else if (event.isEndElement()) {
                        if (event.asEndElement().getName().getLocalPart().equals(ITEM)) {
                            versionTitle = title;
                            versionLink = link;
                            break;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (XMLStreamException e) {
            plugin.getLogger().warning("Could not reach dev.bukkit.org for update checking. Is it offline?");
            return false;
        }
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Could not reach BukkitDev file stream for update checking. Is dev.bukkit.org offline?", e);
            return null;
        }
    }

    private class UpdateRunnable implements Runnable {

        @Override
        public void run() {
            if (url != null) {
                if (readFeed()) {
                    if (versionCheck(versionTitle)) {
                        String fileLink = getFile(versionLink);
                        if (fileLink != null && type != UpdateType.NO_DOWNLOAD) {
                            String name = file.getName();
                            if (fileLink.endsWith(".zip")) {
                                String[] split = fileLink.split("/");
                                name = split[split.length - 1];
                            }
                            saveFile(updateFolder, name, fileLink);
                        } else {
                            result = UpdateResult.UPDATE_AVAILABLE;
                        }
                    }
                }
            }
        }
    }
}
