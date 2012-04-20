package com.wolvencraft.MineReset.cmd;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;



public class Teleport
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("warp"))
		{
			Util.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
			Help.getTeleport();
		else if(args.length != 2)
		{
			Util.sendInvalid(args);
			return;
		}
		
		if(args[1].equalsIgnoreCase("set"))
		{
			Player player = (Player) CommandManager.getSender();
			Location loc = player.getLocation();
			String mineName = CommandManager.getMine();
			if(mineName == null)
			{
				Util.sendError("Select a mine first with /mine edit <name>");
				return;
			}
			
			String baseNode = "mines." + mineName + ".coordinates";
			Util.setRegionInt(baseNode + ".pos2.x", 0);
			Util.setRegionInt(baseNode + ".pos2.y", 0);
			Util.setRegionInt(baseNode + ".pos2.z", 0);
			
			Util.saveRegionData();
			Util.sendSuccess ("Mine spawn point set (" + (int)loc.getX() + ", " + (int)loc.getY() + ", " + (int)loc.getZ() + ")");
			return;
		}
		
		if(Util.mineExists(args[1]))
		{
			Player player = (Player) CommandManager.getSender();
			Util.warpToMine(player, args[1]);
			
			Util.sendSuccess("You have teleported to mine + '" + args[1] + "'");
			return;
		}
		else
		{
			Util.sendError("Mine '" + args[1] + "' does not exist");
			return;
		}
	}
}
