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
package net.ae97.totalpermissions.commands.subcommands;

import java.util.Arrays;
import java.util.List;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.lang.Lang;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.1
 */
public class SpecialCommand implements SubCommand {

    protected final TotalPermissions plugin;

    public SpecialCommand(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 2) { // If there is an action command
            plugin.getCommandHandler().getActionHandler().onAction(sender, args, fields());
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "special";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "ttp special [rcon|console|op] [actions..]",
            Lang.COMMAND_SPECIAL_HELP.getMessage()
        };
    }

    private List<String> fields() {
        return Arrays.asList(new String[]{
            "permissions",
            "inheritance",
            "commands",
            "prefix",
            "suffix",
            "groups"
        });
    }
}
