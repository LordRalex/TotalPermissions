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
import org.bukkit.Bukkit;
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
    private PermissionAttachment attachment;

    public PermissionUser() {
        playerName = "";
    }

    /**
     * Returns the PermissionUser with that name. This can refer to the cache if
     * needed.
     *
     * @param name Name of the group.
     * @return The PermissionUser to load
     *
     * @since 1.0
     */
    public static PermissionUser loadUser(String name) {
        PermissionUser user = userCache.get(name);
        if (user != null) {
            return user;
        }
        return new PermissionUser(name);
    }

    /**
     * Creates a new PermissionUser with the given name. This will load all the
     * values and then save it to the cache.
     *
     * @param name The name of the user
     *
     * @since 1.0
     */
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

    /**
     * Adds the permissions for this PermissionUser to the given Player. If the
     * PermissionUser is not defined or has no permissions, this will create a
     * new instance of the PermissionUser and give those permissions to the
     * player
     *
     * @param player Player to add the permissions to
     *
     * @since 1.0
     */
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

    /**
     * Gets a list of the permissions for this user, including those that are
     * inherited. A '-' is added in front of negative nodes.
     *
     * @return List of permissions with - in front of negative nodes
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
     * Returns all the groups this user is in.
     *
     * @return Groups this user is in
     */
    public PermissionGroup[] getGroups() {
        return groups.toArray(new PermissionGroup[0]);
    }

    /**
     * Get the name of this user.
     *
     * @return Name of user
     *
     * @since 1.0
     */
    public String getName() {
        return playerName;
    }

    /**
     * Gets an option for the user. This is what is stored in the options:
     * section of the permissions in the user
     *
     * @param key Path to option
     * @return Value of that option, or null if no option
     *
     * @since 1.0
     */
    public Object getOption(String path) {
        return options.get(path);
    }

    /**
     * Compares the name of the parameter with that of the user. If they match,
     * this will return true.
     *
     * @param anotherUser Name of another user
     * @return True if names match, false otherwise
     *
     * @since 1.0
     */
    public boolean equals(PermissionUser anotherUser) {
        if (anotherUser.getName().equalsIgnoreCase(this.playerName)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the {@link org.bukkit.permissions.PermissionAttachment}
     * associated with this user.
     *
     * @return PermissionAttachment for user
     */
    public PermissionAttachment getAtt() {
        return attachment;
    }

    /**
     * Add a permission node to the user. This will also add the perm to the
     * player if they are online. This will apply for adding negative nodes too.
     *
     * @param perm Perm to add to this user
     */
    public void addPerm(String perm) {
        if (perm.startsWith("-")) {
            perm = perm.substring(1);
            perms.put(perm, Boolean.FALSE);
            Player player = Bukkit.getPlayerExact(playerName);
            if (player != null) {
                attachment.setPermission(perm, false);
            }
        } else {
            perms.put(perm, Boolean.TRUE);
            Player player = Bukkit.getPlayerExact(playerName);
            if (player != null) {
                attachment.setPermission(perm, false);
            }
        }
    }
}
