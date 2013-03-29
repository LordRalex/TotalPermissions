package com.lordralex.totalpermissions.commands.subcommands;

import com.lordralex.totalpermissions.TotalPermissions;
import com.lordralex.totalpermissions.permission.PermissionUser;
import org.bukkit.command.CommandSender;

public class DebugCommand implements SubCommand {

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No target specified, for help, use /totalperms help debug");
            return;
        }
        PermissionUser target = TotalPermissions.getPlugin().getManager().getUser(args[1]);
        if (target == null) {
            sender.sendMessage("Target user (" + args[0] + ") was not found");
            return;
        }
        boolean newState = false;
        if (args.length == 2) {
            if ((args[1].equalsIgnoreCase("on")) || (args[1].equalsIgnoreCase("enable")) || (args[1].equalsIgnoreCase("true"))) {
                newState = true;
            }
            else if (args[1].equalsIgnoreCase("toggle")) {
                newState = !target.getDebugState();
            }
            else {
                newState = false;
            }
        }
        target.setDebug(newState);
        if (target.getDebugState()) {
            sender.sendMessage("Debug turned on for " + target.getName());
        }
        else {
            sender.sendMessage("Debug turned off for " + target.getName());
        }
    }
    
    public String getName() {
        return "debug";
    }

    public String getPerm() {
        return "totalpermissions.cmd.debug";
    }

    public String[] getHelp() {
        return new String[] {
            "/ttp debug <username>",
            "Turns on debug mode for a user"
        };
    }
}