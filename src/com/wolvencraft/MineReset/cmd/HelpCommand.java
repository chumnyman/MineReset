package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Util;

public class HelpCommand {	
	public static void getAuto() {
		formatHeader(20, "Timer");
		formatHelp("auto", "toggle", "Toggles the automatic resets on and off", "");
		formatHelp("auto", "time <time>", "Changes the automatic reset time to the value specified", "");
		formatHelp("auto", "warning toggle", "Toggles reset warnings on and off", "");
		formatHelp("auto", "warning add <time>", "Adds a warning at time specified", "");
		formatHelp("auto", "warning remove <time>", "Adds a warning at time specified", "");
		return;
	}
	
	public static void getBlacklist() {
		formatHeader(20, "Blacklist");
		formatHelp("blacklist", "toggle", "Enables the use of blacklist for the mine", "");
		formatHelp("blacklist", "whitelist", "Makes the blacklist be treated as a whitelist", "");
		formatHelp("blacklist", "add <block>", "Adds a block to the blacklist", "");
		formatHelp("blacklist", "remove <block>", "Removes a block from the whitelist", "");
		return;
	}
	
	public static void getConfig() {
		formatHeader(20, "Configuration");
		formatHelp("config", "save", "Saves the data into a configuration file", "");
		formatHelp("config", "load", "Loads the configuration from a file", "");
	}
	
	public static void getEdit() {
		formatHeader(20, "Editing");
		formatHelp("edit", "<name>", "Selects a mine to edit its properties", "");
		formatHelp("none", "", "De-selects the mine", "");
		formatHelp("name", "<name>", "Creates a display name for a mine", "");
		formatHelp("add", "<block> [percentage]", "Adds a block type to the mine", "");
		formatNote("If no persentage is provided, the block will fill up all the space available");
		formatHelp("remove", "<block> [persentage]", "Removes the specified persentage of a block from the mine", "");
		formatNote("If no persentage is provided, the block will be removed completely");
		formatHelp("delete", "", "Completely deletes all the data about the selected mine", "");
		return;
	}
	
	public static void getHelp() {
		String title = Language.getString("general.title");
		formatHeader(20, title);

        formatHelp("about", "", "Returns version info and project info about MineReset", "");
		formatHelp("info", "<name>", "Returns the information about a mine", "info");	
		formatHelp("list", "", "Lists all the available mines", "list");
		formatHelp("warp", "<name>", "Teleports you to the mine warp location", "warp");
		formatHelp("reset", "<name>", "Resets the mine manually", "reset");
		formatHelp("select", "", "Shows region selection help page", "edit");
		formatHelp("save", "<name>", "Saves the region for future use", "edit");
		formatHelp("edit", "", "Shows a help page on how to handle the mine options", "edit");
		formatHelp("delete", "<name>", "Completely deletes a mine", "edit");
		formatHelp("auto", "", "Shows a help page on how to automate the mine", "edit");
		formatHelp("protection", "", "Shows how to set up a mine protection", "edit");
		return;
	}
	
	public static void getInfo() {
		formatHeader(20, "Information");
		formatHelp("info", "<name>", "Returns the information about a mine", "info");
		formatHelp("info", "<name> blacklist", "Returns the information about mine blacklist", "info");
		formatHelp("info", "<name> protection", "Returns the information about mine protection", "info");
		formatHelp("info", "<name> signs", "Returns the information about signs", "info");
		formatHelp("info", "<name> reset", "Returns the information about resets", "info");
		formatHelp("time", "<name>", "Returns the next reset time for the mine", "time");
		return;
	}
	
	public static void getProtection() {
		formatHeader(20, "Protection");
		formatHelp("prot", "pvp", "Toggles the PVP on and off for the current mine", "");
		formatHelp("prot", "break toggle", "Enables or disables the block breaking protection", "");
		formatHelp("prot", "break blacklist toggle", "Enables or disables the block breaking blacklist", "");
		formatHelp("prot", "break blacklist whitelist", "Should the blacklist be treated as a whitelist?", "");
		formatHelp("prot", "break blacklist add", "Add a block to the block breaking blacklist", "");
		formatHelp("prot", "break blacklist remove", "Remove a block from the block breaking blacklist", "");
		formatHelp("prot", "place blacklist toggle", "Enables or disables the block breaking blacklist", "");
		formatHelp("prot", "place blacklist whitelist", "Should the blacklist be treated as a whitelist?", "");
		formatHelp("prot", "place blacklist add", "Add a block to the block breaking blacklist", "");
		formatHelp("prot", "place blacklist remove", "Remove a block from the block breaking blacklist", "");
		return;
	}
	
	public static void getReset() {
		formatHeader(20, "Reset");
		formatHelp("reset", "<name> [generator]", "Resets the mine manually", "");
		formatNote("Resets the mine according to the generator defined by the configuration.");
		formatNote("The following generators are supported: RANDOM, CLEAR");
		return;
	}
	
	public static void getSave() {
		formatHeader(20, "Save");
		formatHelp("save", "<name>", "Saves the region for future use", "");
		return;
	}
	
	public static void getSelect() {
		formatHeader(20, "Selecting");
		formatHelp("hpos1", "", "Creates a reference point 1 at the block you are looking at", "");
		formatHelp("hpos2", "", "Creates a reference point 2 at the block you are looking at", "");
		formatNote("Your field of view is limited to 100 blocks");
		formatHelp("pos1", "", "Creates a reference point 1 at your immediate location", "");
		formatHelp("pos2", "", "Creates a reference point 2 at your immediate location", "");
		formatNote("You can also select a region with your normal World Edit tool");
		return;
	}
	
	public static void getSign() {
		formatHeader(20, "Signs");
		formatHelp("sign", "create", "Saves the targeted sign into a file", "");
		formatHelp("sign", "parent <id>", "Sets the sign's parent to the one specified", "");
		formatHelp("sign", "reset", "Marks the targeted sign as mine-resetting", "");
		formatHelp("sign", "remove", "Removes the targeted sign from data", "");
		return;
	}
	
	public static void getTeleport() {
		formatHeader(20, "Teleportation");
		formatHelp("warp", "<name>", "Teleports you to the mine warp location", "");
		formatHelp("warp", "set", "Sets a warp for the current mine", "");
		return;
	}
	
	private static void formatHelp(String command, String arguments, String description, String node) {
		CommandSender sender = CommandManager.getSender();
		if(!arguments.equalsIgnoreCase("")) arguments = " " + arguments;
		if(Util.senderHasPermission(node) || node.equals(""))
			sender.sendMessage(ChatColor.GOLD + "/mine " + command + ChatColor.GRAY + arguments + ChatColor.WHITE + " " + description);
		return;
	}
	
	public static void formatHeader(int padding, String name) {
		CommandSender sender = CommandManager.getSender();
		String spaces = "";
		for(int i = 0; i < padding; i++)
			spaces = spaces + " ";
		sender.sendMessage(spaces + "-=[ " + ChatColor.BLUE + name + ChatColor.WHITE + " ]=-");
	}
	
	private static void formatNote(String message) {
		CommandSender sender = CommandManager.getSender();
		sender.sendMessage(" " + message);
	}
}
