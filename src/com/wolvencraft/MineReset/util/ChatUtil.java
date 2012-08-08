package com.wolvencraft.MineReset.util;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;

public class ChatUtil
{
	/**
	 * Sends an untitled message to the command sender
	 * @param message A message to be sent
	 */
	public static void sendMessage(String message) {
		if(message == null) message = " == UNABLE TO FIND LANGUAGE DATA ==";
		message = Util.parseColors(message);
		CommandSender sender = CommandManager.getSender();
		if(sender == null) log(message);
		sender.sendMessage(ChatColor.WHITE + message);
	}
	
	/**
	 * Sends an untitled message directly to the player
	 * @param p Player to send the message to
	 * @param message Message to be sent
	 */
	public static void sendMessage(Player player, String message) {
		if(message == null) message = " == UNABLE TO FIND LANGUAGE DATA ==";
		message = Util.parseColors(message);
		player.sendMessage(ChatColor.WHITE + message);
	}
	
    /**
     * Sends a message to the command sender with an custom title
     * @param title Title of the message
     * @param message Message to be sent
     */
	public static void sendNote(String title, String message) {
		if(message == null) message = " == UNABLE TO FIND LANGUAGE DATA ==";
		CommandSender sender = CommandManager.getSender();
		if(sender == null) log(message);
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		message = ChatColor.GOLD + "[" + title + "] " + ChatColor.WHITE + message;
		sender.sendMessage(message);
	}
	
    /**
     * Sends a message to the command sender with an custom title
     * @param player Player to send the note to
     * @param title Title of the message
     * @param message Message to be sent
     */
	public static void sendNote(Player player, String title, String message) {
		if(message == null) message = " == UNABLE TO FIND LANGUAGE DATA ==";
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		message = ChatColor.GOLD + "[" + title + "] " + ChatColor.WHITE + message;
		player.sendMessage(message);
	}

    /**
     * Broadcasts a green-titled message to all players with a permission
     * @param message A message to be sent
     */
    public static void broadcast(String message) {
        if(message == null) message = " == UNABLE TO FIND LANGUAGE DATA ==";
        String title = Language.getString("general.title-success");
        title = Util.parseColors(title);
        message = Util.parseColors(message);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Util.playerHasPermission(p, "reset.broadcast." + p.getWorld().getName()) || p.isOp()) {
                p.sendMessage(ChatColor.GREEN + title + " " + ChatColor.WHITE + message);
            }
        }
        log(ChatColor.GREEN + title + " " + ChatColor.WHITE + message);
    }

	/**
	 * Sends a green-titled message to the command sender
	 * @param message A message to be sent
	 */
	public static void sendSuccess(String message) {
		if(message == null) message = " == UNABLE TO FIND LANGUAGE DATA ==";
		CommandSender sender = CommandManager.getSender();
		if(sender == null) log(message);
		String title = Language.getString("general.title-success");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		sender.sendMessage(ChatColor.GREEN + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a green-titled message to a player
	 * @param message A message to be sent
	 */
	public static void sendSuccess(Player player, String message) {
		if(message == null) message = " == UNABLE TO FIND LANGUAGE DATA ==";
		String title = Language.getString("general.title-success");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		player.sendMessage(ChatColor.GREEN + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a red-titled message to the command sender
	 * @param message A message to be sent
	 */
	public static void sendError(String message) {
		if(message == null) message = " == UNABLE TO FIND LANGUAGE DATA ==";
		CommandSender sender = CommandManager.getSender();
		if(sender == null) log(message);
		String title = Language.getString("general.title-error");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		sender.sendMessage(ChatColor.RED + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a red-titled message to a player
	 * @param message A message to be sent
	 */
	public static void sendError(Player player, String message) {
		if(message == null) message = " == UNABLE TO FIND LANGUAGE DATA ==";
		String title = Language.getString("general.title-error");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		player.sendMessage(ChatColor.RED + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Handles all the input errors
	 * @param error Error that has occurred
	 * @param args Arguments of the command, or null if extra is in place
	 * @param extra An extra variable, used for mine name and block name, or null if args is in place
	 */
	public static void sendInvalid(MineError error, String[] args, String extra) {
		CommandSender sender = CommandManager.getSender();
		if(sender == null) sender = CommandManager.getPlugin().getServer().getConsoleSender();
		String title = Language.getString("general.title-error");
        
        String message;
		
		if(error.equals(MineError.INVALID))
			message = Language.getString("error.command");
		else if(error.equals(MineError.ARGUMENTS))
			message = Language.getString("error.arguments");
		else if(error.equals(MineError.ACCESS))
			message = Language.getString("error.access");
		else if(error.equals(MineError.MINE_NAME))
			message = Language.getString("error.mine-name").replaceAll("%MINE%", extra);
		else if(error.equals(MineError.MINE_NOT_SELECTED))
			message = Language.getString("error.mine-not-selected");
		else if(error.equals(MineError.INVALID_BLOCK))
			message = Language.getString("error.block-does-not-exist").replaceAll("%BLOCK%", extra);
		else
			message = "An unknown error has occurred";

		sender.sendMessage(Util.parseColors(ChatColor.RED + title + " " + ChatColor.WHITE + message));
		
		String command = "";
        for (String arg : args) { command = command + " " + arg; }
    	if(error.equals(MineError.ACCESS)) log(sender.getName() + " was denied access to command: /mine" + command);
    	else log(sender.getName() + " sent an invalid command: /mine" + command);
	}
	
	/**
	 * Handles all the input errors<br />
	 * Cannot be used with <b>MineError.MINE_NAME</b> an <b>MineError.INVALID_BLOCK</b>
	 * @param error Error that has occurred
	 * @param args Arguments of the command
	 */
	public static void sendInvalid(MineError error, String[] args) {
		sendInvalid(error, args, null);
		return; 
	}
	
    /**
     * Sends a message into the server log if debug is enabled
     * @param message A message to be sent
     */
    public static void debug(String message) {
        if (Util.debugEnabled()) ChatUtil.log(message);
    }
    
    /**
     * Returns the logger
     * @return Logger
     */
	public static Logger getLogger() {
		return Logger.getLogger("MineReset");
	}
	
	/**
	 * Sends a message into the server log
	 * @param message A message to be sent
	 */
	public static void log(String message) {
		Logger.getLogger("MineReset").info(message);
	}
	
	public static void formatHelp(String command, String arguments, String description, String node) {
		CommandSender sender = CommandManager.getSender();
		if(!arguments.equalsIgnoreCase("")) arguments = " " + arguments;
		if(Util.hasPermission(node) || node.equals(""))
			sender.sendMessage(ChatColor.GOLD + "/mine " + command + ChatColor.GRAY + arguments + ChatColor.WHITE + " " + description);
		return;
	}
	
	public static void formatHelp(String command, String arguments, String description) {
		formatHelp(command, arguments, description, "");
		return;
	}
	
	public static void formatHeader(int padding, String name) {
		CommandSender sender = CommandManager.getSender();
		String spaces = "";
		for(int i = 0; i < padding; i++)
			spaces = spaces + " ";
		sender.sendMessage(spaces + "-=[ " + ChatColor.BLUE + name + ChatColor.WHITE + " ]=-");
	}
	
	public static void formatMessage(String message) {
		CommandSender sender = CommandManager.getSender();
		sender.sendMessage(" " + message);
	}
}
