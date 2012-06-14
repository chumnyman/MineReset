package com.wolvencraft.MineReset.generation;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.Message;

public class SnapshotGenerator {
	public static void reset(Mine curMine) {
		Location one = curMine.getFirstPoint();
		Location two = curMine.getSecondPoint();
		World world = curMine.getWorld();
		List<BlockState> blocks = curMine.getSnapshot().getBlocks();
		if(!blocks.isEmpty())
		{
			Message.sendError("Snapshot was never saved! Use " + ChatColor.GOLD + "/mine snapshot" + ChatColor.WHITE + " to save it");
		}
		int index = 0;
		if(curMine.getBlacklist().getEnabled())
    	{        		
    		if(curMine.getBlacklist().getWhitelist()) {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                    MaterialData original = new MaterialData(world.getBlockAt(x, y, z).getType());
		                    BlockState newBlock = blocks.get(index);
		                    if(curMine.getBlacklist().getBlocks().contains(original))
		                    	newBlock.update();
		                    index++;
		                }
		            }
		        }
    		}
    		else {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                    MaterialData original = new MaterialData(world.getBlockAt(x, y, z).getType());
		                    BlockState newBlock = blocks.get(index);
		                    if(!curMine.getBlacklist().getBlocks().contains(original))
		                    	newBlock.update();
		                    index++;
		                }
		            }
		        }
    		}
    	}
    	else
    	{
	        for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
	            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
	                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
	                	BlockState newBlock = blocks.get(index);
	                	newBlock.update();
	                    index++;
	                }
	            }
	        }
    	}
		return;
	}
}
