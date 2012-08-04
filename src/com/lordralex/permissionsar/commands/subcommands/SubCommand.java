/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lordralex.permissionsar.commands.subcommands;

import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public abstract class SubCommand {

    /**
     * Returns the command's name. When used, it is the /par [name].
     *
     * @return Name of the command
     */
    public abstract String getName();

    /**
     * Returns a String showing how to use this command.
     *
     * @return The help statement
     */
    public abstract String getHelp();

    /**
     * Executes the command. Only the args and sender are needed.
     */
    public abstract void execute(CommandSender sender, String[] args);
    
    /**
     * Returns the permission to use this command.
     * 
     * @return The permission 
     */
    public abstract String getPerm();
}
