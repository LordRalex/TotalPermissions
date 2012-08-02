package com.lordralex.permissionsar;

import java.util.*;
import java.util.Map.Entry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class PermissionUser {

    private final static CacheList<PermissionUser> userCache = new CacheList(PermissionUser.class);
    private final String playerName;
    private final Map<String, Boolean> perms = new HashMap<String, Boolean>();
    private final List<PermissionGroup> groups = new ArrayList<PermissionGroup>();
    private final Map<String, Object> options = new HashMap<String, Object>();

    /**
     * Gets a permission user
     *
     * @param name Name of player to get
     * @return The PermissionUser for that player
     */
    public static PermissionUser loadUser(String name) {
        PermissionUser user = userCache.get(name);
        if (user != null) {
            return user;
        }
        return new PermissionUser(name);
    }

    public PermissionUser(String name) {
        playerName = name;
        perms.clear();
        groups.clear();
        options.clear();
        ConfigurationSection userSec = PermissionsAR.permFile.getConfigurationSection("users." + name);
        List<String> permList = userSec.getStringList("permissions");
        for (String perm : permList) {
            if (perm.startsWith("-")) {
                perms.put(perm, Boolean.FALSE);
            } else {
                perms.put(perm, Boolean.TRUE);
            }
        }
        List<String> groupsList = userSec.getStringList("group");
        for (String groupName : groupsList) {
            PermissionGroup group = PermissionManager.getGroup(groupName);
            List<String> groupPerms = group.getPerms();
            for (String perm : groupPerms) {
                if (perm.startsWith("-")) {
                    perms.put(perm, Boolean.FALSE);
                } else {
                    perms.put(perm, Boolean.TRUE);
                }
            }
            groups.add(group);
        }
        ConfigurationSection optionSec = userSec.getConfigurationSection("options");
        Set<String> optionsList = optionSec.getKeys(true);
        for (String option : optionsList) {
            options.put(option, optionSec.get(option));
        }
        userCache.remove(playerName);
        userCache.add(this);
    }

    public void setPerms(Player player) {
        if (perms == null) {
            List<String> permList = new PermissionUser(player.getName()).getPerms();
            for (String perm : permList) {
                if (perm.startsWith("-")) {
                    perms.put(perm, Boolean.FALSE);
                } else {
                    perms.put(perm, Boolean.TRUE);
                }
            }
        }
        PermissionAttachment att = player.addAttachment(PermissionManager.getPlugin());
        Set<Entry<String, Boolean>> entries = perms.entrySet();
        for (Entry entry : entries) {
            att.setPermission((String) entry.getKey(), ((Boolean) entry.getValue()).booleanValue());
        }
    }

    public List<String> getPerms() {
        List<String> permList = new ArrayList<String>();
        Entry[] permKeys = perms.entrySet().toArray(new Entry[0]);
        for (Entry entry : permKeys) {
            String perm = (String) entry.getKey();
            if (!((Boolean) entry.getValue()).booleanValue()) {
                perm = "-" + perm;
            }
            permList.add(perm);
        }
        return permList;
    }

    public PermissionGroup[] getGroups() {
        return groups.toArray(new PermissionGroup[0]);
    }

    public String getName() {
        return playerName;
    }

    public Object getOption(String path) {
        return options.get(path);
    }

    public boolean equals(PermissionUser anotherUser) {
        if (anotherUser.getName().equalsIgnoreCase(this.playerName)) {
            return true;
        }
        return false;
    }
}
