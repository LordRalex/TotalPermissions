/*
 * Copyright (C) 2013 Spencer
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
package com.lordralex.totalpermissions.commands.subcommands.actions;

import org.bukkit.command.CommandSender;

/**
 *
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public interface SubAction {
    
    /**
     * Executes the command. Only the args and sender are needed.
     * 
     * @return Success of the command. False if an issue
     */
    public abstract boolean execute(CommandSender sender, String type, String target, String[] args);
    
    /**
     * Returns the action's name. Used for both info and the perm node.
     *
     * @return Name of the command
     */
    public abstract String getName();

    public abstract String[] getHelp();
    
    public abstract String[] supportedTypes();
    
}
