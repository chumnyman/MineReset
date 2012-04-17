package com.wolvencraft.MineReset.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;



public class Teleport
{
	public static void run(String[] args)
	{
		if(args.length == 1)
			Help.getTeleport();
		else if(args.length != 2)
		{
			Util.sendInvalid(args);
			return;
		}
		
		if(args[1].equalsIgnoreCase("set"))
		{
			CommandManager.getPlugin().saveConfig();
			CommandManager.getPlugin().saveRegionData();
			Util.sendSuccess("Configuration saved to disc");
			return;
		}
		
		if(Util.mineExists(args[1]))
		{
			Player player = (Player) CommandManager.getSender();
			String newLocWorld = Util.getRegionString("mines." + args[1] + ".coordinates.world");
			double[] coords = {
					Util.getRegionInt("mines." + args[1] + ".coordinates.pos2.x"),
					Util.getRegionInt("mines." + args[1] + ".coordinates.pos2.y"),
					Util.getRegionInt("mines." + args[1] + ".coordinates.pos2.z"),
			};
			Location newLoc = new Location(Bukkit.getServer().getWorld(newLocWorld), coords[0], coords[1], coords[1]);
			
			//TODO how to change the coords of a player???
		}
		else
		{
			Util.sendError("Mine '" + args[1] + "' does not exist");
		}
		
		return;
	}
}
