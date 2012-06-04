package com.wolvencraft.MineReset.util;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;

public class Message
{
	public static void sendMessage(String message)
	{
		message = Util.parseColors(message);
		CommandSender sender = CommandManager.getSender();
		sender.sendMessage(message);
	}

	/**
	 * Sends a green-titled message to the command sender
	 * @param message A message to be sent
	 */
	public static void sendSuccess(String message)
	{
		CommandSender sender = CommandManager.getSender();
		String title = Language.getString("general.title-success");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		sender.sendMessage(ChatColor.GREEN + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a green-titled message to a player
	 * @param message A message to be sent
	 */
	public static void sendPlayerSuccess(Player player, String message)
	{
		String title = Language.getString("general.title-success");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		player.sendMessage(ChatColor.GREEN + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a red-titled message to the command sender
	 * @param message A message to be sent
	 */
	public static void sendError(String message)
	{
		CommandSender sender = CommandManager.getSender();
		String title = Language.getString("general.title-error");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		sender.sendMessage(ChatColor.RED + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a red-titled message to a player
	 * @param message A message to be sent
	 */
	public static void sendPlayerError(Player player, String message)
	{
		String title = Language.getString("general.title-error");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		player.sendMessage(ChatColor.RED + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Message thrown when the command used by a player is invalid
	 * Also sends a command into the log
	 * @param command Command used
	 */
	public static void sendInvalid(String[] args)
	{
		CommandSender sender = CommandManager.getSender();
		String title = Language.getString("general.title-error");
		String message = Language.getString("general.invalid-command");
		String command = "";
        for (String arg : args) {
            command = command + " " + arg;
        }
		log(sender.getName() + " sent an invalid command (/mine" + command + ")");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		sender.sendMessage(ChatColor.RED + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Message thrown when the user is denied of an action
	 * @param command Command used
	 */
	public static void sendDenied(String[] args)
	{
		CommandSender sender = CommandManager.getSender();
		String title = Language.getString("general.title-error");
		String message = Language.getString("general.access-denied");
		String command = "";
        for (String arg : args) {
            command = command + " " + arg;
        }
		log(sender.getName() + " was denied to use a command (/mine" + command + ")");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		sender.sendMessage(ChatColor.RED + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a message into the server log
	 * @param message A message to be sent
	 */
	public static void log(String message)
	{
		Logger log = Bukkit.getServer().getLogger();
		log.info(message);
	}

    /**
     * Sends a message into the server log if debug is enabled
     * @param message A message to be sent
     */
    public static void debug(String message)
    {
        if (Util.debugEnabled()) {
            Message.log(message);
        }
    }

    public static void sendPlayer(Player p, String message) {
        p.sendMessage(ChatColor.GREEN + "[" + Language.getString("general.title") + "]" + ChatColor.WHITE + message);
    }
}
