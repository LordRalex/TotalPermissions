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

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class PermissionGroup {

    private final static CacheList<PermissionGroup> groupCache = new CacheList(PermissionGroup.class);
    private final String groupName;
    private final Map<String, Boolean> perms = new HashMap<String, Boolean>();
    private final List<PermissionGroup> inheritance = new ArrayList<PermissionGroup>();
    private final Map<String, Object> options = new HashMap<String, Object>();

    /**
     * Returns the PermissionGroup with that name. This can refer to the cache
     * if needed.
     *
     * @param name Name of the group.
     * @return The PermissionGroup to load
     *
     * @since 1.0
     */
    public static PermissionGroup loadGroup(String name) {
        PermissionGroup user = groupCache.get(name);
        if (user != null) {
            return user;
        }
        return new PermissionGroup(name);
    }

    /**
     * Creates a new PermissionGroup with the given name. This will load all the
     * values and then save it to the cache.
     *
     * @param name The name of the group
     *
     * @since 1.0
     */
    public PermissionGroup(String name) {
        groupName = name;
        ConfigurationSection groupSec = PermissionsAR.getPermFile().getConfigurationSection("groups." + groupName);
        List<String> permList = groupSec.getStringList("permissions");
        for (String perm : permList) {
            if (perm.startsWith("-")) {
                perms.put(perm, Boolean.FALSE);
            } else {
                perms.put(perm, Boolean.TRUE);
            }
        }
        List<String> inherList = groupSec.getStringList("inheritance");
        for (String tempName : inherList) {
            PermissionGroup tempGroup = loadGroup(tempName);
            List<String> tempGroupPerms = tempGroup.getPerms();
            for (String perm : tempGroupPerms) {
                if (perm.startsWith("-")) {
                    perms.put(perm, Boolean.FALSE);
                } else {
                    perms.put(perm, Boolean.TRUE);
                }
            }
            inheritance.add(tempGroup);
        }
        ConfigurationSection optionSec = groupSec.getConfigurationSection("options");
        Set<String> optionsList = optionSec.getKeys(true);
        for (String option : optionsList) {
            options.put(option, optionSec.get(option));
        }
        groupCache.remove(groupName);
        groupCache.add(this);
    }

    /**
     *
     * @param group
     *
     * @since 1.0
     */
    public void setPerms(String group) {
        if (perms == null) {
            List<String> permList = new PermissionGroup(group).getPerms();
            for (String perm : permList) {
                if (perm.startsWith("-")) {
                    perms.put(perm, Boolean.FALSE);
                } else {
                    perms.put(perm, Boolean.TRUE);
                }
            }
        }
    }

    /**
     *
     * @return
     *
     * @since 1.0
     */
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

    /**
     *
     * @param key
     * @return
     *
     * @since 1.0
     */
    public Object getOption(String key) {
        return options.get(key);
    }

    /**
     *
     * @return
     *
     * @since 1.0
     */
    public List<PermissionGroup> getInheritance() {
        return inheritance;
    }

    /**
     *
     * @return
     *
     * @since 1.0
     */
    public String getName() {
        return groupName;
    }

    /**
     *
     * @param group
     * @return
     *
     * @since 1.0
     */
    public boolean equals(PermissionGroup group) {
        if (group.getName().equalsIgnoreCase(this.groupName)) {
            return true;
        }
        return false;
    }
}
