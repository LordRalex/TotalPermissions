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
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class PermissionUser {

    private final String playerName;
    private final Map<String, Boolean> perms = new HashMap<String, Boolean>();
    private final List<PermissionGroup> groups = new ArrayList<PermissionGroup>();
    private final Map<String, Object> options = new HashMap<String, Object>();
    private PermissionAttachment attachment;

    public PermissionUser() {
        playerName = "";
    }

    /**
     * Creates a new PermissionUser with the given name. This will load all the
     * values.
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
            if (perm.equals("**")) {
                Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
                for (Permission permTest : permT) {
                    if (!perms.containsKey(permTest.getName())) {
                        perms.put(permTest.getName(), Boolean.TRUE);
                    }
                }
            } else if (perm.equals("*")) {
                Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
                for (Permission permTest : permT) {
                    if (permTest.getDefault() == PermissionDefault.OP || permTest.getDefault() == PermissionDefault.TRUE) {
                        if (!perms.containsKey(permTest.getName())) {
                            perms.put(permTest.getName(), Boolean.TRUE);
                        }
                    }
                }
            } else if (perm.equals("-*")) {
                Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
                for (Permission permTest : permT) {
                    if (permTest.getDefault() == PermissionDefault.OP || permTest.getDefault() == PermissionDefault.TRUE) {
                        perms.put(perm, Boolean.FALSE);
                    }
                }
            } else if (perm.startsWith("-")) {
                if (!perms.containsKey(perm)) {
                    perms.put(perm, Boolean.FALSE);
                }
            } else {
                if (!perms.containsKey(perm)) {
                    perms.put(perm, Boolean.TRUE);
                }
            }
        }
        List<String> groupsList = userSec.getStringList("group");
        for (String groupName : groupsList) {
            PermissionGroup group = PermissionsAR.getManager().getGroup(groupName);
            List<String> groupPerms = group.getPerms();
            for (String perm : groupPerms) {
                if (perm.equals("**")) {
                    Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
                    for (Permission permTest : permT) {
                        if (!perms.containsKey(permTest.getName())) {
                            perms.put(permTest.getName(), Boolean.TRUE);
                        }
                    }
                } else if (perm.equals("*")) {
                    Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
                    for (Permission permTest : permT) {
                        if (permTest.getDefault() == PermissionDefault.OP || permTest.getDefault() == PermissionDefault.TRUE) {
                            if (!perms.containsKey(permTest.getName())) {
                                perms.put(permTest.getName(), Boolean.TRUE);
                            }
                        }
                    }
                } else if (perm.equals("-*")) {
                    Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
                    for (Permission permTest : permT) {
                        if (permTest.getDefault() == PermissionDefault.OP || permTest.getDefault() == PermissionDefault.TRUE) {
                            perms.put(perm, Boolean.FALSE);
                        }
                    }
                } else if (perm.startsWith("-")) {
                    if (!perms.containsKey(perm)) {
                        perms.put(perm, Boolean.FALSE);
                    }
                } else {
                    if (!perms.containsKey(perm)) {
                        perms.put(perm, Boolean.TRUE);
                    }
                }
            }
            groups.add(group);
        }
        ConfigurationSection optionSec = userSec.getConfigurationSection("options");
        Set<String> optionsList = optionSec.getKeys(true);
        for (String option : optionsList) {
            options.put(option, optionSec.get(option));
        }
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
        attachment = player.addAttachment(PermissionsAR.getPlugin());
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
    public synchronized List<String> getPerms() {
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
     * Add a permission node to the user. This will apply for adding negative
     * nodes too.
     *
     * @param perm Perm to add to this user
     */
    public synchronized void addPerm(String perm) {
        if (perm.equals("**")) {
            Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
            for (Permission permTest : permT) {
                perms.remove(permTest.getName());
                perms.put(permTest.getName(), Boolean.TRUE);
            }
        } else if (perm.equals("*")) {
            Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
            for (Permission permTest : permT) {
                if (permTest.getDefault() == PermissionDefault.OP || permTest.getDefault() == PermissionDefault.TRUE) {
                    perms.remove(permTest.getName());
                    perms.put(permTest.getName(), Boolean.TRUE);
                }
            }
        } else if (perm.equals("-*")) {
            Set<Permission> permT = Bukkit.getPluginManager().getPermissions();
            for (Permission permTest : permT) {
                if (permTest.getDefault() == PermissionDefault.OP || permTest.getDefault() == PermissionDefault.TRUE) {
                    perms.remove(permTest.getName());
                    perms.put(permTest.getName(), Boolean.FALSE);
                }
            }
        } else if (perm.startsWith("-")) {
            perms.remove(perm);
            perms.put(perm, Boolean.TRUE);
        } else {
            perms.remove(perm);
            perms.put(perm, Boolean.TRUE);
        }
    }

    public synchronized boolean has(String perm) {
        Boolean result = perms.get(perm);
        if (result != null && result.booleanValue()) {
            return true;
        }
        return false;
    }
}
