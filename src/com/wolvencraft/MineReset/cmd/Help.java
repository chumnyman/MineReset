package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.wolvencraft.MineReset.CommandManager;

public class Help
{	
	public void getAuto()
	{
		formatHelp("mine auto", " toggle", "Toggles the automatic resets on and off", "");
		formatHelp("mine auto", " time <time>", "Changes the automatic reset time to the value specified, in minutes", "");
		Util.sendMessage(" The value cannot be smaller then 5 minutes");
		formatHelp("mine auto", " warning toggle", "Toggles warnings before the reset on and off", "");
		formatHelp("mine auto", " warning <time>", "Changes the warning time to the value specified, in minutes", "");
		Util.sendMessage(" The value cannot be larger then the reset time");
	}
	
	public static void getBlacklist()
	{
		formatHelp("mine blacklist", " toggle", "Enables the use of blacklist for the mine", "");
		formatHelp("mine blacklist", " whitelist", "Makes the blacklist be treated as a whitelist", "");
		Util.sendMessage(" Blacklist should be enabled");
		formatHelp("mine blacklist", " add <block>", "Adds a block to the blacklist", "");
		formatHelp("mine blacklist", " remove <block>", "Removes a block from the whitelist", "");
	}
	
	public static void getConfig()
	{
		Util.sendMessage(" You do NOT need these commands. At all. I promise.");
		formatHelp("mine config", " save", "Saves the data into a configuration file", "");
		formatHelp("mine config", " load", "Loads the configuration from a file", "");
		formatHelp("mine config", " update", "Updates the region data from the old format to the new one", "");
	}
	
	public static void getEdit()
	{
		formatHelp("mine edit", " <name>", "Selects a mine to edit its properties", "");
		formatHelp("mine name", " <name>", "Creates a display name for a mine", "");
		//formatHelp("mine cooldown", " toggle", "Toggles the reset cooldown in the mine (manual reset)", "");
		//formatHelp("mine cooldown", " <time>", "Sets a cooldown time for the manual reset", "");
		formatHelp("mine add", " <block> <percentage>", "Adds another type of block to the mine", "");
		formatHelp("mine remove", " <block>", "Removes a block from the mine", "");
		formatHelp("mine delete", "", "Completely deletes all the data about the selected mine", "");
	}
	
	public static void getHelp()
	{
		String title = Util.getConfigString("messages.title");
		Util.sendMessage("                    -=[ " + title + " ]=-");
		
		formatHelp("mine info", " <name>", "Returns the information about a mine", "info");	
		formatHelp("mine list", "", "Lists all the available mines", "list");
		formatHelp("mine warp", " <name>", "Teleports you to the mine warp location", "warp");
		formatHelp("mine reset", " <name>", "Resets the mine manually", "reset");
		formatHelp("mine select", "", "Shows region selection help page", "edit");
		formatHelp("mine save", " <name>", "Saves the region for future use", "edit");
		formatHelp("mine edit", "", "Shows a help page on how to handle the mine options", "edit");
		formatHelp("mine delete", " <name>", "Completely deletes a mine", "edit");
		formatHelp("mine auto", "", "Shows a help page on how to automate the mine", "edit");
		formatHelp("mine protection", "", "Shows how to set up a mine protection", "edit");
		
		return;
	}
	
	public static void getInfo()
	{
		formatHelp("mine info", " <name>", "Returns the information about a mine", "");
		return;
	}
	
	public static void getProtection()
	{
		formatHelp("mine protection", " pvp", "Toggles the PVP on and off for the current mine", "");
		
		formatHelp("mine protection", " breaking toggle", "Enables or disables the block breaking protection", "");
		formatHelp("mine protection", " breaking blacklist toggle", "Enables or disables the block breaking blacklist", "");
		formatHelp("mine protection", " breaking blacklist whitelist", "Should the blacklist be treated as a whitelist?", "");
		formatHelp("mine protection", " breaking blacklist add", "Add a block to the block breaking blacklist", "");
		formatHelp("mine protection", " breaking blacklist remove", "Remove a block from the block breaking blacklist", "");

		formatHelp("mine protection", " placement blacklist toggle", "Enables or disables the block breaking blacklist", "");
		formatHelp("mine protection", " placement blacklist whitelist", "Should the blacklist be treated as a whitelist?", "");
		formatHelp("mine protection", " placement blacklist add", "Add a block to the block breaking blacklist", "");
		formatHelp("mine protection", " placement blacklist remove", "Remove a block from the block breaking blacklist", "");
		
		return;
	}
	
	public static void getReset()
	{
		formatHelp("mine reset", " <name>", "Resets the mine manually", "");
		return;
	}
	
	public static void getSave()
	{
		formatHelp("mine save", " <name>", "Saves the region for future use", "");
		Util.sendMessage(" If no name is provided, the default name will be used");
		Util.sendMessage(" The default name is defined in the configuration file");
		
		return;
	}
	
	public static void getSelect()
	{
		formatHelp("mine select", " hpos1", "Creates a reference point 1 at the block you are looking at", "");
		formatHelp("mine select", " hpos2", "Creates a reference point 2 at the block you are looking at", "");
		Util.sendMessage(" Your field of view is limited to 100 blocks");
		formatHelp("mine select", " pos1", "Creates a reference point 1 at your immediate location", "");
		formatHelp("mine select", " pos2", "Creates a reference point 2 at your immediate location", "");
		Util.sendMessage(" You can also select a region with your normal World Edit tool");
		
		return;
	}
	
	public static void getTeleport()
	{
		formatHelp("mine warp", " <name>", "Teleports you to the mine warp location", "");
		formatHelp("mine warp", " set", "Sets a warp for the current mine", "");
		return;
	}
	
	private static void formatHelp(String command, String arguments, String description, String node)
	{
		CommandSender sender = CommandManager.getSender();
		if(Util.senderHasPermission(node))
			sender.sendMessage(ChatColor.GREEN + "/" + command + ChatColor.GRAY + arguments + ChatColor.WHITE + " " + description);
		
		return;
	}
}
