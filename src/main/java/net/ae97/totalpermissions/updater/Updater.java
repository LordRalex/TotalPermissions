/*
 * Updater for Bukkit.
 *
 * This class provides the means to safely and easily update a plugin, or check to see if it is updated using dev.bukkit.org
 */
package net.ae97.totalpermissions.updater;

import java.io.*;
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

public class Updater {

    private final Plugin plugin;
    private final UpdateType type;
    private String versionTitle;
    private String versionLink;
    private long totalSize;
    private int sizeLine;
    private int multiplier;
    private final boolean announce;
    private URL url;
    private final File file;
    private final Thread thread;
    private static final String DBOUrl = "http://dev.bukkit.org/server-mods/";
    private final String[] noUpdateTag = {"-DEV", "-PRE", "-SNAPSHOT"};
    private static final int BYTE_SIZE = 1024;
    private final File updateFolder = Bukkit.getUpdateFolderFile();
    private Updater.UpdateResult result = Updater.UpdateResult.SUCCESS;
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String ITEM = "item";

    public enum UpdateResult {

        SUCCESS,
        NO_UPDATE,
        FAIL_DOWNLOAD,
        FAIL_DBO,
        FAIL_NOVERSION,
        FAIL_BADSLUG,
        UPDATE_AVAILABLE
    }

    public enum UpdateType {

        DEFAULT,
        NO_VERSION_CHECK,
        NO_DOWNLOAD
    }

    public Updater(Plugin plugin, String slug, File file, UpdateType type, boolean announce) {
        this.plugin = plugin;
        this.type = type;
        this.announce = announce;
        this.file = file;
        try {
            url = new URL(DBOUrl + slug + "/files.rss");
        } catch (MalformedURLException ex) {
            plugin.getLogger().warning("The author of this plugin (" + plugin.getDescription().getAuthors().get(0) + ") has misconfigured their Auto Update system");
            plugin.getLogger().warning("The project slug given ('" + slug + "') is invalid. Please nag the author about this.");
            result = Updater.UpdateResult.FAIL_BADSLUG;
        }
        thread = new Thread(new UpdateRunnable());

    }

    public void checkForUpdate() {
        thread.start();
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
        if (thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                plugin.getLogger().log(Level.SEVERE, "Unhandled exception", e);
            }
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
                plugin.getLogger().info("About to download a new update: " + versionTitle);
            }
            long downloaded = 0;
            while ((count = in.read(data, 0, BYTE_SIZE)) != -1) {
                downloaded += count;
                fout.write(data, 0, count);
                int percent = (int) (downloaded * 100 / fileLength);
                if (announce & (percent % 10 == 0)) {
                    plugin.getLogger().info("Downloading update: " + percent + "% of " + fileLength + " bytes.");
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
            result = Updater.UpdateResult.FAIL_DOWNLOAD;
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
            result = Updater.UpdateResult.FAIL_DOWNLOAD;
        }
        new File(file).delete();
    }

    public boolean pluginFile(String name) {
        for (File f : new File("plugins").listFiles()) {
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
            result = Updater.UpdateResult.FAIL_DBO;
            return null;
        } catch (NumberFormatException ex) {
            plugin.getLogger().log(Level.WARNING, "The auto-updater tried to contact dev.bukkit.org, but was unsuccessful.", ex);
            result = Updater.UpdateResult.FAIL_DBO;
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
                int remVer = -1, curVer = 0;
                try {
                    remVer = calVer(remoteVersion);
                    curVer = calVer(version);
                } catch (NumberFormatException nfe) {
                    remVer = -1;
                }
                if (hasTag(version) || version.equalsIgnoreCase(remoteVersion) || curVer >= remVer) {
                    result = Updater.UpdateResult.NO_UPDATE;
                    return false;
                }
            } else {
                plugin.getLogger().warning("The author of this plugin (" + plugin.getDescription().getAuthors().get(0) + ") has misconfigured their Auto Update system");
                plugin.getLogger().warning("Files uploaded to BukkitDev should contain the version number, seperated from the name by a 'v', such as PluginName v1.0");
                plugin.getLogger().warning("Please notify the author of this error.");
                result = Updater.UpdateResult.FAIL_NOVERSION;
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
                            saveFile(new File("plugins/" + updateFolder), name, fileLink);
                        } else {
                            result = UpdateResult.UPDATE_AVAILABLE;
                        }
                    }
                }
            }
        }
    }
}
