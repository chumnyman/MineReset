package com.wolvencraft.MineReset.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Configuration;
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

	
	/**
	 * Parses a block specified for a material
	 * @param blockName Name of a block
	 * @return Block material if it exists, null if it does not.
	 */
	public static MaterialData getBlock(String blockName)
	{
		Message.debug("Parsing block: " + blockName);
		try
		{
			MaterialData block;
			String[] parts = blockName.split(":");
			if(parts.length > 2)
			{
				return null;
			}
			
			if(isNumeric(parts[0]))
			{
				block = new MaterialData (Material.getMaterial(Integer.parseInt(blockName)));
				if(parts.length == 2)
				{
					block.setData(Byte.parseByte(parts[1]));
				}
			}
			else
			{
				if(parts[0].equalsIgnoreCase("iron") || parts[0].equalsIgnoreCase("ironore"))
					parts[0] = "iron_ore";
				else if(blockName.equalsIgnoreCase("gold") || parts[0].equalsIgnoreCase("goldore"))
					parts[0] = "gold_ore";
				else if(blockName.equalsIgnoreCase("lapis") || parts[0].equalsIgnoreCase("lapisore"))
					parts[0] = "lapis_ore";
				else if(blockName.equalsIgnoreCase("diamond") || parts[0].equalsIgnoreCase("diamondore"))
					parts[0] = "diamond_ore";
				else if(blockName.equalsIgnoreCase("coal") || parts[0].equalsIgnoreCase("coalore"))
					parts[0] = "coal_ore";
				else if(blockName.equalsIgnoreCase("redstone") || parts[0].equalsIgnoreCase("redstoneore"))
					parts[0] = "redstone_ore";
				
				block = new MaterialData (Material.getMaterial(parts[0]));
				if(parts.length == 2)
				{
					block.setData(Byte.parseByte(parts[1]));
				}
			}
			
			return block;
			
		}
		catch(NumberFormatException nfe) { return null; }
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
	 * Parses the message for time and returns it in seconds
	 * @param message Input in the MINmSECs format
	 * @return
	 */
	public static int parseTime(String message)
	{
		int minPos = message.indexOf("m");
		int secPos = message.indexOf("s");
		int time = 0;
		
		if(minPos == -1 && secPos == -1)
		{
			try
			{
				time = Integer.parseInt(message);
				return -1;
			}
			catch(NumberFormatException nfe)
			{
				return -1;
			}
		}
		
		if(minPos != -1)
		{
			time += Integer.parseInt(message.substring(0, minPos));
			if(secPos != -1)
				time += Integer.parseInt(message.substring(minPos + 1, secPos));
		}
		else if(secPos != 1)
		{
			time += Integer.parseInt(message.substring(0, secPos));
		}
		else time = -1;
		return time;
	}
}