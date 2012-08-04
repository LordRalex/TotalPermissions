package com.lordralex.permissionsar.commands.subcommands.subclasses;

import com.lordralex.permissionsar.commands.subcommands.SubCommand;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Amber
 */
public class Template extends SubCommand {

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args[0].equalsIgnoreCase("import")) {
            switch (args.length) {
                case 1:
                    /* Do crazy importin' stuff */
                    break;
                default:
                    cs.sendMessage("Invalid args");
                    cs.sendMessage("Usage: " + getHelp());
                    break;
            }
        }
    }

    
    // Not OVERLY concerned from here.
    
    @Override
    public String getName() {
        return "[thisarg]";
    }

    @Override
    public String getHelp() {
        return "cmd [thisarg]";
    }
}
