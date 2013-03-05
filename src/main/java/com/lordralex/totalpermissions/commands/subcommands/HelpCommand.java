/*
 * Copyright (C) 2013 LordRalex
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
package com.lordralex.totalpermissions.commands.subcommands;

import org.bukkit.command.CommandSender;

/**
 * @since 0.1
 * @author AmberK
 * @version 0.1
 */
public class HelpCommand implements SubCommand {

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length == 1) {
            args = new String[]{"1"};
        }
        int page = 1;
        try {
            page = Integer.parseInt(args[0]);
            if (page > 3) {
                page = 3;
            }
            if (page < 1) {
                page = 1;
            }
        } catch (NumberFormatException e) {
        }
        switch (page) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        cs.sendMessage("Use /totalperms <command> help for help with a command");
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPerm() {
        return "totalpermissions.cmd.help";
    }

    public String[] getHelp() {
        return new String[]{
            "Usage: /totalperms help",
            "Return the help center for TotalPermissions"
        };
    }
}
