package com.lordralex.totalpermissions.permission;

import com.lordralex.totalpermissions.TotalPermissions;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public final class PermissionUser extends PermissionBase {

    private Player player = null;
    private PermissionAttachment attachment;
    private boolean isDebug;

    public PermissionUser(String aName) {
        super("users", aName);
        //checks to see if a player already on the server matches this,
        //if so, then set perms up now
        Player test = Bukkit.getPlayerExact(name);
        if (test != null) {
            player = test;
            setPerms(player);
        }
    }

    /**
     * Creates a new PermissionUser with the given name. This will load all the
     * values.
     *
     * @param name The name of the user
     *
     * @since 1.0
     */
    public PermissionUser(Player aPlayer) {
        this(aPlayer.getName());
        player = aPlayer;
        if (player != null) {
            setPerms(player);
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
    public void setPerms(Player aPlayer) {
        player = aPlayer;
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
        attachment = player.addAttachment(TotalPermissions.getPlugin());
        Set<Entry<String, Boolean>> entries = perms.entrySet();
        for (Entry entry : entries) {
            attachment.setPermission((String) entry.getKey(), ((Boolean) entry.getValue()).booleanValue());
        }
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

    public synchronized void update() {
        for (String perm : perms.keySet()) {
            attachment.setPermission(perm, perms.get(perm).booleanValue());
        }
    }

    public void setDebug(boolean newState) {
        isDebug = newState;
    }

    public boolean getDebugState() {
        return isDebug;
    }

    public void changeWorld(String name) {
        
    }
}
