package com.lordralex.totalpermissions.commands.subcommands;

import org.bukkit.command.CommandSender;

/**
 *
 * @author AmberK
 */
public class HelpCommand implements SubCommand {

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length == 1) {
            args = new String[]{"1"};
        }
        int page = 1;
        try {
            page = Integer.parseInt(args[0]);
            if (page > 3) {
                page = 3;
            }
            if (page < 1) {
                page = 1;
            }
        } catch (NumberFormatException e) {
        }
        switch (page) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        cs.sendMessage("Use /totalperms <command> help for help with a command");
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPerm() {
        return "totalpermissions.command.help";
    }

    public String[] getHelp() {
        return new String[]{
                    "Usage: /totalperms help",
                    "Return the help center for TotalPermissions"
                };
    }
}
