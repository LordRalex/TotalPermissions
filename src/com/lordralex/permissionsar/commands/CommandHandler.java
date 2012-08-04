package com.lordralex.permissionsar.commands;

import com.lordralex.permissionsar.Configuration;
import com.lordralex.permissionsar.PermissionsAR;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @version 1.0
 * @author Joshua
 * @since 1.0
 */
public class CommandHandler implements CommandExecutor {
    
    public static PermissionsAR plugin;
    public static Configuration config;

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        //TODO: 
        /*
         * What we will do here is just check args[0] is a sub-command, then 
         * execute that command's class in the 
         * com.lordralex.permissionsar.commands.subcommands class
         */
        return false;
    }
}
