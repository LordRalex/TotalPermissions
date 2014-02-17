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

import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.base.PermissionGroup;
import net.ae97.totalpermissions.lang.Lang;
import org.bukkit.command.CommandSender;

/**
 * @author 1Rogue
 */
public class GroupCommand implements SubCommand {

    protected final TotalPermissions plugin;

    public GroupCommand(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            PermissionGroup pg = plugin.getDataManager().getGroup(args[1]);
            sender.sendMessage(Lang.COMMAND_GROUP_GROUP.getMessage(pg.getName()));
            StringBuilder sb = new StringBuilder();
            for (String name : pg.getInheritence()) {
                sb.append(name).append(", ");
            }
            String prefix = (String) pg.getOptions().get("prefix");
            if (prefix == null) {
                prefix = "";
            }
            String suffix = (String) pg.getOptions().get("suffix");
            if (suffix == null) {
                suffix = "";
            }
            sender.sendMessage(Lang.COMMAND_GROUP_INHERITS.getMessage((sb.length() >= 2) ? sb.substring(0, sb.length() - 2) : "None!"));
            sender.sendMessage(Lang.COMMAND_GROUP_PREFIX.getMessage(prefix));
            sender.sendMessage(Lang.COMMAND_GROUP_SUFFIX.getMessage(suffix));

            return true;
        } else if (args.length == 1) {
            StringBuilder sb = new StringBuilder();
            for (String group : plugin.getDataManager().getGroups()) {
                sb.append(group).append(", ");
            }
            sender.sendMessage(Lang.COMMAND_GROUP_LIST.getMessage(sb.substring(0, sb.length() - 2)));
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
            "ttp group " + Lang.VARIABLES_GROUP + " [actions..]",
            Lang.COMMAND_GROUP_HELP.getMessage()
        };
    }
}
