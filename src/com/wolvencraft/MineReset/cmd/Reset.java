package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.util.Pattern;

public class Reset
{
	public static void run(String[] args, boolean automatic)
	{
		if(!automatic && !Util.senderHasPermission("reset"))
		{
			Util.sendDenied(args);
			return;
		}
		
		if(args.length > 2)
		{
			Util.sendInvalid(args);
			return;
		}
		if(args.length == 1)
		{
			if(automatic) return;
			Util.sendInvalid(args);
			//Help.getReset();
			return;
		}
		
		String mineName = args[1];
		
		if(!Util.mineExists(mineName))
		{
			Util.sendError("Mine '" + mineName + "' does not exist");
			return;
		}
		
		List<String> blockList = Util.getRegionList("mines." + mineName + ".materials.blocks");
		List<String> weightList = Util.getRegionList("mines." + mineName + ".materials.weights");
		Pattern pattern = new Pattern(blockList, weightList);
		boolean blacklist = Util.getRegionBoolean("mines." + mineName + ".blacklist.enabled");
		boolean whitelist = Util.getRegionBoolean("mines." + mineName + ".blacklist.whitelist");
		List<String> blacklistedBlocks = null;
		if(blacklist)
			blacklistedBlocks = Util.getRegionList("mines." + mineName + ".blacklist.blocks");
		String worldName = Util.getRegionString("mines." + mineName + ".coordinates.world");
		World mineWorld = Bukkit.getServer().getWorld(worldName);
		
		boolean broadcastReset = Util.getConfigBoolean("configuration.broadcast-on-reset");
		
		if(Util.debugEnabled()) Util.log("Determining coordinates");
		
		int[] point1 = {
				Util.getRegionInt("mines." + mineName + ".coordinates.pos0.x"),
				Util.getRegionInt("mines." + mineName + ".coordinates.pos0.y"),
				Util.getRegionInt("mines." + mineName + ".coordinates.pos0.z")
		};
		int[] point2 = {
				Util.getRegionInt("mines." + mineName + ".coordinates.pos1.x"),
				Util.getRegionInt("mines." + mineName + ".coordinates.pos1.y"),
				Util.getRegionInt("mines." + mineName + ".coordinates.pos1.z")
		};
		
		int blockID = 0;
		if(Util.debugEnabled()) Util.log("x " + point1[0] + ", " + point2[0]);
		if(Util.debugEnabled()) Util.log("y " + point1[1] + ", " + point2[1]);
		if(Util.debugEnabled()) Util.log("z " + point1[2] + ", " + point2[2]);
		
		// TODO I hate the way this is done.
		
		if(Util.getConfigBoolean("configuration.teleport-out-of-the-mine-on-reset"))
		{
			Player[] onlinePlayers = Bukkit.getOnlinePlayers();
			for(Player player : onlinePlayers)
			{
				if(Util.playerInTheMine(player, mineName))
				{
					if(Util.debugEnabled()) Util.log("Player " + player.getName() + " is in the mine");
					
					if(!Util.nodeIsValid("mines." + mineName + ".coordinates.pos2.x"))
					{
						if(Util.debugEnabled()) Util.log("Coordinates of the mine spawn point are not set");
					}
					else
					{
						Util.warpToMine(player, mineName);
						Util.sendSuccess("You have teleported to mine + '" + args[1] + "'");
					}
				}
				if(Util.debugEnabled()) Util.log("Player " + player.getName() + " is not in the mine");
			}
		}
		
		// Comment for the future me:
		// It makes more sense to handle all the logic here, since
		// doing the same thing in the middle of resetting a bunch
		// of blocks is clearly not the best idea ever. Best wishes,
		// me from the past.
		
		if(blacklist && !whitelist)
		{
			if(Util.debugEnabled()) Util.log("Blacklist detected");
			for(int y = point1[1]; y <= point2[1]; y++)
			{
				for(int x = point1[0]; x <= point2[0]; x++)
				{
					for(int z = point1[2]; z <= point2[2]; z++ )
					{
						Block b = mineWorld.getBlockAt(x, y, z);
						for(String block : blacklistedBlocks)
						{
							String blockTypeId = b.getTypeId() + "";
							if(blockTypeId.equals(block))
								if(Util.debugEnabled()) Util.log(blockID + " is blacklisted and thus was not replaced");
							else
							{
								blockID = pattern.next();
								b.setTypeId(blockID);
							}	
						}
					}
				}
			}
		}
		else if(blacklist && whitelist)
		{
			if(Util.debugEnabled()) Util.log("Whitelist detected");
			for(int y = point1[1]; y <= point2[1]; y++)
			{
				for(int x = point1[0]; x <= point2[0]; x++)
				{
					for(int z = point1[2]; z <= point2[2]; z++ )
					{
						Block b = mineWorld.getBlockAt(x, y, z);
						for(String block : blacklistedBlocks)
						{
							String blockTypeId = b.getTypeId() + "";
							if(blockTypeId.equals(block))
							{
								blockID = pattern.next();
								b.setTypeId(blockID);
							}
							else
								if(Util.debugEnabled()) Util.log(blockID + " is not whitelisted and thus not replaced");	
						}
					}
				}
			}
		}
		else
		{
			if(Util.debugEnabled()) Util.log("No blacklist detected");
			for(int y = point1[1]; y <= point2[1]; y++)
			{
				if(Util.debugEnabled()) Util.log("Parsing y " + y);
				for(int x = point1[0]; x <= point2[0]; x++)
				{
					if(Util.debugEnabled()) Util.log("Parsing x " + x);
					for(int z = point1[2]; z <= point2[2]; z++ )
					{
						Block b = mineWorld.getBlockAt(x, y, z);
						blockID = pattern.next();
						b.setTypeId(blockID);
					}
				}
			}
		}
			
		int nextReset = Util.getRegionInt("mines." + mineName + ".reset.auto.reset-time");
		Util.setRegionInt("mines." + mineName + ".reset.auto.data.min", nextReset);
		Util.setRegionInt("mines." + mineName + ".reset.auto.data.min", 0);
		
		if(broadcastReset)
		{
			String broadcastMessage;
			if(automatic)
			{
				broadcastMessage = Util.getConfigString("messages.automatic-mine-reset-successful");
			}
			else
			{
				broadcastMessage = Util.getConfigString("messages.manual-mine-reset-successful");
			}
			
				broadcastMessage = Util.parseString(broadcastMessage, "%MINE%", "'" + mineName + "'");
				broadcastMessage = Util.parseString(broadcastMessage, "%TIME%", nextReset+"");
				
				Util.broadcastSuccess(broadcastMessage);
		}
	}
}
