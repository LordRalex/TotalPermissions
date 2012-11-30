package com.lordralex.totalpermissions.commands.subcommands;

import com.lordralex.totalpermissions.TotalPermissions;
import com.lordralex.totalpermissions.permission.PermissionUser;
import org.bukkit.command.CommandSender;

/**
 * @since 1.0
 * @author Joshua
 * @version 1.0
 */
public class DebugCommand implements SubCommand {

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No target specified, for help, use /totalperms help debug");
            return;
        }
        PermissionUser target = TotalPermissions.getManager().getUser(args[0]);
        if (target == null) {
            sender.sendMessage("Target user (" + args[0] + ") was not found");
            return;
        }
        boolean newState = true;
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("true")) {
                newState = true;
            } else if (args[1].equalsIgnoreCase("toogle")) {
                newState = !target.getDebugState();
            } else {
                newState = false;
            }
        }
        target.setDebug(newState);
        if (target.getDebugState()) {
            sender.sendMessage("Debug turned on for " + target.getName());
        } else {
            sender.sendMessage("Debug turned off for " + target.getName());
        }
    }

    @Override
    public String getPerm() {
        return "totalpermissions.command.debug";
    }

    public String[] getHelp() {
        return new String[]{
                    "Usage: /totalperms debug <user name>",
                    "This turns on debug mode for a user, which shows perms "
                    + "needed to use commands by them and the permissions they have"
                };
    }
}
