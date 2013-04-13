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
package net.ae97.totalpermissions.commands.subcommands;

import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.commands.subcommands.actions.SubAction;
import net.ae97.totalpermissions.util.Formatter;
import java.util.Arrays;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @since 0.1
 * @author 1Rogue
 * @version 0.1
 */
public class HelpCommand implements SubCommand {

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("actions")) {
            if (args.length == 2) {
                args = new String[]{"help", "actions", "1"};
                int page = this.getInt(args[2]);
                Map actions = TotalPermissions.getPlugin().getCommandHandler().getActionHandler().getActionList();
                cs.sendMessage(getPage(page, actions));
                cs.sendMessage("Use \"help\" without quotes after an action for help information");
            }
            return;
        }
        if (args.length == 1) {
            args = new String[]{"help", "1"};
        }
        int page = this.getInt(args[1]);
        Map commands = TotalPermissions.getPlugin().getCommandHandler().getCommandList();
        cs.sendMessage(getPage(page, commands));
        cs.sendMessage("Use /ttp <command> help for help with a command");
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
        sb.append(Formatter.formatTitle(TotalPermissions.getPlugin().getName(), ChatColor.WHITE, ChatColor.WHITE)).append("\n");
        sb.append(ChatColor.RED).append("Page ").append(page).append(" of ").append((int)Math.ceil((double)listSize/(double)factor)).append("\n").append(ChatColor.RESET);
        String[] list = map.keySet().toArray(new String[listSize]);
        Arrays.sort(list);
        for (int i = index; i < upper; i++) {
            Object test = map.get(list[i]);
            if (test != null) {
                if (test instanceof SubCommand) {
                    SubCommand db = (SubCommand)map.get(list[i]);
                    sb.append(db.getHelp()[0]).append(" - ").append(db.getHelp()[1]);
                }
                else if (test instanceof SubAction) {
                    SubAction db = (SubAction)map.get(list[i]);
                    sb.append(db.getHelp()[0]).append(" - ").append(db.getHelp()[1]);
                }
                if (i != upper - 1) {
                    sb.append("\n");
                }
            }
        }
        sb.append("Use /ttp <command> help for help with a command");
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
