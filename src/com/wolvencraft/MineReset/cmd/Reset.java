package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Pattern;

public class Reset
{
	public static void run(String[] args, boolean automatic)
	{	
		if(args.length > 2)
		{
			Util.sendInvalid(args);
			return;
		}
		String mineName = null;
		if(args.length == 1)
		{
			mineName = CommandManager.getMine();

			if(mineName == null)
			{
				Help.getReset();
				return;
			}
		}
		
		if(mineName == null)
			mineName = args[1];
		
		if(!Util.mineExists(mineName))
		{
			String error = Language.getString("general.mine-name-invalid");
			error = Util.parseString(error, "%MINE%", mineName);
			Util.sendError(error);
			return;
		}
		
		if(!automatic)
		{
			boolean manPerm = Util.senderHasPermission("reset.manual");
			boolean minPerm = Util.senderHasPermission("reset.manual." + mineName);
			if(!manPerm && !minPerm)
			{
				Util.sendDenied(args);
				return;
			}
		}
		
		List<String> blockList = Regions.getList("mines." + mineName + ".materials.blocks");
		List<String> weightList = Regions.getList("mines." + mineName + ".materials.weights");
		Pattern pattern = new Pattern(blockList, weightList);
		boolean blacklist = Regions.getBoolean("mines." + mineName + ".blacklist.enabled");
		boolean whitelist = Regions.getBoolean("mines." + mineName + ".blacklist.whitelist");
		List<String> blacklistedBlocks = null;
		if(blacklist)
			blacklistedBlocks = Regions.getList("mines." + mineName + ".blacklist.blocks");
		String worldName = Regions.getString("mines." + mineName + ".coordinates.world");
		World mineWorld = Bukkit.getServer().getWorld(worldName);
		
		boolean broadcastReset = Configuration.getBoolean("configuration.broadcast-on-reset");
		
		if(Util.debugEnabled()) Util.log("Determining coordinates");
		
		int[] point1 = {
				Regions.getInt("mines." + mineName + ".coordinates.pos0.x"),
				Regions.getInt("mines." + mineName + ".coordinates.pos0.y"),
				Regions.getInt("mines." + mineName + ".coordinates.pos0.z")
		};
		int[] point2 = {
				Regions.getInt("mines." + mineName + ".coordinates.pos1.x"),
				Regions.getInt("mines." + mineName + ".coordinates.pos1.y"),
				Regions.getInt("mines." + mineName + ".coordinates.pos1.z")
		};
		
		int blockID = 0;
		if(Util.debugEnabled()) Util.log("x " + point1[0] + ", " + point2[0]);
		if(Util.debugEnabled()) Util.log("y " + point1[1] + ", " + point2[1]);
		if(Util.debugEnabled()) Util.log("z " + point1[2] + ", " + point2[2]);
				
		if(Configuration.getBoolean("configuration.teleport-out-of-the-mine-on-reset"))
		{
			Player[] onlinePlayers = Bukkit.getOnlinePlayers();
			for(Player player : onlinePlayers)
			{
				if(Util.playerInTheMine(player, mineName))
				{
					String message = Language.getString("teleportation.mine-teleport");
					String displayName = Regions.getString("mines." + mineName + ".display-name");
					int min = Regions.getInt("mines." + mineName + ".reset.auto.data.min");
					int sec = Regions.getInt("mines." + mineName + ".reset.auto.data.sec");
					String time = ChatColor.GOLD + "" + min + ChatColor.WHITE + " minutes, " + ChatColor.GOLD + "" + sec + ChatColor.WHITE + " seconds";
					
					if(displayName.equals("")) displayName = mineName;
					message = Util.parseString(message, "%MINENAME%", displayName);
					message = Util.parseString(message, "%TIME%", time);
					
					Util.warpToMine(player, mineName);
					
					Util.sendSuccess(message);
					
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
						String blockTypeId = b.getTypeId() + "";
						for(String block : blacklistedBlocks)
						{
							if(blockTypeId.equals(block))
							{
								if(Util.debugEnabled()) Util.log(blockID + " is blacklisted and thus was not replaced");
							}
							else
							{
								blockID = pattern.next();
								b.setTypeId(blockID);
								if(Util.debugEnabled()) Util.log(blockTypeId + " was replaced with " + blockID);
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
						String blockTypeId = b.getTypeId() + "";
						for(String block : blacklistedBlocks)
						{
							if(blockTypeId.equals(block))
							{
								blockID = pattern.next();
								b.setTypeId(blockID);
								if(Util.debugEnabled()) Util.log(blockTypeId + " was replaced with " + blockID);
							}
							else
							{
								if(Util.debugEnabled()) Util.log(blockTypeId + " is not whitelisted and thus not replaced");
							}
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
				for(int x = point1[0]; x <= point2[0]; x++)
				{
					for(int z = point1[2]; z <= point2[2]; z++ )
					{
						Block b = mineWorld.getBlockAt(x, y, z);
						blockID = pattern.next();
						b.setTypeId(blockID);
					}
				}
			}
		}
		
		boolean silent = Regions.getBoolean("mines." + mineName + ".silent");
		int nextReset = Regions.getInt("mines." + mineName + ".reset.auto.reset-time");
		Regions.setInt("mines." + mineName + ".reset.auto.data.min", nextReset);
		Regions.setInt("mines." + mineName + ".reset.auto.data.sec", 0);
		Regions.saveData();
		
		if(broadcastReset)
		{
			String broadcastMessage;
			if(automatic)
			{
				broadcastMessage = Language.getString("reset.automatic-reset-successful");
			}
			else
			{
				broadcastMessage = Language.getString("reset.manual-reset-successful");
			}
			
				String displayName = Regions.getString("mines." + mineName + ".display-name");
				if(displayName.equals("")) displayName = mineName;
				broadcastMessage = Util.parseVars(broadcastMessage, mineName);
				
				if(!silent)
				{
					Util.broadcastSuccess(broadcastMessage);
				}
				else if(!automatic)
				{
					Util.sendSuccess(broadcastMessage);
				}
		}
		return;
	}
}
