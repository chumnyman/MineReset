package com.wolvencraft.MineReset.cmd;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
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
		
		
		if(args.length > 2) {
			Message.sendInvalidArguments(args);
			return;
		}
		
		
		if(args[0].equalsIgnoreCase("select")) {
			if(args.length == 1) {
				HelpCommand.getSelect();
				return;
			}
			if(args.length != 2) {
				Message.sendInvalidArguments(args);
				return;
			}
			
			Mine curMine = MineUtils.getMine(args[1]);
			if(curMine == null) {
				Message.sendInvalidMineName(args[1]);
				return;
			}
			
			CommandManager.setLocation(curMine.getFirstPoint(), 0);
			CommandManager.setLocation(curMine.getSecondPoint(), 1);
			Message.sendNote(curMine.getName(), "Referents points of the mine are selected!");
			return;
		}
		
		Location loc = null;
		String message = "";
		
		if(args[0].equalsIgnoreCase("hpos1")) {
			loc = player.getTargetBlock(null, 100).getLocation();
			CommandManager.setLocation(loc, 0);
			message = "First point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
		}
		else if(args[0].equalsIgnoreCase("pos1")) {
			loc = player.getLocation();
			CommandManager.setLocation(loc, 0);
			message = "First point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
		}
		else if(args[0].equalsIgnoreCase("hpos2")) {
			loc = player.getTargetBlock(null, 100).getLocation();
			CommandManager.setLocation(loc, 1);
			message = "Second point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
		}
		else if(args[0].equalsIgnoreCase("pos2")) {
			loc = player.getLocation();
			CommandManager.setLocation(loc, 1);
			message = "Second point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
		}
		else {
			Message.sendInvalid(args);
			return;
		}
		
		if(Util.locationsSet()) message = message + ": " + Util.getBlockCount() + " blocks";
		Message.sendSuccess (message);
		
		if(Util.locationsSet())
			Message.sendSuccess("Both reference points are set! You can now save the region.");
		
		return;
	}
}
