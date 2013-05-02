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
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class CheckAction implements SubAction {

    public boolean execute(CommandSender sender, String type, String target, String field, String item) {
        //Check if the provided area has the right value within it. Not for listing
        return false;
    }

    public String getName() {
        return "check";
    }

    public String[] getHelp() {
        return new String[]{
            "check <field> <value>",
            "Checks if a given target's field has the provided value"
        };
    }
    
    public String[] supportedTypes() {
        return new String[] {
            "permission",
            "inheritance",
            "command",
            "group",
            "default",
            "prefix",
            "suffix"
        };
    }
}
