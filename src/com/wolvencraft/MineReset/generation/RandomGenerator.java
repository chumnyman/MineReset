package com.wolvencraft.MineReset.generation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Language;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.RandomBlock;
import com.wolvencraft.MineReset.util.Util;

public class RandomGenerator
{
	public static void run(String mineName)
	{
		List<String> tempBlockList = Regions.getList("mines." + mineName + ".materials.blocks");
		List<MaterialData> blockList = new ArrayList<MaterialData>();
		for(String tempBlock : tempBlockList)
		{
			MaterialData block = new MaterialData(Material.getMaterial(Integer.parseInt(tempBlock.split(":")[0])));
			block.setData(Byte.parseByte(tempBlock.split(":")[1]));
			blockList.add(block);
		}
		List<String> weightList = Regions.getList("mines." + mineName + ".materials.weights");
		RandomBlock pattern = new RandomBlock(blockList, weightList);
		boolean blacklist = Regions.getBoolean("mines." + mineName + ".blacklist.enabled");
		boolean whitelist = Regions.getBoolean("mines." + mineName + ".blacklist.whitelist");
		List<String> blacklistedBlocks = null;
		if(blacklist)
			blacklistedBlocks = Regions.getList("mines." + mineName + ".blacklist.blocks");
		String worldName = Regions.getString("mines." + mineName + ".coordinates.world");
		World mineWorld = Bukkit.getServer().getWorld(worldName);

        Message.debug("Determining coordinates");
		
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
		
		MaterialData blockId = null;
        Message.debug("x " + point1[0] + ", " + point2[0]);
        Message.debug("y " + point1[1] + ", " + point2[1]);
        Message.debug("z " + point1[2] + ", " + point2[2]);
				
		if(Configuration.getBoolean("lag.teleport-out-of-the-mine-on-reset"))
		{
			Player[] onlinePlayers = Bukkit.getOnlinePlayers();
			for(Player player : onlinePlayers)
			{
				if(MineUtils.playerInTheMine(player, mineName))
				{
					String message = Language.getString("teleportation.mine-teleport");
					message = Util.parseVars(message, mineName);
					
					MineUtils.warpToMine(player, mineName);
					
					String title = Language.getString("general.title");
					player.sendMessage(ChatColor.GREEN + "[" + title + "] " + ChatColor.WHITE + message);
					
				}
			}
		}
		
		// Comment for the future me:
		// It makes more sense to handle all the logic here, since
		// doing the same thing in the middle of resetting a bunch
		// of blocks is clearly not the best idea ever. Best wishes,
		// me from the past.
		
		if(blacklist && !whitelist)
		{
            Message.debug("Blacklist detected");
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
								Message.debug(blockId.getItemTypeId() + " is blacklisted and thus was not replaced");
							}
							else
							{
								blockId = pattern.next();
								b.setType(blockId.getItemType());
								b.setData(blockId.getData());
								Message.debug(blockTypeId + " was replaced with " + blockId.getItemTypeId());
							}	
						}
					}
				}
			}
		}
		else if(blacklist && whitelist)
		{
            Message.debug("Whitelist detected");
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
								blockId = pattern.next();
								b.setType(blockId.getItemType());
								b.setData(blockId.getData());
								Message.debug(blockTypeId + " was replaced with " + blockId.getItemTypeId());
							}
							else
							{
								Message.debug(blockTypeId + " is not whitelisted and thus not replaced");
							}
						}
					}
				}
			}
		}
		else
		{
            Message.debug("No blacklist detected");
			for(int y = point1[1]; y <= point2[1]; y++)
			{
				for(int x = point1[0]; x <= point2[0]; x++)
				{
					for(int z = point1[2]; z <= point2[2]; z++ )
					{
						Block b = mineWorld.getBlockAt(x, y, z);
						blockId = pattern.next();
						b.setType(blockId.getItemType());
						b.setData(blockId.getData());
					}
				}
			}
		}
		return;
	}
}
