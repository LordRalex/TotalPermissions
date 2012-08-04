package com.lordralex.permissionsar;


// Decided I liked the other idea better. I just commented this in case we want to steal a couple of methods.

/**
 * @deprecated Use the CommandHandler and abstract class
 */
public class CommandHandler {
    
}


/*import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public abstract class CommandHandler implements CommandExecutor {
    
    public static PermissionsAR plugin;
    public static Configuration config;

    @Override
    public abstract boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args);

    public abstract String getName();

    public abstract String getHelp();
    
    public static void setup(PermissionsAR aPlugin) {
        plugin = aPlugin;
        /*PluginClass var = new PluginClass();
        plugin.getCommand(var.getName()).setExecutor(var);*//*
    }
    
    
    
    // All utils crap below, thought about moving to a Utils.class but not sure yet, as all commands just extend this class anyhow.
    
    
    public String match(String name) {
        List<Player> matches = Bukkit.matchPlayer(name);
		if(matches.size() >= 1) { return (("Perhaps you meant " + matches.get(0).getName()) + "?"); }
		else { return ("No close matches found.");}
    }

    public static final Logger logger = Bukkit.getLogger();
    public void out(String message) {
  	  /*PluginDescriptionFile pdfFile = plugin.getDescription();
  	  logger.info("[" + pdfFile.getName() + "] " + message);*//*
        logger.info(message);
  	  }
    public void outc(CommandSender cs, String message) {
    	if (cs instanceof Player) {
    		System.out.println("[PLAYER COMMAND] " + cs.getName() + ": /" + message);
    	}
    }
    public void output(CommandSender cs, String message) {
    	if (cs instanceof Player) {
    		cs.sendMessage(message.replaceAll("&([0-9a-f])", "\u00A7$1"));
    	}
    	else {
    	  	logger.info(message.replaceAll("&([0-9a-f])", ""));
    	}
    }
    public void output(Player p, String s) {
    		p.sendMessage(s.replaceAll("&([0-9a-f])", "\u00A7$1"));
    }
    public void formatTitle(CommandSender s, String title) {
		String line = "------------------------------------------------------------";
		int pivot = line.length() / 2;
		String center =  "[ " + title + " ]";
		String out = line.substring(0, pivot - center.length() / 2);
		out += center + line.substring(pivot + center.length() / 2);
		output(s, out);
	}
}*/