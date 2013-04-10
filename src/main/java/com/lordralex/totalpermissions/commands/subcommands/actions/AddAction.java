package com.lordralex.totalpermissions.commands.subcommands.actions;

import org.bukkit.command.CommandSender;

/**
 *
 * @since
 * @author 1Rogue
 * @version
 */
public class AddAction implements SubAction {

    public boolean execute(CommandSender sender, String type, String target, String[] args) {
        // Add perms
        return true;
    }

    public String getName() {
        return "add";
    }

    public String[] getHelp() {
        return new String[]{
            "add <permnode>",
            "Adds a perm node to a user"
        };
    }
    
    public String[] supportedTypes() {
        return new String[]{
            "group",
            "user"
        };
    }
    
}
