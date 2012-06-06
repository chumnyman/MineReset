package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class HelpCommand
{	
	public static void getAuto()
	{
		Message.sendMessage("                    -=[ Timer ]=-");
		formatHelp("auto", " toggle", "Toggles the automatic resets on and off", "");
		formatHelp("auto", " time <time>", "Changes the automatic reset time to the value specified", "");
		formatHelp("auto", " warning add <time>", "Adds a warning at time specified", "");
		formatHelp("auto", " warning remove <time>", "Adds a warning at time specified", "");
		formatHelp("auto", " warning clear", "Removes all warnings from the mine", "");
	}
	
	public static void getBlacklist()
	{
		Message.sendMessage("                    -=[ Blacklist ]=-");
		formatHelp("blacklist", " toggle", "Enables the use of blacklist for the mine", "");
		formatHelp("blacklist", " whitelist", "Makes the blacklist be treated as a whitelist", "");
		formatHelp("blacklist", " add <block>", "Adds a block to the blacklist", "");
		formatHelp("blacklist", " remove <block>", "Removes a block from the whitelist", "");
	}
	
	public static void getConfig()
	{
		Message.sendMessage("                   -=[ Configuration ]=-");
		formatHelp("config", " save", "Saves the data into a configuration file", "");
		formatHelp("config", " load", "Loads the configuration from a file", "");
	}
	
	public static void getEdit()
	{
		Message.sendMessage("                     -=[ Editing ]=-");
		formatHelp("edit", " <name>", "Selects a mine to edit its properties", "");
		formatHelp("none", "", "De-selects the mine", "");
		formatHelp("name", " <name>", "Creates a display name for a mine", "");
		formatHelp("add", " <block> [percentage]", "Adds a block type to the mine", "");
		Message.sendMessage("If no persentage is provided, the block will fill up all the space available");
		formatHelp("remove", " <block> [persentage]", "Removes the specified persentage of a block from the mine", "");
		Message.sendMessage("If no persentage is provided, the block will be removed completely");
		formatHelp("delete", "", "Completely deletes all the data about the selected mine", "");
	}
	
	public static void getHelp()
	{
		String title = Language.getString("general.title");
		Message.sendMessage("                    -=[ " + title + " ]=-");
		
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
		Message.sendMessage("                    -=[ Information ]=-");
		formatHelp("info", " <name>", "Returns the information about a mine", "info");
		formatHelp("time", " <name>", "Returns the next reset time for the mine", "time");
		return;
	}
	
	public static void getProtection()
	{
		//formatHelp("protection", " pvp", "Toggles the PVP on and off for the current mine", "");
		
		formatHelp("prot", " breaking toggle", "Enables or disables the block breaking protection", "");
		formatHelp("prot", " breaking blacklist toggle", "Enables or disables the block breaking blacklist", "");
		formatHelp("prot", " breaking blacklist whitelist", "Should the blacklist be treated as a whitelist?", "");
		formatHelp("prot", " breaking blacklist add", "Add a block to the block breaking blacklist", "");
		formatHelp("prot", " breaking blacklist remove", "Remove a block from the block breaking blacklist", "");

		formatHelp("prot", " placement blacklist toggle", "Enables or disables the block breaking blacklist", "");
		formatHelp("prot", " placement blacklist whitelist", "Should the blacklist be treated as a whitelist?", "");
		formatHelp("prot", " placement blacklist add", "Add a block to the block breaking blacklist", "");
		formatHelp("prot", " placement blacklist remove", "Remove a block from the block breaking blacklist", "");
		
		return;
	}
	
	public static void getReset()
	{
		Message.sendMessage("                    -=[ Reset ]=-");
		formatHelp("reset", " <name>", "Resets the mine manually", "");
		return;
	}
	
	public static void getSave()
	{
		Message.sendMessage("                    -=[ Save ]=-");
		formatHelp("save", " <name>", "Saves the region for future use", "");
		Message.sendMessage(" If no name is provided, the default name will be used");
		Message.sendMessage(" The default name is defined in the configuration file");
		
		return;
	}
	
	public static void getSelect()
	{
		Message.sendMessage("                    -=[ Information ]=-");
		formatHelp("hpos1", "", "Creates a reference point 1 at the block you are looking at", "");
		formatHelp("hpos2", "", "Creates a reference point 2 at the block you are looking at", "");
		Message.sendMessage(" Your field of view is limited to 100 blocks");
		formatHelp("pos1", "", "Creates a reference point 1 at your immediate location", "");
		formatHelp("pos2", "", "Creates a reference point 2 at your immediate location", "");
		Message.sendMessage(" You can also select a region with your normal World Edit tool");
		
		return;
	}
	
	public static void getSign()
	{
		formatHelp("sign", " create", "Saves the targeted sign into a file", "");
		formatHelp("sign", " reset", "Marks the targeted sign as mine-resetting", "");
		formatHelp("sign", " remove", "Removes the targeted sign from data", "");
		
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
			sender.sendMessage(ChatColor.BLUE + "/mine " + command + ChatColor.GRAY + arguments + ChatColor.WHITE + " " + description);
		
		return;
	}
}
