package com.lordralex.permissionsar.commands.subcommands;

import org.bukkit.command.CommandSender;

/**
 *
 * @author AmberK
 */
public class HelpCommand extends SubCommand {
    
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
                pageOne(cs);
                break;
        }
        cs.sendMessage("Use /par <command> help for help with a command");
    }

    /**
     * First page to handle both none and one argument. Unfinished, falling
     * asleep
     */
    public void pageOne(CommandSender cs) {
        //cs.sendMessage();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPerm() {
        return "par.command.help";
    }
}
