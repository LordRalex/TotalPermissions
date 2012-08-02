/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.permissionsar.permission;

import com.lordralex.permissionsar.PermissionsAR;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 *
 * @author Joshua
 */
public class PermissionUser {
    
    private final static CacheList<PermissionUser> userCache = new CacheList(PermissionUser.class);
    private final String playerName;
    private final Map<String, Boolean> perms = new HashMap<String, Boolean>();
    private final List<PermissionGroup> groups = new ArrayList<PermissionGroup>();
    private final Map<String, Object> options = new HashMap<String, Object>();
    private PermissionAttachment attachment;

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
        ConfigurationSection userSec = PermissionsAR.getPermFile().getConfigurationSection("users." + name);
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
            PermissionGroup group = PermissionsAR.getManager().getGroup(groupName);
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
        attachment = player.addAttachment(PermissionsAR.getManager().getPlugin());
        Set<Entry<String, Boolean>> entries = perms.entrySet();
        for (Entry entry : entries) {
            attachment.setPermission((String) entry.getKey(), ((Boolean) entry.getValue()).booleanValue());
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
    
    public PermissionAttachment getAtt()
    {
        return attachment;
    }        
}
