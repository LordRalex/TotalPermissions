package com.lordralex.permissionsar;

import java.util.Map.Entry;
import java.util.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 * @version 1.0
 * @author Joshua
 */
public class PermissionUser {

    private String playerName;
    private Map<String, Boolean> perms = new HashMap<String, Boolean>();
    private List<PermissionGroup> groups = new ArrayList<PermissionGroup>();

    public static PermissionUser loadUser(String name) {
        return new PermissionUser(name);
    }

    public PermissionUser(String name) {
        playerName = name;
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

    public PermissionGroup[] getGroups()
    {
        return groups.toArray(new PermissionGroup[0]);
    }

    public String getName()
    {
        return playerName;
    }
}
