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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import net.ae97.totalpermissions.TotalPermissions;
import net.ae97.totalpermissions.data.DataHolder;
import net.ae97.totalpermissions.data.YamlDataHolder;
import net.ae97.totalpermissions.lang.Lang;
import net.ae97.totalpermissions.util.DataHolderMerger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 * @since 0.1
 * @author Lord_Ralex
 * @version 0.2
 */
public class BackupCommand implements SubCommand {

    protected final TotalPermissions plugin;
    protected int isRunningBackup = 0;

    public BackupCommand(TotalPermissions p) {
        plugin = p;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (isRunningBackup != 0) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "A backup is already being made (Task id: " + isRunningBackup);
            } else {
                if (args[0].equalsIgnoreCase("cancel")) {
                    Bukkit.getScheduler().cancelTask(isRunningBackup);
                    isRunningBackup = 0;
                    sender.sendMessage(ChatColor.RED + "Backup cancelled");
                }
            }
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + "Backing up the data, this may take some time");
        File dest = plugin.getBackupFolder();
        YamlDataHolder target = new YamlDataHolder(new File(new File(dest, "manual"), "backup.yml"));
        isRunningBackup = Bukkit.getScheduler().runTaskAsynchronously(plugin, new BackupRunnable(sender, plugin.getDataHolder(), target)).getTaskId();
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
            Lang.COMMAND_BACKUP_HELP.getMessage()
        };
    }

    private class BackupRunnable implements Runnable {

        private final CommandSender sender;
        private final DataHolder parent, child;

        protected BackupRunnable(CommandSender s, DataHolder p, DataHolder c) {
            sender = s;
            parent = p;
            child = c;
        }

        @Override
        public void run() {
            DataHolderMerger merger = new DataHolderMerger(plugin, parent);
            try {
                merger.merge(child);
                Bukkit.getScheduler().callSyncMethod(plugin, new MessageSender(sender, ChatColor.GREEN + "Backup complete", ChatColor.GREEN + "Backup stored to " + null));
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "IOException on merging files", ex);
                Bukkit.getScheduler().callSyncMethod(plugin, new MessageSender(sender, ChatColor.RED + "An IOException occurred on backing up the data"));
            } catch (InvalidConfigurationException ex) {
                plugin.getLogger().log(Level.SEVERE, "Invalid config error", ex);
                Bukkit.getScheduler().callSyncMethod(plugin, new MessageSender(sender, ChatColor.RED + "An InvalidConfigurationException occurred on backing up the data"));
            } finally {
                isRunningBackup = 0;
            }
        }
    }

    private class MessageSender implements Callable {

        private final String[] messages;
        private final CommandSender sender;

        protected MessageSender(CommandSender target, String... message) {
            sender = target;
            messages = message;
        }

        @Override
        public Object call() {
            for (String message : messages) {
                sender.sendMessage(message);
            }
            return messages;
        }
    }
}
