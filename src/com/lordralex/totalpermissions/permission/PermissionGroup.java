package com.lordralex.totalpermissions.permission;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class PermissionGroup extends PermissionBase {

    private boolean isDefault = false;

    /**
     * Creates a new PermissionGroup with the given name. This will load all the
     * values.
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
    }

    public boolean isDefault() {
        return isDefault;
    }
}
