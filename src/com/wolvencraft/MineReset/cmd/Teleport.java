package com.wolvencraft.MineReset.cmd;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;



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
			Regions.setDouble(baseNode + ".pos2.x", loc.getX());
			Regions.setDouble(baseNode + ".pos2.y", loc.getY());
			Regions.setDouble(baseNode + ".pos2.z", loc.getZ());
			Regions.setDouble(baseNode + ".pos2.yaw", loc.getYaw());
			Regions.setDouble(baseNode + ".pos2.pitch", loc.getPitch());
			
			Regions.saveData();
			Util.sendSuccess ("Mine spawn point set (" + (int)loc.getX() + ", " + (int)loc.getY() + ", " + (int)loc.getZ() + ")");
			return;
		}
		
		if(Util.mineExists(args[1]))
		{
			String message = Language.getString("teleportation.mine-teleport");
			
			message = Util.parseVars(message, args[1]);
			
			Player player = (Player) CommandManager.getSender();
			Util.warpToMine(player, args[1]);
			
			Util.sendSuccess(message);
			return;
		}
		else
		{
			String error = Language.getString("general.mine-name-invalid");
			error = Util.parseString(error, "%MINE%", args[1]);
			Util.sendError(error);
			return;
		}
	}
}
