package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.util.Util;

public class HelpCommand implements BaseCommand {
	public boolean run(String[] args) { 
		getHelp();
		return true;
	}
	
	public void getHelp() {
		formatHeader(20, Language.getString("general.title"));
        formatHelp("about", "", "Returns version and project info about MineReset");
		formatHelp("info", "<name>", "Returns the information about a mine", "info.all");	
		formatHelp("list", "", "Lists all the available mines", "info.list");
		formatHelp("warp", "<name>", "Teleports you to the mine warp location", "warp.use");
		formatHelp("reset", "<name> [generator]", "Resets the mine manually", "reset.manual");
		formatHelp("select", "", "Shows region selection help page", "edit.select");
		formatHelp("save", "<name>", "Saves the region for future use", "edit.save");
		formatHelp("edit", "", "Shows a help page on how to handle the mine options", "edit.info");
		formatHelp("delete", "<name>", "Completely deletes a mine", "edit.info");
		formatHelp("timer", "", "Shows a help page on how to automate the mine", "edit.timer");
		formatHelp("protection", "", "Shows how to set up a mine protection", "edit.protection");
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
}
