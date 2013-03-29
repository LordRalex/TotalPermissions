package com.lordralex.totalpermissions.commands.subcommands;

import org.bukkit.command.CommandSender;

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
        return new String[] { "/ttp backup", "Forces back up of files" };
    }
}