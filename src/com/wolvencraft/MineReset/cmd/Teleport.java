package com.wolvencraft.MineReset.cmd;

import org.bukkit.ChatColor;
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
			Util.setRegionDouble(baseNode + ".pos2.x", loc.getX());
			Util.setRegionDouble(baseNode + ".pos2.y", loc.getY());
			Util.setRegionDouble(baseNode + ".pos2.z", loc.getZ());
			Util.setRegionDouble(baseNode + ".pos2.yaw", loc.getYaw());
			Util.setRegionDouble(baseNode + ".pos2.pitch", loc.getPitch());
			
			Util.saveRegionData();
			Util.sendSuccess ("Mine spawn point set (" + (int)loc.getX() + ", " + (int)loc.getY() + ", " + (int)loc.getZ() + ")");
			return;
		}
		
		if(Util.mineExists(args[1]))
		{
			String message = Util.getConfigString("messages.mine-teleport");
			String displayName = Util.getRegionString("mines." + args[1] + ".display-name");
			int min = Util.getRegionInt("mines." + args[1] + ".reset.auto.data.min");
			int sec = Util.getRegionInt("mines." + args[1] + ".reset.auto.data.sec");
			String time = ChatColor.GOLD + "" + min + ChatColor.WHITE + " minutes, " + ChatColor.GOLD + "" + sec + ChatColor.WHITE + " seconds";
			
			if(displayName.equals("")) displayName = args[1];
			message = Util.parseString(message, "%MINENAME%", displayName);
			message = Util.parseString(message, "%TIME%", time);
			
			Player player = (Player) CommandManager.getSender();
			Util.warpToMine(player, args[1]);
			
			Util.sendSuccess(message);
			return;
		}
		else
		{
			Util.sendError("Mine '" + args[1] + "' does not exist");
			return;
		}
	}
}
