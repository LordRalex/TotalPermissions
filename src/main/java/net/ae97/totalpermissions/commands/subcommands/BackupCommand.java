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
import net.ae97.totalpermissions.permission.util.FileUpdater;
import org.bukkit.command.CommandSender;

/**
 * @since 0.1
 * @author Lord_Ralex
 * @version 0.2
 */
public class BackupCommand implements SubCommand {
    
    private final TotalPermissions plugin;
    
    public BackupCommand(TotalPermissions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        FileUpdater update = new FileUpdater(plugin);
        update.backup(true);
        update.runUpdate();
        return true;
    }

    @Override
    public String getName() {
        return "backup";
    }

    @Override
    public String[] getHelp() {
        return new String[]{
            "ttp backup",
            this.plugin.getLangFile().getString("command.backup.help")
        };
    }
}