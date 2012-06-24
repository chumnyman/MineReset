package com.wolvencraft.MineReset.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.mine.Mine;

public class Util
{
	/**
	 * Checks if the command sender has a permission.
	 * @param node The permission node
	 * @return true is the sender has a permission, false if he does not
	 */
	public static boolean hasPermission(String node) {
		
		// Console always has the permissions
		Player player;
		if (isPlayer()) player = (Player) CommandManager.getSender();
		else return true;
		
		// If permissions are not used, check for op
		if(!Configuration.getBoolean("configuration.use-permissions")) {
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
	public static boolean playerHasPermission(Player player, String node) {
		
		if(!Configuration.getBoolean("configuration.use-permissions")) {
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
	 * Parses a block specified for a material
	 * @param blockName Name of a block
	 * @return Block material if it exists, null if it does not.
	 */
	public static MaterialData getBlock(String blockName) {
		ChatUtil.debug("Parsing block: " + blockName);
		try
		{
			String[] parts = blockName.split(":");
			if(parts.length > 2) return null;
			
			MaterialData block;
			if(isNumeric(parts[0])) block = new MaterialData(Material.getMaterial(Integer.parseInt(parts[0])));
			else {
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
				
				ChatUtil.debug(parts[0] + " => " + Material.getMaterial(parts[0].toUpperCase()).name());
				block = new MaterialData(Material.getMaterial(parts[0].toUpperCase()));
			}
			
			parts[0] = block.getItemTypeId() + "";
			
			if(parts.length == 2) {
				if(!isNumeric(parts[1])) parts[1] = parseMetadata(parts, false);
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
	public static String parseMetadata(String[] parts, boolean recursive) {
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
				case 24:
				{
					if(Integer.parseInt(parts[1]) == 1) parts[1] = "chiseled";
					else if(Integer.parseInt(parts[1]) == 2) parts[1] = "smooth";
					else parts[1] = "";

					break;
				}
				case 33:
				case 34:
				{
					if(Integer.parseInt(parts[1]) == 1) parts[1] = "sandstone";
					else if(Integer.parseInt(parts[1]) == 2) parts[1] = "wooden";
					else if(Integer.parseInt(parts[1]) == 3) parts[1] = "cobblestone";
					else if(Integer.parseInt(parts[1]) == 4) parts[1] = "brick";
					else if(Integer.parseInt(parts[1]) == 5) parts[1] = "stone brick";
					else if(Integer.parseInt(parts[1]) == 6) parts[1] = "smooth";
					else parts[1] = "stone";

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
				case 98:
				{
					if(Integer.parseInt(parts[1]) == 1) parts[1] = "mossy";
					else if(Integer.parseInt(parts[1]) == 2) parts[1] = "cracked";
					else if(Integer.parseInt(parts[1]) == 3) parts[1] = "chiseled";
					else parts[1] = "";
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
			else if(parts[0].equalsIgnoreCase("24"))
			{
				if(parts[1].equalsIgnoreCase("chiseled") || parts[1].equalsIgnoreCase("creeper")) parts[1] = 1 + "";
				else if(parts[1].equalsIgnoreCase("smooth")) parts[1] = 2 + "";
				else parts[1] = 0 + "";
			}
			else if(parts[0].equalsIgnoreCase("33") || parts[0].equalsIgnoreCase("34"))
			{
				if(parts[1].equalsIgnoreCase("sandstone")) parts[1] = 1 + "";
				else if(parts[1].equalsIgnoreCase("wooden") || parts[1].equalsIgnoreCase("wood") || parts[1].equalsIgnoreCase("plank")) parts[1] = 2+ "";
				else if(parts[1].equalsIgnoreCase("cobblestone") || parts[1].equalsIgnoreCase("cobble")) parts[1] = 3 + "";
				else if(parts[1].equalsIgnoreCase("brick")) parts[1] = 4 + "";
				else if(parts[1].equalsIgnoreCase("stonebrick") || parts[1].equalsIgnoreCase("stone_brick")) parts[1] = 5 + "";
				else if(parts[1].equalsIgnoreCase("smoothstone") || parts[1].equalsIgnoreCase("smooth")) parts[1] = 6 + "";
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
			else if(parts[0].equalsIgnoreCase("98"))
			{
				if(parts[1].equalsIgnoreCase("mossy")) parts[1] = 1 + "";
				else if(parts[1].equalsIgnoreCase("cracked")) parts[1] = 2 + "";
				else if(parts[1].equalsIgnoreCase("chiseled")) parts[1] = 3 + "";
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
	public static boolean isNumeric(String str) {  
	  try
	  { double d = Double.parseDouble(str); }
	  catch(NumberFormatException nfe)  
	  { return false; }  
	  return true;  
	}
	
	/**
	 * Replaces the variables in the string with their values
	 * @param str String to be parsed
	 * @param mineName Name of the mine
	 * @return Parsed string
	 */
	public static String parseVars(String str, Mine curMine) {
		if(curMine == null) return null;
		String displayName = curMine.getDisplayName();

		if(displayName.equals("")) displayName = curMine.getName();
		str = str.replaceAll("%MINE%", curMine.getName());
		str = str.replaceAll("%MINENAME%", displayName);

		if(curMine.getAutomatic()) {
			// Reset period variable calculations
			int ptime = curMine.getResetPeriod();
			
			int phour = ptime / 3600;				// Unformatted variables
			int pmin = ptime / 60;					// Contain exact values for hour, minutes, seconds
			int psec = ptime;						// Used in further calculations
			
			int phourFormatted = phour;				// Formatted variables
			int pminFormatted = pmin - phour * 60;	// Values of higher-level variables have been subtracted
			int psecFormatted = psec - pmin * 60;	// Do not have a 0 in front if the value is < 10
			
			String phourClock = phourFormatted + "";				// Extra-formatted variables
			if(phourFormatted < 10) phourClock = "0" + phourClock;	// Have an added 0 in front
			String pminClock = pminFormatted + "";					// if the value of the variable
			if(pminFormatted < 10) pminClock = "0" + pminClock;		// is single-digit
			String psecClock = psecFormatted + "";					// used in super-formatted variable
			if(psecFormatted < 10) psecClock = "0" + psecClock;
			
			String ptimeClock = pminClock + ":" + psecClock;				// Super-formatted variable
			if(phour != 0) ptimeClock = phourFormatted + ":" + ptimeClock;	// Displays time in HOUR:MINUTE:SECOND format

			// Next reset variable calculations
			int ntime = (int)(curMine.getNextAutomaticResetTick()) / 20;
			
			int nhour = ntime / 3600;				// Unformatted variables
			int nmin = ntime / 60;					// Contain exact values for hour, minutes, seconds
			int nsec = ntime;						// Used in further calculations
			
			int nhourFormatted = nhour;				// Formatted variables
			int nminFormatted = nmin - nhour * 60;	// Values of higher-level variables have been subtracted
			int nsecFormatted = nsec - nmin * 60;	// Do not have a 0 in front if the value is < 10
			
			String nhourClock = nhourFormatted + "";				// Extra-formatted variables
			if(nhourFormatted < 10) nhourClock = "0" + nhourClock;	// Have an added 0 in front
			String nminClock = nminFormatted + "";					// if the value of the variable
			if(nminFormatted < 10) nminClock = "0" + nminClock;		// is single-digit
			String nsecClock = nsecFormatted + "";					// used in super-formatted variable
			if(nsecFormatted < 10) nsecClock = "0" + nsecClock;
			
			String ntimeClock = nminClock + ":" + nsecClock;				// Super-formatted variable
			if(nhour != 0) ntimeClock = nhourFormatted + ":" + ntimeClock;	// Displays time in HOUR:MINUTE:SECOND format


			// Legacy variables
			str = str.replaceAll("%HOUR%", phour + "");
			str = str.replaceAll("%MIN%", pminFormatted + "");
			str = str.replaceAll("%SEC%", psecFormatted + "");
			str = str.replaceAll("%TIME%", ptimeClock);
			str = str.replaceAll("%NEXT%", ntimeClock);
			
			// New variables
			
			// Reset Period variables
			str = str.replaceAll("%PHR%", phour + "");			// Raw variables
			str = str.replaceAll("%PMR%", pmin + "");
			str = str.replaceAll("%PSR%", psec + "");
			
			str = str.replaceAll("%PHOUR%", phourFormatted + "");	// Formatted variables
			str = str.replaceAll("%PMIN%", pminFormatted + "");
			str = str.replaceAll("%PSEC%", psecFormatted + "");
			
			str = str.replaceAll("%PHC%", phourClock);			// Extra-formatted variables
			str = str.replaceAll("%PMC%", pminClock);
			str = str.replaceAll("%PSC%", psecClock);
			
			str = str.replaceAll("%PTC%", ptimeClock);			// Super-formatted variable
			
			// Next Reset variables
			str = str.replaceAll("%NHR%", nhour + "");			// Raw variables
			str = str.replaceAll("%NMR%", nmin + "");
			str = str.replaceAll("%NSR%", nsec + "");
			
			str = str.replaceAll("%NHOUR%", nhourFormatted + "");	// Formatted variables
			str = str.replaceAll("%NMIN%", nminFormatted + "");
			str = str.replaceAll("%NSEC%", nsecFormatted + "");
			
			str = str.replaceAll("%NHC%", nhourClock);			// Extra-formatted variables
			str = str.replaceAll("%NMC%", nminClock);
			str = str.replaceAll("%NSC%", nsecClock);
			
			str = str.replaceAll("%NTC%", ntimeClock);			// Super-formatted variable
		}

		str = parseColors(str);

		return str;
	}
	
	/**
	 * Replaces the color codes with colors
	 * @param msg String to be parsed
	 * @return Parsed string
	 */
	public static String parseColors(String msg) {
		if(msg == null) return null;
		msg = msg.replaceAll("&0", ChatColor.BLACK.toString());
		msg = msg.replaceAll("&1", ChatColor.DARK_BLUE.toString());
		msg = msg.replaceAll("&2", ChatColor.DARK_GREEN.toString());
		msg = msg.replaceAll("&3", ChatColor.DARK_AQUA.toString());
		msg = msg.replaceAll("&4", ChatColor.DARK_RED.toString());
		msg = msg.replaceAll("&5", ChatColor.DARK_PURPLE.toString());
		msg = msg.replaceAll("&6", ChatColor.GOLD.toString());
		msg = msg.replaceAll("&7", ChatColor.GRAY.toString());
		msg = msg.replaceAll("&8", ChatColor.DARK_GRAY.toString());
		msg = msg.replaceAll("&9", ChatColor.BLUE.toString());

		msg = msg.replaceAll("&a", ChatColor.GREEN.toString());
		msg = msg.replaceAll("&b", ChatColor.AQUA.toString());
		msg = msg.replaceAll("&c", ChatColor.RED.toString());
		msg = msg.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString());
		msg = msg.replaceAll("&e", ChatColor.YELLOW.toString());
		msg = msg.replaceAll("&f", ChatColor.WHITE.toString());

		msg = msg.replaceAll("&A", ChatColor.GREEN.toString());
		msg = msg.replaceAll("&B", ChatColor.AQUA.toString());
		msg = msg.replaceAll("&C", ChatColor.RED.toString());
		msg = msg.replaceAll("&D", ChatColor.LIGHT_PURPLE.toString());
		msg = msg.replaceAll("&E", ChatColor.YELLOW.toString());
		msg = msg.replaceAll("&F", ChatColor.WHITE.toString());

		msg = msg.replaceAll("&k", ChatColor.MAGIC.toString());
		msg = msg.replaceAll("&l", ChatColor.BOLD.toString());
		msg = msg.replaceAll("&m", ChatColor.STRIKETHROUGH.toString());
		msg = msg.replaceAll("&n", ChatColor.UNDERLINE.toString());
		msg = msg.replaceAll("&o", ChatColor.ITALIC.toString());
		msg = msg.replaceAll("&r", ChatColor.RESET.toString());

		msg = msg.replaceAll("&K", ChatColor.MAGIC.toString());
		msg = msg.replaceAll("&L", ChatColor.BOLD.toString());
		msg = msg.replaceAll("&M", ChatColor.STRIKETHROUGH.toString());
		msg = msg.replaceAll("&N", ChatColor.UNDERLINE.toString());
		msg = msg.replaceAll("&O", ChatColor.ITALIC.toString());
		msg = msg.replaceAll("&R", ChatColor.RESET.toString());

		return msg;
	}
	
	/**
	 * Parses the message for time and returns it in seconds
	 * @param message Input in the HOUR:MIN:SEC format
	 * @return Seconds
	 */
	public static int parseTime(String message) {
		if(message.charAt(0) == ':')
			message = "0" + message;
		if(message.charAt(message.length() - 1) == ':')
			message = message + "0";

		String[] parts = message.split(":");
		int time = 0;

		try {
			if(parts.length == 3) {
				time += Integer.parseInt(parts[0]) * 3600;
				time += Integer.parseInt(parts[1]) * 60;
				time += Integer.parseInt(parts[2]);
			}
			else if(parts.length == 2) {
				time += Integer.parseInt(parts[0]) * 60;
				time += Integer.parseInt(parts[1]);
			}
			else if(parts.length == 1) {
				time += Integer.parseInt(parts[0]);
			}
			else {
				return -1;
			}
		}
		catch(NumberFormatException nfe) {
			return -1;
		}

		return time;
	}
	
	/**
	 * Parses the seconds and returns time in HOUR:MIN:SEC format
	 * @param seconds Seconds to parse
	 * @return Time in user-friendly format
	 */
	public static String parseSeconds(int seconds) {
		int hour = seconds / 3600;
		int min = (seconds - (hour * 3600)) / 60;
		int sec = seconds - (hour * 3600) - (min * 60);
		String resetTime = min + ":";
		if(hour > 0)
		{
			if(min < 10)
				resetTime = "0" + resetTime;
			resetTime = hour + ":" + resetTime;
		}
		if(sec < 10)
			resetTime = resetTime + "0";
		resetTime = resetTime + sec;
		return resetTime;
	}
	
	/**
	 * Checks if both selection points are set
	 * @return true if both points are set, false if they are not
	 */
	public static boolean locationsSet() {
		Location[] point = CommandManager.getLocation();
		if(point[0] == null || point[1] == null) return false;
		return true;
	}

	/**
	 * Counts the number of blocks in the selection region
	 * @return the number of blocks in the region
	 */
	public static int getBlockCount() {
		Location[] point = CommandManager.getLocation();
		int x1 = point[0].getBlockX(), x2 = point[1].getBlockX(),
	       y1 = point[0].getBlockY(), y2 = point[1].getBlockY(),
	       z1 = point[0].getBlockZ(), z2 = point[1].getBlockZ();

		int xdist, zdist, ydist;

		if(x1 > x2) { xdist = Math.round(x1 - x2); } else { xdist = Math.round(x2 - x1); }
		if(y1 > y2) { ydist = Math.round(y1 - y2); } else { ydist = Math.round(y2 - y1); }
		if(z1 > z2) { zdist = Math.round(z1 - z2); } else { zdist = Math.round(z2 - z1); }

		int blockCount = xdist * ydist * zdist;

		return blockCount;
	}
	
	/**
	 * Parses the material and returns a user-friendly name
	 * @param material Material to parse
	 * @return Name of the material
	 */
	public static String parseMaterial(Material material) {
		return material.toString().toLowerCase().replace("_", " ");
	}
	
	/**
	 * Parses the material and returns a user-friendly name
	 * @param material Material to parse
	 * @return Name of the material
	 */
	public static String parseMaterialData(MaterialData material) {
		String str[] = {material.getItemTypeId() + "", material.getData() + ""};
		String name = parseMetadata(str, true) + " " + material.getItemType().toString().toLowerCase().replace("_", " ");
		return name;
	}
	
	/**
	 * Rounds the nubmer to the specified decimal point
	 * @param number Number to round
	 * @param decimal Decimal point
	 * @return The rounded number
	 */
	public static double round(double number, int decimal) {
		double pow = Math.pow(10, decimal);
		number = ((int)(number * pow)) / pow;
		return number;
	}
}