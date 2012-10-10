package com.lordralex.permissionsar.commands;

import com.lordralex.permissionsar.PermissionsAR;
import com.lordralex.permissionsar.commands.subcommands.*;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public final class CommandHandler implements CommandExecutor, SubCommand {

    private Map<String, SubCommand> commands = new HashMap<String, SubCommand>();

    public CommandHandler() {
        //Create and store all commands to the map here
        HelpCommand help = new HelpCommand();
        commands.put(help.getName().toLowerCase().trim(), help);
        DebugCommand debug = new DebugCommand();
        commands.put(debug.getName().toLowerCase().trim(), debug);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        String subCommand;
        if (args.length >= 1) {
            subCommand = args[0];
        } else {
            subCommand = "help";
        }
        SubCommand executor = commands.get(subCommand.toLowerCase());
        if (executor == null) {
            sender.sendMessage("No command found, use /par help for command list");
            return true;
        }
        int length = args.length - 1;
        if (length < 0) {
            length = 0;
        }
        String[] newArgs = new String[length];
        for (int i = 0; i < newArgs.length; i++) {
            newArgs[i] = args[i + 1];
        }
        if (sender.hasPermission(executor.getPerm())) {
            executor.execute(sender, args);
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You cannot use that command");
        }
        return true;
    }

    public String getName() {
        return "";
    }

    public void execute(CommandSender sender, String[] args) {
        onCommand(sender, PermissionsAR.getPlugin().getCommand("par"), "par", args);
    }

    public String getPerm() {
        return "permissionsar.command";
    }
}
