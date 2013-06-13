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
package net.ae97.totalpermissions.commands.subcommands.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.ae97.totalpermissions.TotalPermissions;
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
        CheckAction check = new CheckAction();
        actions.put(check.getName().toLowerCase().trim(), check);
        ListAction list = new ListAction();
        actions.put(list.getName().toLowerCase().trim(), list);
        RemoveAction remove = new RemoveAction();
        actions.put(remove.getName().toLowerCase().trim(), remove);
        SetAction set = new SetAction();
        actions.put(set.getName().toLowerCase().trim(), set);
    }

    public boolean onAction(CommandSender sender, String[] args, List<String> fields) {
        String type = args[0].toLowerCase();
        String target = args[1];
        String world = null;
        String field = null;
        String iterm = null;
        String ackshun = null;

        switch (args.length) {
            case 4:
                ackshun = args[2];
                field = args[3];
                break;
            case 5:
                ackshun = args[2];
                field = args[3];
                iterm = args[4];
                break;
            case 6:
                ackshun = args[2];
                field = args[3];
                iterm = args[4];
                world = args[5];
                break;
            default:
                return false;
        }
        
        SubAction executor = actions.get(ackshun.toLowerCase());
        if (executor == null) {
            sender.sendMessage("No action found, use /ttp help actions for an action list");
            return false;
        }

        if (args.length < 5 && !((ackshun.equalsIgnoreCase("list")) || field.equalsIgnoreCase("help"))) {
            sender.sendMessage("Invalid use of actions.");
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.handler.usage", executor.getHelp()[0]));
            sender.sendMessage(executor.getHelp()[1]);
            return true;
        }

        if ((args.length >= 4) && (field.equalsIgnoreCase("help"))) {
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.handler.usage", executor.getHelp()[0]));
            sender.sendMessage(executor.getHelp()[1]);
            return true;
        }

        if (sender.hasPermission("totalpermissions.cmd." + type + "." + executor.getName())) {
            if (this.isSupported(field, fields)) {
                boolean begin = executor.execute(sender, type, target, field, iterm, world);
                if (!begin) {
                    sender.sendMessage(ChatColor.RED + "Invalid use of " + ackshun);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Executable fields for '").append(ackshun).append("': ");
                    for (String item : executor.supportedTypes()) {
                        sb.append(item).append(", ");
                    }
                    sender.sendMessage(ChatColor.RED + sb.toString().substring(0, sb.length() - 3));
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Field '" + field + "' is not supported for " + type + "!");
                StringBuilder sb = new StringBuilder();
                    sb.append("Editable fields for '").append(type).append("': ");
                    for (String item : fields) {
                        sb.append(item).append(", ");
                    }
                    sender.sendMessage(ChatColor.RED + sb.toString().substring(0, sb.length() - 3));
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + TotalPermissions.getPlugin().getLangFile().getString("command.handler.denied"));
        }
        return true;
    }

    private boolean isSupported(String arg, List<String> allowed) {
        return (allowed.contains(arg) || arg.equalsIgnoreCase("help"));
    }

    public Map getActionList() {
        return actions;
    }
}