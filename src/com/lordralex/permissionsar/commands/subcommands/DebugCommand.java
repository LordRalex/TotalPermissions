package com.lordralex.permissionsar.commands.subcommands;

import com.lordralex.permissionsar.PermissionsAR;
import com.lordralex.permissionsar.permission.PermissionUser;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public class DebugCommand implements SubCommand {

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("No target specified, for help, use /par help debug");
            return;
        }
        PermissionUser target = PermissionsAR.getManager().getUser(args[0]);
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
        return "permissionsar.command.debug";
    }
}
