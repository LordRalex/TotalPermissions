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
package net.ae97.totalpermissions.commands.subcommands.actions;

import net.ae97.totalpermissions.util.ArrayShift;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class ActionHandler {

    protected final Map<String, SubAction> actions = new HashMap<String, SubAction>();

    public ActionHandler() {
        AddAction add = new AddAction();
        actions.put(add.getName().toLowerCase().trim(), add);
    }

    public boolean onAction(CommandSender sender, String[] args) {
        String type = args[0].toLowerCase();
        String target = args[1];
        String[] newArgs = ArrayShift.getEndOfStringArray(args, 2);
        
        SubAction executor = actions.get(args[2].toLowerCase());
        if (executor == null) {
            sender.sendMessage("No action found, use /ttp help actions for an action list");
            return false;
        }
        
        if ((newArgs.length > 1) && (newArgs[1].equalsIgnoreCase("help"))) {
            sender.sendMessage("Usage: " + executor.getHelp()[0]);
            sender.sendMessage(executor.getHelp()[1]);
            return true;
        }
        
        if (sender.hasPermission("totalpermissions." + type + "." + executor.getName())) {
            if (this.isSupported(executor, type)) {
                executor.execute(sender, type, target, args);
            }
            else {
                sender.sendMessage(ChatColor.RED + "Action '" + args[2] + "' is not supported for " + type + "s!");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You cannot use that command");
        }
        return true;
    }
    
    private boolean isSupported (SubAction action, String type) {
        for (String comp : action.supportedTypes()) {
            if (comp.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    public Map getActionList() {
        return actions;
    }
}
