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
package net.ae97.totalpermissions.commands.subcommands;

import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.permission.PermissionGroup;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class GroupCommand implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 2) { // If there is an action command
            TotalPermissions.getPlugin().getCommandHandler().getActionHandler().onAction(sender, args, fields());
            return true;
        } else if (args.length == 2) {
            PermissionGroup pg = TotalPermissions.getPlugin().getManager().getGroup(args[2]);
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.group.group", pg.getName()));
            StringBuilder sb = new StringBuilder();
            for (String name : pg.getInheritances(pg.getName())) {
                sb.append(name).append(", ");
            }
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.group.inherits", sb.substring(0, sb.length() - 2)));
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.group.prefix", pg.getName(), pg.getOption("prefix")));
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.group.suffix", pg.getName(), pg.getOption("suffix")));

        } else if (args.length == 1) {
            StringBuilder sb = new StringBuilder();
            for (String group : TotalPermissions.getPlugin().getManager().getGroups()) {
                sb.append(group).append(", ");
            }
            sender.sendMessage(TotalPermissions.getPlugin().getLangFile().getString("command.group.list", sb.substring(0, sb.length() - 2)));
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "group";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "/ttp group " + TotalPermissions.getPlugin().getLangFile().getString("variables.group") + " [actions..]",
            TotalPermissions.getPlugin().getLangFile().getString("command.group.help")
        };
    }

    private List<String> fields() {
        return Arrays.asList(new String[]{
            "permissions",
            "inheritance",
            "commands",
            "prefix",
            "suffix"
        });
    }
}