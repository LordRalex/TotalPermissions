package com.lordralex.permissionsar.commands.subcommands;

import org.bukkit.command.CommandSender;

/**
 *
 * @author AmberK
 */
public class HelpCommand extends SubCommand {

    @Override
    public void execute(CommandSender cs, String[] args) {
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
