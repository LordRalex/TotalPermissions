/*
 * Copyright (C) 2014 AE97
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
import java.util.Map;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.lang.Lang;
import net.ae97.totalpermissions.util.Formatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author 1Rogue
 */
public class HelpCommand implements SubCommand {

    protected final TotalPermissions plugin;

    public HelpCommand(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        //don't quite understand what this point of the help arg in this, but to fix a bug, will keep it
        if (args.length == 1 || args.length == 0) {
            args = new String[]{"help", "1"};
        }
        int page = getInt(args[1]);
        Map commands = plugin.getCommandHandler().getCommandList();
        cs.sendMessage(getPage(page, commands));
        return true;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "ttp help",
            Lang.COMMAND_HELP_HELP.getMessage()
        };
    }

    private String getPage(int page, Map<String, Object> map) {
        int factor = 5;
        int index = (page - 1) * factor;
        int listSize = map.size();
        if (index > listSize) {
            return "";
        }
        int upper = index + factor;
        if (upper >= listSize) {
            upper = listSize;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Formatter.formatTitle(plugin.getName(), ChatColor.WHITE, ChatColor.RED)).append("\n");
        sb.append(Lang.COMMAND_HELP_PAGE.getMessage(page, (int) Math.ceil((double) listSize / (double) factor))).append("\n").append(ChatColor.RESET);
        String[] list = map.keySet().toArray(new String[listSize]);
        Arrays.sort(list);
        for (int i = index; i < upper; i++) {
            Object test = map.get(list[i]);
            if (test != null) {
                if (test instanceof SubCommand) {
                    SubCommand db = (SubCommand) map.get(list[i]);
                    sb.append(db.getHelp()[0]).append(" - ").append(db.getHelp()[1]);
                }
                if (i != upper - 1) {
                    sb.append("\n");
                }
            }
        }
        sb.append('\n').append(Lang.COMMAND_HELP_CONTPLAIN.getMessage(Lang.VARIABLES_COMMAND.getMessage()));
        return sb.toString();
    }

    private int getInt(String test) {
        int page;
        try {
            page = Integer.parseInt(test);
            if (page < 1) {
                page = 1;
            }
        } catch (NumberFormatException e) {
            page = 1;
        }
        return page;
    }
}
