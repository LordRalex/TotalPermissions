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
import com.lordralex.totalpermissions.permission.PermissionUser;
import org.bukkit.command.CommandSender;

/**
 * @since 0.1
 * @author Lord_Ralex
 * @version 0.1
 */
public class DebugCommand implements SubCommand {

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No target specified, for help, use /totalperms help debug");
            return;
        }
        PermissionUser target = TotalPermissions.getPlugin().getManager().getUser(args[1]);
        if (target == null) {
            sender.sendMessage("Target user (" + args[0] + ") was not found");
            return;
        }
        boolean newState = false;
        if (args.length == 2) {
            if ((args[1].equalsIgnoreCase("on")) || (args[1].equalsIgnoreCase("enable")) || (args[1].equalsIgnoreCase("true"))) {
                newState = true;
            }
            else if (args[1].equalsIgnoreCase("toggle")) {
                newState = !target.getDebugState();
            }
            else {
                newState = false;
            }
        }
        target.setDebug(newState);
        if (target.getDebugState()) {
            sender.sendMessage("Debug turned on for " + target.getName());
        }
        else {
            sender.sendMessage("Debug turned off for " + target.getName());
        }
    }
    
    public String getName() {
        return "debug";
    }

    public String getPerm() {
        return "totalpermissions.cmd.debug";
    }

    public String[] getHelp() {
        return new String[] {
            "/ttp debug <username>",
            "Turns on debug mode for a user"
        };
    }
}