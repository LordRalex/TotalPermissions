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
 * @since 0.1
 * @author Lord_Ralex
 * @version 0.1
 */
public class BackupCommand implements SubCommand {

    public void execute(CommandSender sender, String[] args) {
    }
    
    public String getName() {
        return "backup";
    }

    public String getPerm() {
        return "totalpermissions.cmd.backup";
    }

    public String[] getHelp() {
        return new String[] {
            "/ttp backup",
            "Forces back up of files"
        };
    }
}