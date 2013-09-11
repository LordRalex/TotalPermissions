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
package net.ae97.totalpermissions.commands.subcommands.actions;

import net.ae97.totalpermissions.permission.PermissionBase;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public abstract class SubAction {

    /**
     * Executes the command. Only the args and sender are needed.
     *
     * @return Success of the command. False if an issue
     *
     * @param sender The executor of the command
     * @param aType The top-level type being edited (e.g. group)
     * @param target The relevant item being modified
     * @param field The specific type of data being edited (e.g. permissions)
     * @param item The new data value
     * @param world The world to edit in, null if global.
     */
    public abstract boolean execute(CommandSender sender, String aType, String target, String field, String item, String world);

    /**
     * Returns the action's name. Used for both info and the perm node.
     *
     * @return Name of the command
     *
     * @since 0.2
     */
    public abstract String getName();

    /**
     * Gets the help message for this sub action
     *
     * @return Help message
     *
     * @since 0.2
     */
    public abstract String[] getHelp();

    /**
     * Gets the supported types for this sub action. This results Strings, so
     * only is for visual help
     *
     * @return The supported types for this sub action
     *
     * @since 0.2
     */
    public abstract String[] supportedTypes();

    protected void saveError(TotalPermissions plugin, PermissionBase tar, CommandSender sender, Exception ex) {
        sender.sendMessage(plugin.getLangFile().getString("command.action.subaction.saveerror1"));
        sender.sendMessage(plugin.getLangFile().getString("command.action.subaction.saveerror2"));
        plugin.getLogger().log(Level.SEVERE, plugin.getLangFile().getString("command.action.subaction.saveerror3", tar.getType(), tar.getName()), ex);
    }
}