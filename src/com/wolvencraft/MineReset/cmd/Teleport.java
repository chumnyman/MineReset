package com.wolvencraft.MineReset.cmd;

import org.bukkit.command.CommandSender;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.MineReset;

public class Teleport
{
	public static void run()
	{
		CommandSender sender = CommandManager.getSender();
		MineReset plugin = CommandManager.getPlugin();
		
		// This function should allow the user to teleport a player to each
		// individual mine. Checks for the correctness of arguments should be included.
		
		// Help.java includes command help. Util.java has a bunch of helpful functions
		// for accessing the configuration and printing out messages to the user.
	}
}
