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
				block = new MaterialData(Material.getMaterial(Integer.parseInt(parts[0])));
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
				
				Message.debug(parts[0]);
				Message.debug(Material.getMaterial(parts[0].toUpperCase()).name());
				block = new MaterialData(Material.getMaterial(parts[0].toUpperCase()));
			}
			
			parts[0] = block.getItemTypeId() + "";
			
			if(parts.length == 2)
			{
				if(!isNumeric(parts[1]))
				{
					parts[1] = parseMetadata(parts, false);
				}
				
				block.setData(Byte.parseByte(parts[1]));
			}
			
			return block;
			
		}
		catch(NumberFormatException nfe) { return null; }
	}
	
	/**
	 * Returns the data of the block specified
	 * @param parts Block name
	 * @return metadata of a block
	 */
	public static String parseMetadata(String[] parts, boolean recursive)
	{
		if(recursive)
		{
			switch(Integer.parseInt(parts[0]))
			{
				case 5:
				case 6:
				case 17:
				case 18:
				{
					if(Integer.parseInt(parts[1]) == 1) parts[1] = "pine";
					else if(Integer.parseInt(parts[1]) == 2) parts[1] = "birch";
					else if(Integer.parseInt(parts[1]) == 3) parts[1] = "jungle";
					else parts[1] = "oak";
					
					break;
				}
				case 35:
				{
					if(Integer.parseInt(parts[1]) == 1) parts[1] = "orange";
					else if(Integer.parseInt(parts[1]) == 2) parts[1] = "magenta";
					else if(Integer.parseInt(parts[1]) == 3) parts[1] = "lightblue";
					else if(Integer.parseInt(parts[1]) == 4) parts[1] = "yellow";
					else if(Integer.parseInt(parts[1]) == 5) parts[1] = "lime";
					else if(Integer.parseInt(parts[1]) == 6) parts[1] = "pink";
					else if(Integer.parseInt(parts[1]) == 7) parts[1] = "gray";
					else if(Integer.parseInt(parts[1]) == 8) parts[1] = "lightgray";
					else if(Integer.parseInt(parts[1]) == 9) parts[1] = "cyan";
					else if(Integer.parseInt(parts[1]) == 10) parts[1] = "purple";
					else if(Integer.parseInt(parts[1]) == 11) parts[1] = "blue";
					else if(Integer.parseInt(parts[1]) == 12) parts[1] = "brown";
					else if(Integer.parseInt(parts[1]) == 13) parts[1] = "green";
					else if(Integer.parseInt(parts[1]) == 14) parts[1] = "red";
					else if(Integer.parseInt(parts[1]) == 15) parts[1] = "black";
					else parts[1] = "white";
					
					break;
				}
				case 84:
				{
					if(Integer.parseInt(parts[1]) == 1) parts[1] = "gold disk";
					else if(Integer.parseInt(parts[1]) == 2) parts[1] = "green disk";
					else if(Integer.parseInt(parts[1]) == 3) parts[1] = "orange disk";
					else if(Integer.parseInt(parts[1]) == 4) parts[1] = "red disk";
					else if(Integer.parseInt(parts[1]) == 5) parts[1] = "lime disk";
					else if(Integer.parseInt(parts[1]) == 6) parts[1] = "purple disk";
					else if(Integer.parseInt(parts[1]) == 7) parts[1] = "violet disk";
					else if(Integer.parseInt(parts[1]) == 8) parts[1] = "black disk";
					else if(Integer.parseInt(parts[1]) == 9) parts[1] = "white disk";
					else if(Integer.parseInt(parts[1]) == 10) parts[1] = "sea green disk";
					else if(Integer.parseInt(parts[1]) == 11) parts[1] = "broken disk";
					else parts[1] = "";
					
					break;
				}
			}
		}
		else
		{
			if(parts[0].equalsIgnoreCase("5") || parts[0].equalsIgnoreCase("6") || parts[0].equalsIgnoreCase("17") || parts[0].equalsIgnoreCase("18"))
			{
				if(parts[1].equalsIgnoreCase("dark") || parts[1].equalsIgnoreCase("pine") || parts[1].equalsIgnoreCase("spruce")) parts[1] = 1 + "";
				else if(parts[1].equalsIgnoreCase("birch")) parts[1] = 2 + "";
				else if(parts[1].equalsIgnoreCase("jungle")) parts[1] = 3 + "";
				else parts[1] = 0 + "";
			}
			else if(parts[0].equalsIgnoreCase("35"))
			{
				if(parts[1].equalsIgnoreCase("orange")) parts[1] = 1 + "";
				else if(parts[1].equalsIgnoreCase("magenta")) parts[1] = 2 + "";
				else if(parts[1].equalsIgnoreCase("lightblue")) parts[1] = 3 + "";
				else if(parts[1].equalsIgnoreCase("yellow")) parts[1] = 4 + "";
				else if(parts[1].equalsIgnoreCase("lime")) parts[1] = 5 + "";
				else if(parts[1].equalsIgnoreCase("pink")) parts[1] = 6 + "";
				else if(parts[1].equalsIgnoreCase("gray")) parts[1] = 7 + "";
				else if(parts[1].equalsIgnoreCase("lightgray")) parts[1] = 8 + "";
				else if(parts[1].equalsIgnoreCase("cyan")) parts[1] = 9 + "";
				else if(parts[1].equalsIgnoreCase("purple")) parts[1] = 10 + "";
				else if(parts[1].equalsIgnoreCase("blue")) parts[1] = 11 + "";
				else if(parts[1].equalsIgnoreCase("brown")) parts[1] = 12 + "";
				else if(parts[1].equalsIgnoreCase("green")) parts[1] = 13 + "";
				else if(parts[1].equalsIgnoreCase("red")) parts[1] = 14 + "";
				else if(parts[1].equalsIgnoreCase("black")) parts[1] = 15 + "";
				else parts[1] = 0 + "";
			}
			else if(parts[0].equalsIgnoreCase("84"))
			{
				if(parts[1].equalsIgnoreCase("gold")) parts[1] = 1 + "";
				else if(parts[1].equalsIgnoreCase("green")) parts[1] = 2 + "";
				else if(parts[1].equalsIgnoreCase("orange")) parts[1] = 3 + "";
				else if(parts[1].equalsIgnoreCase("red")) parts[1] = 4 + "";
				else if(parts[1].equalsIgnoreCase("lime")) parts[1] = 5 + "";
				else if(parts[1].equalsIgnoreCase("purple")) parts[1] = 6 + "";
				else if(parts[1].equalsIgnoreCase("violet")) parts[1] = 7 + "";
				else if(parts[1].equalsIgnoreCase("black")) parts[1] = 8 + "";
				else if(parts[1].equalsIgnoreCase("white")) parts[1] = 9 + "";
				else if(parts[1].equalsIgnoreCase("seagreen")) parts[1] = 10 + "";
				else if(parts[1].equalsIgnoreCase("broken")) parts[1] = 11 + "";
				else parts[1] = 0 + "";
			}
		}
		return parts[1];
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
		String[] parts = message.split(":");
		int time = 0;
		
		try
		{
			if(parts.length < 2)
			{
				return (Integer.parseInt(message) * 60);
			}
			else if(parts.length > 2)
			{
				return -1;
			}
			
			time = (Integer.parseInt(parts[0]) * 60) + Integer.parseInt(parts[1]);
			return time;
		}
		catch(NumberFormatException nfe)
		{
			return -1;
		}
		
	}
}