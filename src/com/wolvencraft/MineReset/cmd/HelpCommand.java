package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.ChatUtil;
import com.wolvencraft.MineReset.util.GeneratorUtil;
import com.wolvencraft.MineReset.util.Util;

public class HelpCommand {
	public static void getConfig() {
		formatHeader(20, "Data");
		formatHelp("data", "save", "Saves the mine data to file");
		formatHelp("data", "load", "Loads the mine data from file");
		formatHelp("data", "import", "Imports the old-formatted data to the new format");
	}
	
	public static void getEdit() {
		formatHeader(20, "Editing");
		formatHelp("edit", "<id>", "Selects a mine to edit its properties");
		formatHelp("none", "", "De-selects the mine");
		formatHelp("name", "<id>", "Creates a display name for a mine");
		formatHelp("+", "<block> [percentage]", "Adds a block type to the mine");
		formatNote("If no persentage is provided, the block will fill up all the space available");
		formatHelp("-", "<block> [persentage]", "Removes the specified persentage of a block from the mine");
		formatNote("If no persentage is provided, the block will be removed completely");
		formatHelp("delete", "", "Completely deletes all the data about the selected mine");
		formatHelp("generator", "<generator>", "Changes the active generator for the mine");
		formatNote("The following generators are supported: ");
		formatNote(GeneratorUtil.list());
		formatHelp("cooldown toggle", "", "Turn the reset cooldown on and off for the mine");
		formatHelp("cooldown <time>", "", "Sets the cooldown time to the value specified");
		return;
	}
	
	public static void getHelp() {
		formatHeader(20, Language.getString("general.title"));
        formatHelp("about", "", "Returns version and project info about MineReset");
		formatHelp("info", "<name>", "Returns the information about a mine", "info.all");	
		formatHelp("list", "", "Lists all the available mines", "info.list");
		formatHelp("warp", "<name>", "Teleports you to the mine warp location", "warp.use");
		formatHelp("reset", "<name> [generator]", "Resets the mine manually", "reset.manual");
		formatHelp("select", "", "Shows region selection help page", "edit.select");
		formatHelp("save", "<name>", "Saves the region for future use", "edit.save");
		formatHelp("edit", "", "Shows a help page on how to handle the mine options", "edit.info");
		formatHelp("snapshot", "", "Displays a help page on the snapshots", "edit.snapshot");
		formatHelp("delete", "<name>", "Completely deletes a mine", "edit.info");
		formatHelp("timer", "", "Shows a help page on how to automate the mine", "edit.timer");
		formatHelp("protection", "", "Shows how to set up a mine protection", "edit.protection");
		return;
	}
	
	public static void getInfo() {
		formatHeader(20, "Information");
		formatHelp("info", "<name>", "Returns the information about a mine", "info.all");
		formatHelp("info", "<name> blacklist", "Returns the information about mine blacklist", "info.all");
		formatHelp("info", "<name> protection", "Returns the information about mine protection", "info.all");
		formatHelp("info", "<name> signs", "Returns the information about signs", "info.all");
		formatHelp("info", "<name> reset", "Returns the information about resets", "info.all");
		formatHelp("time", "<name>", "Returns the next reset time for the mine", "info.time");
		return;
	}
	
	public static void getProtection() {
		formatHeader(20, "Protection");
		formatHelp("prot", "pvp", "Toggles the PVP on and off for the current mine");
		ChatUtil.sendMessage(" ");
		formatHelp("prot", "break toggle", "Enables or disables the block breaking protection");
		formatHelp("prot", "break whitelist", "Should the blacklist be treated as a whitelist?");
		formatHelp("prot", "break + <block>", "Add <block> to the block breaking blacklist");
		formatHelp("prot", "break - <block>", "Remove <block> from the block breaking blacklist");
		ChatUtil.sendMessage(" ");
		formatHelp("prot", "place toggle", "Enables or disables the block breaking blacklist");
		formatHelp("prot", "place whitelist", "Should the blacklist be treated as a whitelist?");
		formatHelp("prot", "place + <block>", "Add <block> to the block placement blacklist");
		formatHelp("prot", "place - <block>", "Remove <block> from the block placement blacklist");
		return;
	}
	
	public static void getReset() {
		formatHeader(20, "Reset");
		formatHelp("reset", "<name> [generator]", "Resets the mine manually");
		formatNote("Resets the mine according to the generator defined by the configuration.");
		formatNote("The following generators are supported: ");
		formatNote(GeneratorUtil.list());
		return;
	}
	
	public static void getSave() {
		formatHeader(20, "Save");
		formatHelp("save", "<name>", "Saves the region for future use");
		return;
	}
	
	public static void getSelect() {
		formatHeader(20, "Selecting");
		formatHelp("hpos1", "", "Creates a reference point 1 at the block you are looking at");
		formatHelp("hpos2", "", "Creates a reference point 2 at the block you are looking at");
		formatNote("Your field of view is limited to 100 blocks");
		formatHelp("pos1", "", "Creates a reference point 1 at your immediate location");
		formatHelp("pos2", "", "Creates a reference point 2 at your immediate location");
		formatNote("You can also select a region with your normal World Edit tool");
		return;
	}
	
	public static void getSign() {
		formatHeader(20, "Signs");
		formatHelp("sign", "create", "Saves the targeted sign into a file");
		formatHelp("sign", "setparent <id>", "Sets the sign's parent to the one specified");
		formatHelp("sign", "reset", "Marks the targeted sign as mine-resetting");
		formatHelp("sign", "remove", "Removes the targeted sign from data");
		return;
	}
	
	public static void getSnapshot() {
		formatHeader(20, "Snapshot");
		formatHelp("snapshot", "save", "Saves the snapshot of the selected region to file");
		formatHelp("snapshot", "restore", "Restores the snapshot of a selected mine");
		formatHelp("snapshot", "delete", "Removes the snapshot data completely");
		return;
	}

	public static void getTimer() {
		formatHeader(20, "Timer");
		formatHelp("timer", "toggle", "Toggles the automatic resets on and off");
		formatHelp("timer", "set <time>", "Changes the automatic reset time to the value specified");
		formatHelp("timer", "warning toggle", "Toggles reset warnings on and off");
		formatHelp("timer", "warning + <time>", "Adds a warning at time specified");
		formatHelp("timer", "warning - <time>", "Adds a warning at time specified");
		return;
	}
	
	public static void getWarp() {
		formatHeader(20, "Teleportation");
		formatHelp("warp", "<name>", "Teleports you to the mine warp location");
		formatHelp("warp", "set", "Sets a warp for the current mine");
		return;
	}
	
	private static void formatHelp(String command, String arguments, String description, String node) {
		CommandSender sender = CommandManager.getSender();
		if(!arguments.equalsIgnoreCase("")) arguments = " " + arguments;
		if(Util.hasPermission(node) || node.equals(""))
			sender.sendMessage(ChatColor.GOLD + "/mine " + command + ChatColor.GRAY + arguments + ChatColor.WHITE + " " + description);
		return;
	}
	
	private static void formatHelp(String command, String arguments, String description) {
		formatHelp(command, arguments, description, "");
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
