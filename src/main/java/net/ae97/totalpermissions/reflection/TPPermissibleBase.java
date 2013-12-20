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
import org.bukkit.Bukkit;
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
    protected final TotalPermissions plugin;

    public TPPermissibleBase(CommandSender p, boolean debugTime) {
        super(p);
        plugin = TotalPermissions.getPlugin();
        debug = debugTime;
        parent = p;
        Object obj = null;
        try {
            Class cl = Class.forName("org.bukkit.craftbukkit." + plugin.getBukkitVersion() + ".entity.CraftPlayer");
            Field field = cl.getField("perm");
            field.setAccessible(true);
            obj = field.get(parent);
        } catch (NoSuchFieldException ex) {
            plugin.getLogger().log(Level.SEVERE, "No such field: perm", ex);
        } catch (SecurityException ex) {
            plugin.getLogger().log(Level.SEVERE, "Security exception occurred", ex);
        } catch (IllegalArgumentException ex) {
            plugin.getLogger().log(Level.SEVERE, "Illegal argument passed to field", ex);
        } catch (IllegalAccessException ex) {
            plugin.getLogger().log(Level.SEVERE, "Cannot access perm", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "cannot find " + "org.bukkit.craftbukkit." + plugin.getBukkitVersion() + ".entity.CraftPlayer", ex);
        }
        initialParent = (PermissibleBase) obj;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        plugin.debugLog("Checking for " + perm.getName() + " in " + initialParent);
        boolean has = initialParent.hasPermission(perm);
        plugin.debugLog("Has: " + has);
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
        if (debug) {
            plugin.getLogger().log(Level.INFO, "Checking if {0} has {1}: {2}",
                    new Object[]{parent.getName(), perm.getName(), has});
        }
        return has;
    }

    @Override
    public boolean hasPermission(String perm) {
        Permission permNode;
        permNode = Bukkit.getPluginManager().getPermission(perm);
        if (permNode == null) {
            permNode = new Permission(perm);
            Bukkit.getPluginManager().addPermission(permNode);
        }
        return hasPermission(permNode);
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
