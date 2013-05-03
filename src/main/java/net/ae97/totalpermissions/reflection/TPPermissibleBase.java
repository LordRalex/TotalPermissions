/*
 * Copyright (C) 2013 AE97
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.ae97.totalpermissions.reflection;

import net.ae97.totalpermissions.TotalPermissions;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

/**
 * @author Lord_Ralex
 * @version 0.2
 * @since 0.1
 */
public class TPPermissibleBase extends PermissibleBase {

    protected final PermissibleBase initialParent;
    protected final CommandSender parent;
    protected final boolean debug;
    protected final boolean useReflectionPerm;

    public TPPermissibleBase(CommandSender p, boolean debugTime) {
        super(p);
        debug = debugTime;
        parent = p;
        useReflectionPerm = TotalPermissions.getPlugin().getConfiguration().getBoolean("reflection.starperm");
        Object obj = null;
        try {
            Class cl = Class.forName("org.bukkit.craftbukkit." + TotalPermissions.getPlugin().getBukkitVersion() + ".entity.CraftPlayer");
            Field field = cl.getField("perm");
            field.setAccessible(true);
            obj = field.get(parent);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TPPermissibleBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TPPermissibleBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TPPermissibleBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TPPermissibleBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TPPermissibleBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        initialParent = (PermissibleBase) obj;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        boolean has = initialParent.hasPermission(perm);
        if (useReflectionPerm) {
            if (initialParent.isPermissionSet(perm)) {
                has = initialParent.hasPermission(perm);
            } else if (initialParent.isPermissionSet("*")) {
                has = initialParent.hasPermission("*");
                if (perm.getDefault() == PermissionDefault.FALSE) {
                    has = false;
                }
            } else if (initialParent.isPermissionSet("**")) {
                has = initialParent.hasPermission("**");

            } else {
                has = false;
            }
        }
        if (debug) {
            TotalPermissions.getPlugin().getLogger().info("Checking if " + parent.getName() + " has " + perm.getName() + ": " + has);
        }
        return has;
    }

    @Override
    public boolean hasPermission(String perm) {
        return hasPermission(new Permission(perm));
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return initialParent.isPermissionSet(perm);
    }

    @Override
    public boolean isPermissionSet(String name) {
        return initialParent.isPermissionSet(name);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return initialParent.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return initialParent.addAttachment(plugin, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return initialParent.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return initialParent.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public synchronized void clearPermissions() {
        initialParent.clearPermissions();
    }

    @Override
    public boolean equals(Object obj) {
        return initialParent.equals(obj);
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return initialParent.getEffectivePermissions();
    }

    public PermissibleBase getInitialParent() {
        return initialParent;
    }

    public CommandSender getParent() {
        return parent;
    }

    @Override
    public int hashCode() {
        return initialParent.hashCode();
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    public boolean isOp() {
        return initialParent.isOp();
    }

    public boolean isUseReflectionPerm() {
        return useReflectionPerm;
    }

    @Override
    public void recalculatePermissions() {
        initialParent.recalculatePermissions();
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        initialParent.removeAttachment(attachment);
    }

    @Override
    public void setOp(boolean value) {
        initialParent.setOp(value);
    }

    @Override
    public String toString() {
        return initialParent.toString();
    }
}
