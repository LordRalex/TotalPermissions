package com.lordralex.totalpermissions.commands.subcommands.actions;

import org.bukkit.command.CommandSender;

/**
 *
 * @since
 * @author 1Rogue
 * @version
 */
public class RemoveAction implements SubAction {

    public boolean execute(CommandSender sender, String type, String target, String[] args) {
        // Remove perms
        return true;
    }

    public String getName() {
        return "remove";
    }

    public String[] getHelp() {
        return new String[]{
            "remove <permnode>",
            "Removes a perm node from a user"
        };
    }

    public String[] supportedTypes() {
        return new String[]{
            "group",
            "user"
        };
    }
    
}
