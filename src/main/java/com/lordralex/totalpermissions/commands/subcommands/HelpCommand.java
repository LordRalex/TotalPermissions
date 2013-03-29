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

import com.lordralex.totalpermissions.TotalPermissions;
import org.bukkit.command.CommandSender;

/**
 * @since 0.1
 * @author 1Rogue
 * @version 0.1
 */
public class HelpCommand implements SubCommand {

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length == 1) {
            args = new String[] {"1"};
        }
        int page;
        try
        {
            page = Integer.parseInt(args[0]);
            if (page < 1) {
                page = 1;
            }
        }
        catch (NumberFormatException e) {
            page = 1;
        }
        cs.sendMessage(getPage(page));
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
            "/ttp help",
            "Displays help information"
        };
    }
    private String getPage(int page) {
        int factor = 5;
        int index = (page - 1) * factor;
        int listSize = TotalPermissions.getPlugin().getCommandHandler().getCommandList().size();
        if (index > listSize) {
            return "";
        }
        int upper = index + factor;
        if (upper >= listSize) {
            upper = listSize;
        }
        Object[] list = TotalPermissions.getPlugin().getCommandHandler().getCommandList().keySet().toArray();
        StringBuilder sb = new StringBuilder();
        for (int i = index; i < upper; i++) {
            Object db = TotalPermissions.getPlugin().getCommandHandler().getCommandList().get(list[i]);
            SubCommand dd = null;
            if ((db instanceof SubCommand)) {
                dd = (SubCommand)db;
            }
            if (dd != null) {
                sb.append(dd.getHelp()[0]).append(" - ").append(dd.getHelp()[1]);
                if (i != upper - 1) {
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
}
