package com.lordralex.totalpermissions.commands.subcommands;

import com.lordralex.totalpermissions.TotalPermissions;
import org.bukkit.command.CommandSender;

/**
 *
 * @since 0.2
 * @author 1Rogue
 * @version 0.2
 */
public class UserCommand implements SubCommand {

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("User command executed.");
        if (args.length > 2) { // If there is an action command
            TotalPermissions.getPlugin().getCommandHandler().getActionHandler().onAction(sender, args);
        }
        else if (args.length == 1) {
            //List all users
        }
        else {
            //Error in index length, return help
        }
    }

    public String getName() {
        return "user";
    }

    public String getPerm() {
        return "totalpermissions.command.user";
    }

    public String[] getHelp() {
        return new String[]{
            "/ttp user <username> [actions..]",
            "User interface"
        };
    }
}
