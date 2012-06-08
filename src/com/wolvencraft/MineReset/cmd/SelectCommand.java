package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class SelectCommand
{
	public static void run(String[] args)
	{
		Player player;
		if(CommandManager.getSender() instanceof Player) {
			player = (Player) CommandManager.getSender();
		}
		else {
			Message.sendError("This command cannot be executed via console");
			return;
		}
		if(!Util.isPlayer() || !Util.senderHasPermission("edit")) {
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1) {
			HelpCommand.getSelect();
			return;
		}
		if(args.length > 2) {
			Message.sendError("Invalid parameters. Check your argument count!");
			return;
		}
		
		Location loc = null;
		
		if(args[1].equalsIgnoreCase("hpos1")) {
			loc = player.getTargetBlock(null, 100).getLocation();
			CommandManager.setLocation(loc, 0);
			String message = "First point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
			if(Util.locationsSet()) message = message + ": " + Util.getBlockCount() + " blocks";
			Message.sendSuccess (message);
		}
		else if(args[1].equalsIgnoreCase("pos1")) {
			loc = player.getLocation();
			CommandManager.setLocation(loc, 0);
			String message = "First point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
			if(Util.locationsSet()) message = message + ": " + Util.getBlockCount() + " blocks";
			Message.sendSuccess (message);
		}
		else if(args[1].equalsIgnoreCase("hpos2")) {
			loc = player.getTargetBlock(null, 100).getLocation();
			CommandManager.setLocation(loc, 1);
			String message = "Second point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
			if(Util.locationsSet()) message = message + ": " + Util.getBlockCount() + " blocks";
			Message.sendSuccess (message);
		}
		else if(args[1].equalsIgnoreCase("pos2")) {
			loc = player.getLocation();
			CommandManager.setLocation(loc, 1);
			String message = "Second point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
			if(Util.locationsSet()) message = message + ": " + Util.getBlockCount() + " blocks";
			Message.sendSuccess (message);
		}
		else {
			Message.sendInvalid(args);
			return;
		}
		
		if(Util.locationsSet()) {
			Message.sendSuccess("Both reference points are set! You can now save the region with " + ChatColor.GOLD + "/mine save");
		}
		
		return;
	}
}
