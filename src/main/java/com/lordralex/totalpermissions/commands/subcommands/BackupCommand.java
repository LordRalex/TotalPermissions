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

import org.bukkit.command.CommandSender;

/**
 * @author LordRalex
 * @version 1.0
 */
public class BackupCommand implements SubCommand {

    public String getName() {
        return "backup";
    }

    public void execute(CommandSender sender, String[] args) {
    }

    public String getPerm() {
        return "totalpermissions.cmd.backup";
    }

    public String[] getHelp() {
        return new String[]{
            "Usage: /totalperms backup",
            "This forces a back up of the permission and config files"
        };
    }
}
