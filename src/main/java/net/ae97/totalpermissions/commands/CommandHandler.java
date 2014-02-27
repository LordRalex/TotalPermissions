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
package net.ae97.totalpermissions.commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.commands.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Lord_Ralex
 */
public final class CommandHandler implements CommandExecutor {

    private final Map<String, SubCommand> commands = new ConcurrentHashMap<String, SubCommand>();
    private final TotalPermissions plugin;

    public CommandHandler(TotalPermissions p) {
        plugin = p;

        SubCommand[] cmds = new SubCommand[]{
            new HelpCommand(plugin),
            new DebugCommand(plugin),
            new UserCommand(plugin),
            new GroupCommand(plugin),
            new SpecialCommand(plugin),
            new WorldCommand(plugin),
            new DumpCommand(plugin)
        };

        for (SubCommand sc : cmds) {
            commands.put(sc.getName(), sc);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] rawargs) {
        String subCommand;
        // Default to help
        if (rawargs.length < 1) {
            rawargs = new String[]{"help"};
        }

        // Allow for quotes within commands
        StringBuilder sb = new StringBuilder();
        for (String temp : rawargs) {
            sb.append(temp).append(" ");
        }

        String[] args;
        if (sb.toString().contains("\"")) {
            char[] broken = sb.toString().trim().toCharArray();
            StringBuilder usb = new StringBuilder();
            ArrayList<String> newargs = new ArrayList();
            boolean inquotes = false;
            for (int i = 0; i < broken.length; i++) {
                if (broken[i] == '"') {
                    inquotes = !inquotes;
                } else if (broken[i] == ' ' && inquotes == false) {
                    newargs.add(usb.toString());
                    usb = new StringBuilder();
                } else {
                    usb.append(broken[i]);
                }
            }

            args = newargs.toArray(new String[newargs.size()]);
        } else {
            args = rawargs;
        }

        subCommand = args[0];
        SubCommand executor = commands.get(subCommand.toLowerCase());
        if (executor == null) {
            sender.sendMessage(ChatColor.RED + "No action found, use /ttp help for the action list");
            return true;
        }
        if ((args.length > 1) && (args[1].equalsIgnoreCase("help"))) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: " + executor.getHelp()[0]);
            sender.sendMessage(ChatColor.YELLOW + executor.getHelp()[1]);
            return true;
        }
        int length = args.length - 1;
        if (length < 0) {
            length = 0;
        }
        String[] newArgs = new String[length];
        for (int i = 0; i < newArgs.length; i++) {
            newArgs[i] = args[i + 1];
        }
        if (sender.hasPermission("totalpermissions.cmd" + executor.getName())) {
            if (!executor.execute(sender, newArgs)) {
                String[] help = executor.getHelp();
                for (String h : help) {
                    sender.sendMessage(h);
                }
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to use this command");
        }
        return true;
    }

    public Map<String, SubCommand> getCommandList() {
        return commands;
    }
}
