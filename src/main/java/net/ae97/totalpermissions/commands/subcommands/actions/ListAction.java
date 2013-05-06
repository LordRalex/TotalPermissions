/*
 * Copyright (C) 2013 Spencer Alderman
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
public class ListAction implements SubAction {

    public boolean execute(CommandSender sender, String type, String target, String field, String item) {
        //Self explanatory
        return true;
    }

    public String getName() {
        return "list";
    }

    public String[] getHelp() {
        return new String[]{
            "list <field>",
            "Lists the values from a given field"
        };
    }

    public String[] supportedTypes() {
        return new String[] {
            "permission",
            "inheritance",
            "command",
            "group",
            "prefix",
            "suffix"
        };
    }
}