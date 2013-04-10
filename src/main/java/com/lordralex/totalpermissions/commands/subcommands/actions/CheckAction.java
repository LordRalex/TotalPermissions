package com.lordralex.totalpermissions.commands.subcommands.actions;

import org.bukkit.command.CommandSender;

/**
 *
 * @since
 * @author 1Rogue
 * @version
 */
public class CheckAction implements SubAction {

    public boolean execute(CommandSender sender, String type, String target, String[] args) {
        // Checks for a perm node
        return true;
    }

    public String getName() {
        return "check";
    }

    public String[] getHelp() {
        return new String[]{
            "check <permnode>",
            "Checks if a given target has the provided perm node"
        };
    }
    
    public String[] supportedTypes() {
        return new String[]{
            "group",
            "user"
        };
    }
    
}
