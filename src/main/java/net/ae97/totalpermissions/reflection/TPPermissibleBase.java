/*
 * Copyright (C) 2013 Lord_Ralex
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

/**
 * @author Lord_Ralex
 * @version 0.1
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
            Class cl = Class.forName("org.bukkit.craftbukkit." + TotalPermissions.getBukkitVersion() + ".entity.CraftHumanEntity");
            Field field = cl.getDeclaredField("perm");
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
        System.out.println("Stage zero");
        boolean has = initialParent.hasPermission(perm);
        if (useReflectionPerm) {
            if (initialParent.isPermissionSet(perm)) {
                System.out.println("Stage one");
                has = initialParent.hasPermission(perm);
            } else if (initialParent.isPermissionSet("*")) {
                System.out.println("Stage two");
                has = initialParent.hasPermission("*");
                if (perm.getDefault() == PermissionDefault.FALSE) {
                    has = false;
                }
            } else if (initialParent.isPermissionSet("**")) {
                System.out.println("Stage three");
                has = initialParent.hasPermission("**");

            } else {
                System.out.println("Stage four");
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
        return initialParent.isPermissionSet(perm); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isPermissionSet(String name) {
        return initialParent.isPermissionSet(name); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return initialParent.addAttachment(plugin); //To change body of generated methods, choose Tools | Templates.
    }
}
