package com.wolvencraft.MineReset.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.wolvencraft.MineReset.config.Language;

public class Broadcast
{
	/**
	 * Broadcasts a green-titled message to all players
	 * @param message A message to be sent
	 */
	public static void sendMessage(String message)
	{
		message = Util.parseColors(message);
		Bukkit.getServer().broadcastMessage(message);
		return;
	}
	
	/**
	 * Broadcasts a green-titled message to all players
	 * This should be normally used just for the mine reset warnings
	 * @param message A message to be sent
	 */
	public static void sendSuccess(String message)
	{
		String title = Language.getString("general.title-success");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Broadcasts a red-titled message to all players
	 * This should not be used normally
	 * @param message A message to be sent
	 */
	public static void sendError(String message)
	{
		String title = Language.getString("general.title-error");
		title = Util.parseColors(title);
		message = Util.parseColors(message);
		Bukkit.getServer().broadcastMessage(ChatColor.RED + title + " " + ChatColor.WHITE + message);
	}
}
