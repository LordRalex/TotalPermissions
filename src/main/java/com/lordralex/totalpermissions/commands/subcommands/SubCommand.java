package com.lordralex.totalpermissions.commands.subcommands;

import org.bukkit.command.CommandSender;

/**
 *
 * @author Joshua
 */
public interface SubCommand {

    /**
     * Returns the command's name. When used, it is the /par [name].
     *
     * @return Name of the command
     */
    public abstract String getName();

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

    public abstract String[] getHelp();
}
