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

import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.base.PermissionUser;
import net.ae97.totalpermissions.exceptions.DataLoadFailedException;
import net.ae97.totalpermissions.lang.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Lord_Ralex
 */
public class DebugCommand implements SubCommand {

    protected final TotalPermissions plugin;

    public DebugCommand(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        PermissionUser target;
        try {
            target = plugin.getDataManager().getUser(args[0]);
        } catch (DataLoadFailedException ex) {
            plugin.log(Level.SEVERE, "An error occured on loading " + args[0], ex);
            sender.sendMessage(ChatColor.RED + "An error occured on loading " + args[0]);
            return true;
        }
        if (target == null) {
            sender.sendMessage(Lang.COMMAND_DEBUG_NULLTARGET.getMessage(args[1]));
            return true;
        }
        if (args.length == 2 && !args[1].equalsIgnoreCase("status")) {
            boolean newState;
            if ((args[1].equalsIgnoreCase("on")) || (args[1].equalsIgnoreCase("enable")) || (args[1].equalsIgnoreCase("true"))) {
                newState = true;
            } else if (args[1].equalsIgnoreCase("toggle")) {
                newState = !target.isDebug();
            } else {
                sender.sendMessage(args[1] + " is an invalid argument");
                return false;
            }
            target.setDebug(newState);
            if (target.isDebug()) {
                sender.sendMessage(Lang.COMMAND_DEBUG_DEBUGON.getMessage(target.getName()));
            } else {
                sender.sendMessage(Lang.COMMAND_DEBUG_DEBUGOFF.getMessage(target.getName()));
            }
        } else {
            sender.sendMessage(target.getName() + "'s debug status: " + (target.isDebug() ? "on" : "off"));
        }
        return true;
    }

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "ttp debug " + Lang.VARIABLES_USERNAME.getMessage() + " [on/off]",
            Lang.COMMAND_DEBUG_HELP.getMessage()
        };
    }
}
