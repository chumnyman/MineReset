package com.wolvencraft.MineReset.cmd;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;

public class Select
{
	public static void run(String[] args)
	{
		if(!Util.isPlayer() || !Util.senderHasPermission("edit", false))
		{
			Util.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			Help.getSelect();
			return;
		}
		if(args.length > 2)
		{
			Util.sendInvalid(args);
			return;
		}
		
		Location[] loc = {null, null};
		Player player = (Player) CommandManager.getSender();
		
		if(args[1].equalsIgnoreCase("hpos1"))
		{
			loc[0] = player.getTargetBlock(null, 100).getLocation();
			Util.sendSuccess ("First point selected (" + (int)loc[0].getX() + ", " + (int)loc[0].getY() + ", " + (int)loc[0].getZ() + ")");
		}
		else if(args[1].equalsIgnoreCase("pos1"))
		{
			loc[0] = player.getLocation();
			Util.sendSuccess ("First point selected (" + (int)loc[0].getX() + ", " + (int)loc[0].getY() + ", " + (int)loc[0].getZ() + ")");
		}
		else if(args[1].equalsIgnoreCase("hpos2"))
		{
			loc[1] = player.getTargetBlock(null, 100).getLocation();
			Util.sendSuccess ("Second point selected (" + (int)loc[1].getX() + ", " + (int)loc[1].getY() + ", " + (int)loc[1].getZ() + ")");
		}
		else if(args[1].equalsIgnoreCase("pos2"))
		{
			loc[1] = player.getLocation();
			Util.sendSuccess ("Second point selected (" + (int)loc[1].getX() + ", " + (int)loc[1].getY() + ", " + (int)loc[1].getZ() + ")");
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
		
		CommandManager.setLocation(loc);
	}
}
