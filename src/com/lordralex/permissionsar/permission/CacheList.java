package com.lordralex.permissionsar.permission;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class CacheList<T> extends ArrayList {

    Class stored;

    /**
     * Creates the cache for a particular class.
     *
     * @param something The Class this list represents
     *
     * @since 1.0
     */
    public CacheList(Class something) {
        stored = something;
    }

    /**
     *
     * @param name The name of the object to get
     * @return The object this matches
     *
     * @since 1.0
     */
    public T get(String name) {
        try {
            if (stored.newInstance() instanceof PermissionUser) {
                for (Object user : this.toArray(new PermissionUser[0])) {
                    PermissionUser test = (PermissionUser) user;
                    if (test.getName().equalsIgnoreCase(name)) {
                        return (T) user;
                    }
                }
            }
            if (stored.newInstance() instanceof PermissionGroup) {
                for (Object user : this.toArray(new PermissionUser[0])) {
                    PermissionGroup test = (PermissionGroup) user;
                    if (test.getName().equalsIgnoreCase(name)) {
                        return (T) user;
                    }
                }
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(CacheList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CacheList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Removes an object with the given name. Returns the result of the action.
     *
     * @param name Name to remove
     * @return True if something was removed, false otherwise
     * 
     * @since 1.0
     */
    public boolean remove(String name) {
        Object temp = this.get(name);
        if (temp == null) {
            return false;
        }
        return this.remove(temp);
    }
}
