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

import net.ae97.totalpermissions.TotalPermissions;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class ActionHandler {

    protected final Map<String, SubAction> actions = new ConcurrentHashMap<String, SubAction>();
    protected final TotalPermissions plugin;

    public ActionHandler(TotalPermissions p) {
        plugin = p;

        SubAction[] acts = new SubAction[]{
            new AddAction(plugin),
            new CheckAction(plugin),
            new ListAction(plugin),
            new RemoveAction(plugin),
            new SetAction(plugin)
        };

        for (SubAction act : acts) {
            actions.put(act.getName(), act);
        }
    }

    public boolean onAction(CommandSender sender, String[] args, List<String> fields) {
        String type = args[0].toLowerCase();
        String target = args[1];
        String world;
        String field;
        String iterm;
        String ackshun;

        switch (args.length) {
            case 4:
                ackshun = args[2];
                field = args[3];
                iterm = null;
                world = null;
                break;
            case 5:
                ackshun = args[2];
                field = args[3];
                iterm = args[4];
                world = null;
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
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.handler.ifnull"));
            return false;
        }

        if (args.length < 5 && !((ackshun.equalsIgnoreCase("list")) || field.equalsIgnoreCase("help"))) {
            sender.sendMessage("Invalid use of actions.");
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.handler.usage", executor.getHelp()[0]));
            sender.sendMessage(executor.getHelp()[1]);
            return true;
        }

        if ((args.length >= 4) && (field.equalsIgnoreCase("help"))) {
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.handler.usage", executor.getHelp()[0]));
            sender.sendMessage(executor.getHelp()[1]);
            return true;
        }

        if (sender.hasPermission("totalpermissions.cmd." + type + "." + executor.getName())) {
            if (isSupported(field, fields)) {
                boolean begin = executor.execute(sender, type, target, field, iterm, world);
                if (!begin) {
                    sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.handler.invalid", ackshun));
                    StringBuilder sb = new StringBuilder();
                    for (String item : executor.supportedTypes()) {
                        sb.append(item).append(", ");
                    }
                    sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.handler.execfields", ackshun, sb.toString().substring(0, sb.length() - 3)));
                }
            } else {
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.handler.notsupported", field, type));
                StringBuilder sb = new StringBuilder();
                for (String item : fields) {
                    sb.append(item).append(", ");
                }
                sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.handler.editfields", type, sb.toString().substring(0, sb.length() - 3)));
            }
            return true;
        } else {
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.action.handler.denied"));
        }
        return true;
    }

    private boolean isSupported(String arg, List<String> allowed) {
        return (allowed.contains(arg) || arg.equalsIgnoreCase("help"));
    }

    /**
     * Gets the registered SubActions
     *
     * @return Map of registered SubActions
     *
     * @since 0.2
     */
    public Map<String, SubAction> getActionList() {
        return actions;
    }
}