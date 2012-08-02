package com.lordralex.permissionsar.permission;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joshua
 */
public class CacheList<T> extends ArrayList {

    Class stored;

    public CacheList(Class something) {
        stored = something;
    }

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

    public boolean remove(String name) {
        Object temp = this.get(name);
        if (temp == null) {
            return false;
        }
        return this.remove(temp);
    }
}
