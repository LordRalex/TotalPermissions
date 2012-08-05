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
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class PermissionGroup {

    private final String groupName;
    private final Map<String, Boolean> perms = new HashMap<String, Boolean>();
    private final List<PermissionGroup> inheritance = new ArrayList<PermissionGroup>();
    private final Map<String, Object> options = new HashMap<String, Object>();

    public PermissionGroup() {
        groupName = "";
    }

    /**
     * Creates a new PermissionGroup with the given name. This will load all the
     * values.
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
        List<String> inherList = groupSec.getStringList("inheritance");
        for (String tempName : inherList) {
            PermissionGroup tempGroup = PermissionsAR.getManager().getGroup(tempName);
            List<String> tempGroupPerms = tempGroup.getPerms();
            for (String perm : tempGroupPerms) {
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
            inheritance.add(tempGroup);
        }
        ConfigurationSection optionSec = groupSec.getConfigurationSection("options");
        Set<String> optionsList = optionSec.getKeys(true);
        for (String option : optionsList) {
            options.put(option, optionSec.get(option));
        }
    }

    /**
     * Gets a list of the permissions for this group, including those that are
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
     * Gets an option for the group. This is what is stored in the options:
     * section of the permissions in the groups
     *
     * @param key Path to option
     * @return Value of that option, or null if no option
     *
     * @since 1.0
     */
    public Object getOption(String key) {
        return options.get(key);
    }

    /**
     * Gets this group's inherited groups.
     *
     * @return List of groups this inherits from
     *
     * @since 1.0
     */
    public List<PermissionGroup> getInheritance() {
        return inheritance;
    }

    /**
     * Get the name of this group.
     *
     * @return Name of group
     *
     * @since 1.0
     */
    public String getName() {
        return groupName;
    }

    /**
     * Compares the name of the parameter with that of the group. If they match,
     * this will return true.
     *
     * @param group Name of another group
     * @return True if names match, false otherwise
     *
     * @since 1.0
     */
    public boolean equals(PermissionGroup group) {
        if (group.getName().equalsIgnoreCase(this.groupName)) {
            return true;
        }
        return false;
    }

    /**
     * Add a permission node to the group. This will apply for adding negative
     * nodes too.
     *
     * @param perm Perm to add to this group
     */
    public void addPerm(String perm) {
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
}
