package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;

public class Help
{	
	public static void getAuto()
	{
		formatHelp("auto", " toggle", "Toggles the automatic resets on and off", "");
		formatHelp("auto", " time <time>", "Changes the automatic reset time to the value specified", "");
		Util.sendMessage(" The value cannot be smaller then 5 minutes");
		formatHelp("auto", " warning toggle", "Toggles warnings before the reset on and off", "");
		formatHelp("auto", " warning add <time>", "Adds a warning at time specified", "");
		formatHelp("auto", " warning remove <time>", "Adds a warning at time specified", "");
		Util.sendMessage(" The value cannot be larger then the reset time");
	}
	
	public static void getBlacklist()
	{
		formatHelp("blacklist", " toggle", "Enables the use of blacklist for the mine", "");
		formatHelp("blacklist", " whitelist", "Makes the blacklist be treated as a whitelist", "");
		Util.sendMessage(" Blacklist should be enabled");
		formatHelp("blacklist", " add <block>", "Adds a block to the blacklist", "");
		formatHelp("blacklist", " remove <block>", "Removes a block from the whitelist", "");
	}
	
	public static void getConfig()
	{
		Util.sendMessage(" You do NOT need these commands. At all. I promise.");
		formatHelp("config", " save", "Saves the data into a configuration file", "");
		formatHelp("config", " load", "Loads the configuration from a file", "");
	}
	
	public static void getEdit()
	{
		formatHelp("edit", " <name>", "Selects a mine to edit its properties", "");
		formatHelp("name", " <name>", "Creates a display name for a mine", "");
		formatHelp("cooldown", " toggle", "Toggles the reset cooldown in the mine (manual reset)", "");
		formatHelp("cooldown", " <time>", "Sets a cooldown time for the manual reset", "");
		formatHelp("add", " <block> <percentage>", "Adds another type of block to the mine", "");
		formatHelp("remove", " <block>", "Removes a block from the mine", "");
		formatHelp("delete", "", "Completely deletes all the data about the selected mine", "");
	}
	
	public static void getHelp()
	{
		String title = Language.getString("general.title");
		Util.sendMessage("                    -=[ " + title + " ]=-");
		
		formatHelp("info", " <name>", "Returns the information about a mine", "info");	
		formatHelp("list", "", "Lists all the available mines", "list");
		formatHelp("warp", " <name>", "Teleports you to the mine warp location", "warp");
		formatHelp("reset", " <name>", "Resets the mine manually", "reset");
		formatHelp("select", "", "Shows region selection help page", "edit");
		formatHelp("save", " <name>", "Saves the region for future use", "edit");
		formatHelp("edit", "", "Shows a help page on how to handle the mine options", "edit");
		formatHelp("delete", " <name>", "Completely deletes a mine", "edit");
		formatHelp("auto", "", "Shows a help page on how to automate the mine", "edit");
		formatHelp("protection", "", "Shows how to set up a mine protection", "edit");
		
		return;
	}
	
	public static void getInfo()
	{
		formatHelp("info", " <name>", "Returns the information about a mine", "");
		return;
	}
	
	public static void getProtection()
	{
		formatHelp("protection", " pvp", "Toggles the PVP on and off for the current mine", "");
		
		formatHelp("protection", " breaking toggle", "Enables or disables the block breaking protection", "");
		formatHelp("protection", " breaking blacklist toggle", "Enables or disables the block breaking blacklist", "");
		formatHelp("protection", " breaking blacklist whitelist", "Should the blacklist be treated as a whitelist?", "");
		formatHelp("protection", " breaking blacklist add", "Add a block to the block breaking blacklist", "");
		formatHelp("protection", " breaking blacklist remove", "Remove a block from the block breaking blacklist", "");

		formatHelp("protection", " placement blacklist toggle", "Enables or disables the block breaking blacklist", "");
		formatHelp("protection", " placement blacklist whitelist", "Should the blacklist be treated as a whitelist?", "");
		formatHelp("protection", " placement blacklist add", "Add a block to the block breaking blacklist", "");
		formatHelp("protection", " placement blacklist remove", "Remove a block from the block breaking blacklist", "");
		
		return;
	}
	
	public static void getReset()
	{
		formatHelp("reset", " <name>", "Resets the mine manually", "");
		return;
	}
	
	public static void getSave()
	{
		formatHelp("save", " <name>", "Saves the region for future use", "");
		Util.sendMessage(" If no name is provided, the default name will be used");
		Util.sendMessage(" The default name is defined in the configuration file");
		
		return;
	}
	
	public static void getSelect()
	{
		formatHelp("select", " hpos1", "Creates a reference point 1 at the block you are looking at", "");
		formatHelp("select", " hpos2", "Creates a reference point 2 at the block you are looking at", "");
		Util.sendMessage(" Your field of view is limited to 100 blocks");
		formatHelp("select", " pos1", "Creates a reference point 1 at your immediate location", "");
		formatHelp("select", " pos2", "Creates a reference point 2 at your immediate location", "");
		Util.sendMessage(" You can also select a region with your normal World Edit tool");
		
		return;
	}
	
	public static void getTeleport()
	{
		formatHelp("warp", " <name>", "Teleports you to the mine warp location", "");
		formatHelp("warp", " set", "Sets a warp for the current mine", "");
		return;
	}
	
	private static void formatHelp(String command, String arguments, String description, String node)
	{
		CommandSender sender = CommandManager.getSender();
		if(Util.senderHasPermission(node))
			sender.sendMessage(ChatColor.GREEN + "/mine " + command + ChatColor.GRAY + arguments + ChatColor.WHITE + " " + description);
		
		return;
	}
}
