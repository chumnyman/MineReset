package com.wolvencraft.MineReset.cmd;

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
		
		if(player.hasPermission("ffa." + node))
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
		
		if(player.hasPermission("ffa." + node))
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
		String title = Language.getString("general.title");
		sender.sendMessage(ChatColor.GREEN + "[" + title + "] " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a green-titled message to a player
	 * @param message A message to be sent
	 */
	public static void sendPlayerSuccess(Player player, String message)
	{
		String title = Language.getString("general.title");
		player.sendMessage(ChatColor.GREEN + "[" + title + "] " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a red-titled message to the command sender
	 * @param message A message to be sent
	 */
	public static void sendError(String message)
	{
		CommandSender sender = CommandManager.getSender();
		String title = Language.getString("general.title");
		sender.sendMessage(ChatColor.RED + "[" + title + "] " + ChatColor.WHITE + message);
	}
	
	/**
	 * Sends a red-titled message to a player
	 * @param message A message to be sent
	 */
	public static void sendPlayerError(Player player, String message)
	{
		String title = Language.getString("general.title");
		player.sendMessage(ChatColor.RED + "[" + title + "] " + ChatColor.WHITE + message);
	}
	
	/**
	 * Message thrown when the command used by a player is invalid
	 * Also sends a command into the log
	 * @param command Command used
	 */
	public static void sendInvalid(String[] args)
	{
		CommandSender sender = CommandManager.getSender();
		String title = Language.getString("general.title");
		String message = Language.getString("general.invalid-command");
		String command = "";
		for(int i = 0; i < args.length; i++)
			command = command + " " + args[i];
		log(sender.getName() + " sent an invalid command (/mine" + command + ")");
		sender.sendMessage(ChatColor.RED + "[" + title + "] " + ChatColor.WHITE + message);
	}
	
	/**
	 * Message thrown when the user is denied of an action
	 * @param command Command used
	 */
	public static void sendDenied(String[] args)
	{
		CommandSender sender = CommandManager.getSender();
		String title = Language.getString("general.title");
		String message = Language.getString("general.access-denied");
		String command = "";
		for(int i = 0; i < args.length; i++)
			command = command + " " + args[i];
		log(sender.getName() + " was denied to use a command (/mine" + command + ")");
		sender.sendMessage(ChatColor.RED + "[" + title + "] " + ChatColor.WHITE + message);
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
		String title = Language.getString("general.title");
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[" + title + "] " + ChatColor.WHITE + message);
	}
	
	/**
	 * Broadcasts a red-titled message to all players
	 * This should not be used normally
	 * @param message A message to be sent
	 */
	public static void broadcastError(String message)
	{
		String title = Language.getString("general.title");
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "[" + title + "] " + ChatColor.WHITE + message);
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
				material = Material.getMaterial(Integer.parseInt(blockName));
			}
			else
			{
				if(blockName.equalsIgnoreCase("iron"))
					blockName = "iron_ore";
				else if(blockName.equalsIgnoreCase("gold"))
					blockName = "gold_ore";
				else if(blockName.equalsIgnoreCase("lapis"))
					blockName = "lapis_ore";
				else if(blockName.equalsIgnoreCase("diamond"))
					blockName = "diamond_ore";
				else if(blockName.equalsIgnoreCase("coal"))
					blockName = "coal_ore";
				else if(blockName.equalsIgnoreCase("redstone"))
					blockName = "redstone_ore";
				
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
	 * Replaces the variables in the string with their values
	 * @param str String to parse
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
		if((loc.getX() > x[0] && loc.getX() < x[1])
				&& (loc.getY() > y[0] && loc.getY() < y[1])
				&& (loc.getZ() > z[0] && loc.getZ() < z[1]))
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
}
