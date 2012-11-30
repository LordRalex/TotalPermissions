package com.lordralex.totalpermissions.permission;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class PermissionGroup extends PermissionBase {

    protected boolean isDefault = false;

    /**
     * Creates a new PermissionGroup with the given name.
     *
     * @param name The name of the group
     *
     * @since 1.0
     */
    public PermissionGroup(String name) {
        super("group", name);
        if (name.equalsIgnoreCase("default")) {
            isDefault = true;
        }
        if (!isDefault) {
            if (section != null) {
                if (section.getBoolean("default", false)) {
                    isDefault = true;
                }
            }
        }
    }

    /**
     * Checks to see if this group is the default. If so, this returns true,
     * otherwise false.
     *
     * @return True if this group is the default group, otherwise false
     */
    public boolean isDefault() {
        return isDefault;
    }
}
