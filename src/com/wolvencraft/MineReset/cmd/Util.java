package com.wolvencraft.MineReset.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;

public class Util
{
	/**
	 * Checks if the command sender has a permission.
	 * @param node The permission node
	 * @return true is the sender has a permission, false if he does not
	 */
	public static boolean senderHasPermission(String node)
	{
		CommandSender sender = CommandManager.getSender();
		boolean usePermissions = Configuration.getBoolean("configuration.use-permissions");
		
		// Console always has the permissions
		Player player;
		if (isPlayer()) player = (Player) sender;
		else return true;
		
		// If permissions are not used, check for op
		if(!usePermissions)
		{
			if(player.isOp()) return true;
			else return false;
		}
		
		if(player.hasPermission("minereset." + node))
			return true;
		
		return false;
	}
	

	/**
	 * Checks if the command sender has a permission.
	 * @param node The permission node
	 * @return true is the sender has a permission, false if he does not
	 */
	public static boolean playerHasPermission(Player player, String node)
	{
		boolean usePermissions = Configuration.getBoolean("configuration.use-permissions");
		
		if(!usePermissions)
		{
			if(player.isOp()) return true;
			else return false;
		}
		
		if(player.hasPermission("minereset." + node))
			return true;
		
		return false;
	}
	
	/**
	 * Checks if the command sender is a player or a console
	 * @return true if sender is a player, false if not
	 */
	public static boolean isPlayer()
	{
		CommandSender sender = CommandManager.getSender();
		
		if(sender instanceof Player) return true;
		else return false;
	}
	
	/**
	 * Checks if debug mode is enabled in the config
	 * @return true if debug is enabled, false if it's not
	 */
	public static boolean debugEnabled()
	{
		if(Configuration.getBoolean("configuration.debug-mode")) return true;
		else return false;
	}
	
	/**
	 * Sends a message back to the command sender
	 * @param message A message to be sent
	 */
	public static void sendMessage(String message)
	{
		message = parseColors(message);
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
		title = parseColors(title);
		message = parseColors(message);
		sender.sendMessage(ChatColor.GREEN + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a green-titled message to a player
	 * @param message A message to be sent
	 */
	public static void sendPlayerSuccess(Player player, String message)
	{
		String title = Language.getString("general.title-success");
		title = parseColors(title);
		message = parseColors(message);
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
		title = parseColors(title);
		message = parseColors(message);
		sender.sendMessage(ChatColor.RED + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a red-titled message to a player
	 * @param message A message to be sent
	 */
	public static void sendPlayerError(Player player, String message)
	{
		String title = Language.getString("general.title-error");
		title = parseColors(title);
		message = parseColors(message);
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
		for(int i = 0; i < args.length; i++)
			command = command + " " + args[i];
		log(sender.getName() + " sent an invalid command (/mine" + command + ")");
		title = parseColors(title);
		message = parseColors(message);
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
		for(int i = 0; i < args.length; i++)
			command = command + " " + args[i];
		log(sender.getName() + " was denied to use a command (/mine" + command + ")");
		title = parseColors(title);
		message = parseColors(message);
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
	 * Broadcasts a green-titled message to all players
	 * @param message A message to be sent
	 */
	public static void broadcastMessage(String message)
	{
		message = parseColors(message);
		Bukkit.getServer().broadcastMessage(message);
		return;
	}
	
	/**
	 * Broadcasts a green-titled message to all players
	 * This should be normally used just for the mine reset warnings
	 * @param message A message to be sent
	 */
	public static void broadcastSuccess(String message)
	{
		String title = Language.getString("general.title-success");
		title = parseColors(title);
		message = parseColors(message);
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Broadcasts a red-titled message to all players
	 * This should not be used normally
	 * @param message A message to be sent
	 */
	public static void broadcastError(String message)
	{
		String title = Language.getString("general.title-error");
		title = parseColors(title);
		message = parseColors(message);
		Bukkit.getServer().broadcastMessage(ChatColor.RED + title + " " + ChatColor.WHITE + message);
	}
	
	/**
	 * Parses a block specified for a material
	 * @param blockName Name of a block
	 * @return Block material if it exists, null if it does not.
	 */
	public static int getBlockId(String blockName)
	{
		try
		{
			Material material;
			if(isNumeric(blockName))
			{
				if(Integer.parseInt(blockName) == 10 || Integer.parseInt(blockName) == 11 || Integer.parseInt(blockName) == 326)
					material = Material.WATER_BUCKET;
				else if(Integer.parseInt(blockName) == 10 || Integer.parseInt(blockName) == 11 || Integer.parseInt(blockName) == 327)
					material = Material.LAVA_BUCKET;
				else
					material = Material.getMaterial(Integer.parseInt(blockName));
			}
			else
			{
				if(blockName.equalsIgnoreCase("iron") || blockName.equalsIgnoreCase("ironore"))
					blockName = "iron_ore";
				else if(blockName.equalsIgnoreCase("gold") || blockName.equalsIgnoreCase("goldore"))
					blockName = "gold_ore";
				else if(blockName.equalsIgnoreCase("lapis") || blockName.equalsIgnoreCase("lapisore"))
					blockName = "lapis_ore";
				else if(blockName.equalsIgnoreCase("diamond") || blockName.equalsIgnoreCase("diamondore"))
					blockName = "diamond_ore";
				else if(blockName.equalsIgnoreCase("coal") || blockName.equalsIgnoreCase("coalore"))
					blockName = "coal_ore";
				else if(blockName.equalsIgnoreCase("redstone") || blockName.equalsIgnoreCase("redstoneore"))
					blockName = "redstone_ore";
				
				if(blockName.equalsIgnoreCase("lava") || blockName.equalsIgnoreCase("lava_bucket") || blockName.equalsIgnoreCase("lavabucket"))
					material = Material.WATER_BUCKET;
				else if(blockName.equalsIgnoreCase("water") || blockName.equalsIgnoreCase("water_bucket") || blockName.equalsIgnoreCase("waterbucket"))
					material = Material.LAVA_BUCKET;
				else
					material = Material.matchMaterial(blockName);
			}
			
			return material.getId();
			
		}
		catch(NumberFormatException nfe) { return -1; }
	}
	
	/**
	 * Checks if a string is numeric
	 * @param str String String to be checked
	 * @return boolean True if a string is numeric
	 */
	@SuppressWarnings("unused")
	public static boolean isNumeric(String str)  
	{  
	  try
	  { double d = Double.parseDouble(str); }
	  catch(NumberFormatException nfe)  
	  { return false; }  
	  return true;  
	}
	

	/**
	 * Checks if the mine exists
	 * @param name Name of the mine being checked
	 * @return True if the mine exists, False if it does not
	 */
	public static boolean mineExists(String name)
	{
		List<String> mineList = Regions.getList("data.list-of-mines");
		if(mineList.indexOf(name) == -1) return false;
		else return true;
	}
	
	/**
	 * Replaces the specified value in the string provided with the new value
	 * @param str String to parse
	 * @param replaceFrom Value to be replaced
	 * @param replaceTo Value to be substituted
	 * @return A new String with necessary values substituted
	 */
	public static String parseString(String str, String replaceFrom, String replaceTo)
	{
		str = str.replaceAll(replaceFrom, replaceTo);
		return str;
	}
	
	/**
	 * Replace a list of specified values in the string with the new values
	 * @param str String to parse
	 * @param replaceFrom Values to be replaced
	 * @param replaceTo Values to be substituted
	 * @return
	 */
	public static String parseStringMultiple(String str, List<String> replaceFrom, List<String> replaceTo)
	{
		if(replaceFrom.size() != replaceTo.size())
			return null;
		
		for(int i = 0; i < replaceFrom.size(); i++)
		{
			str = str.replaceAll(replaceFrom.get(i), replaceTo.get(i));
		}
		
		return str;
	}
	
	/**
	 * Replaces the variables in the string with their values
	 * @param str String to be parsed
	 * @param mineName Name of the mine
	 * @return Parsed string
	 */
	public static String parseVars(String str, String mineName)
	{
		String displayName = Regions.getString("mines." + mineName + ".display-name");
		boolean auto = Regions.getBoolean("mines." + mineName + ".reset.auto.reset");
		
		if(displayName.equals("")) displayName = mineName;
		str = parseString(str, "%MINE%", mineName);
		str = parseString(str, "%MINENAME%", displayName);
		
		if(auto)
		{
			int min = Regions.getInt("mines." + mineName + ".reset.auto.data.min");
			int sec = Regions.getInt("mines." + mineName + ".reset.auto.data.sec");
			String time = ChatColor.GOLD + "" + min + ChatColor.WHITE + " minutes, " + ChatColor.GOLD + "" + sec + ChatColor.WHITE + " seconds";
			int next = Regions.getInt("mines." + mineName + ".reset.auto.reset-time");
			
			str = parseString(str, "%MIN%", min + "");
			str = parseString(str, "%SEC%", sec + "");
			str = parseString(str, "%NEXT%", next + "");
			str = parseString(str, "%TIME%", time);
		}
		
		str = parseColors(str);
		
		return str;
	}
	
	/**
	 * Replaces the color codes with colors
	 * @param str String to be parsed
	 * @return Parsed string
	 */
	public static String parseColors(String str)
	{
		str = parseString(str, "&0", "" + ChatColor.BLACK);
		str = parseString(str, "&1", "" + ChatColor.DARK_BLUE);
		str = parseString(str, "&2", "" + ChatColor.DARK_GREEN);
		str = parseString(str, "&3", "" + ChatColor.DARK_AQUA);
		str = parseString(str, "&4", "" + ChatColor.DARK_RED);
		str = parseString(str, "&5", "" + ChatColor.DARK_PURPLE);
		str = parseString(str, "&6", "" + ChatColor.GOLD);
		str = parseString(str, "&7", "" + ChatColor.GRAY);
		str = parseString(str, "&8", "" + ChatColor.DARK_GRAY);
		str = parseString(str, "&9", "" + ChatColor.BLUE);
		str = parseString(str, "&a", "" + ChatColor.GREEN);
		str = parseString(str, "&b", "" + ChatColor.AQUA);
		str = parseString(str, "&c", "" + ChatColor.RED);
		str = parseString(str, "&d", "" + ChatColor.LIGHT_PURPLE);
		str = parseString(str, "&e", "" + ChatColor.YELLOW);
		str = parseString(str, "&f", "" + ChatColor.WHITE);
		

		str = parseString(str, "&k", "" + ChatColor.MAGIC);
		str = parseString(str, "&l", "" + ChatColor.BOLD);
		str = parseString(str, "&m", "" + ChatColor.STRIKETHROUGH);
		str = parseString(str, "&n", "" + ChatColor.UNDERLINE);
		str = parseString(str, "&o", "" + ChatColor.ITALIC);
		str = parseString(str, "&r", "" + ChatColor.RESET);
		return str;
	}
	
	/**
	 * Determines if a specific player is in the mine
	 * @param player Player
	 * @param mineName Name of the mine
	 * @return true if a player is in the mine, false if he is not
	 */
	public static boolean playerInTheMine(Player player, String mineName)
	{
		int[] x = {Regions.getInt("mines." + mineName + ".coordinates.pos0.x"), Regions.getInt("mines." + mineName + ".coordinates.pos1.x")};
		int[] y = {Regions.getInt("mines." + mineName + ".coordinates.pos0.y"), Regions.getInt("mines." + mineName + ".coordinates.pos1.y")};
		int[] z = {Regions.getInt("mines." + mineName + ".coordinates.pos0.z"), Regions.getInt("mines." + mineName + ".coordinates.pos1.z")};
		Location loc = player.getLocation();
		if((loc.getBlockX() > x[0] && loc.getBlockX() < x[1])
				&& (loc.getBlockY() > y[0] && loc.getBlockY() < y[1])
				&& (loc.getBlockZ() > z[0] && loc.getBlockZ() < z[1]))
				{
					return true;
				}
				else return false;
	}
	
	/**
	 * Teleports a player to the mine specified
	 * @param player Player to be teleported
	 * @param mienName The name of the mine
	 */
	public static void warpToMine(Player player, String mineName)
	{
		String newLocWorld = Regions.getString("mines." + mineName + ".coordinates.world");
		double[] coords = {
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.x"),
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.y"),
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.z"),
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.yaw"),
				Regions.getDouble("mines." + mineName + ".coordinates.pos2.pitch")
		};
		Location newLoc = new Location(Bukkit.getServer().getWorld(newLocWorld), coords[0], coords[1], coords[2], (float)coords[3], (float)coords[4]);
		
		player.teleport(newLoc);
	}
	
	public static List<String> getSortedList(String mineName)
	{
		List<String> itemList = Regions.getList("mines." + mineName + ".materials.blocks");
		List<String> weightList = Regions.getList("mines." + mineName + ".materials.weights");
		List<String> finalList = new ArrayList<String>(itemList.size());
		
		String tempItem = null, tempWeight = null;
		for(int j = weightList.size() - 1; j > 0 ; j--)
		{
			for(int i = 0; i < j; i++)
			{
				if(Double.parseDouble(weightList.get(i + 1)) > Double.parseDouble(weightList.get(i)))
				{
					tempItem = itemList.get(i);
					tempWeight = weightList.get(i);
	
					itemList.set(i, itemList.get(i + 1));
					weightList.set(i, weightList.get(i + 1));
	
					itemList.set(i + 1, tempItem);
					weightList.set(i + 1, tempWeight);
				}
				
			}
		}
		
		for(int i = 0; i < itemList.size(); i++)
		{
			String blockName = Material.getMaterial(Integer.parseInt(itemList.get(i))).toString().toLowerCase().replace("_", " ");
			String blockWeight = null;
			if(Double.parseDouble(weightList.get(i)) < 10)
				blockWeight = " " + weightList.get(i) + "%";
			else
				blockWeight = weightList.get(i) + "%";
			
			if(Double.parseDouble(weightList.get(i)) != 0)
				finalList.add(" - " + blockWeight + " " + ChatColor.GREEN + blockName);
		}
		
		return finalList;
		
	}
}
