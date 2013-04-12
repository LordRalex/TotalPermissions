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

import org.bukkit.command.CommandSender;

/**
 *
 * @since
 * @author 1Rogue
 * @version
 */
public class AddAction implements SubAction {

    public boolean execute(CommandSender sender, String type, String target, String[] args) {
        // Add perms
        return true;
    }

    public String getName() {
        return "add";
    }

    public String[] getHelp() {
        return new String[]{
            "add <permnode>",
            "Adds a perm node to a user"
        };
    }
    
    public String[] supportedTypes() {
        return new String[]{
            "group",
            "user"
        };
    }
    
}
