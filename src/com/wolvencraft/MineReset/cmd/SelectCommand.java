package com.wolvencraft.MineReset.cmd;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class SelectCommand
{
	public static void run(String[] args)
	{
		if(!Util.isPlayer() || !Util.senderHasPermission("edit"))
		{
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			HelpCommand.getSelect();
			return;
		}
		if(args.length > 2)
		{
			Message.sendInvalid(args);
			return;
		}
		
		Location loc = null;
		Player player = (Player) CommandManager.getSender();
		
		if(args[1].equalsIgnoreCase("hpos1"))
		{
			loc = player.getTargetBlock(null, 100).getLocation();
			Message.sendSuccess ("First point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
			CommandManager.setLocation(loc, 0);
		}
		else if(args[1].equalsIgnoreCase("pos1"))
		{
			loc = player.getLocation();
			Message.sendSuccess ("First point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
			CommandManager.setLocation(loc, 0);
		}
		else if(args[1].equalsIgnoreCase("hpos2"))
		{
			loc = player.getTargetBlock(null, 100).getLocation();
			Message.sendSuccess ("Second point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
			CommandManager.setLocation(loc, 1);
		}
		else if(args[1].equalsIgnoreCase("pos2"))
		{
			loc = player.getLocation();
			Message.sendSuccess ("Second point selected (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
			CommandManager.setLocation(loc, 1);
		}
		else
		{
			Message.sendInvalid(args);
			return;
		}
		
		return;
	}
}
