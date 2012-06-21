package com.wolvencraft.MineReset.generation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.RandomBlock;

public class RandomGenerator {
	public static void reset(Mine curMine) {
		RandomBlock pattern = new RandomBlock(curMine.getBlocks());
		Location one = curMine.getFirstPoint();
		Location two = curMine.getSecondPoint();
		World world = curMine.getWorld();
		
    	if(curMine.getBlacklist().getEnabled())
    	{        		
    		if(curMine.getBlacklist().getWhitelist()) {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                    BlockState original = world.getBlockAt(x, y, z).getState();
		                    MaterialData newBlock = pattern.next();
		                    if(curMine.getBlacklist().getBlocks().contains(original.getData())) {
			                    original.setType(newBlock.getItemType());
				                original.setRawData(newBlock.getData());
				                original.update();
		                    }
		                }
		            }
		        }
    		}
    		else {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                    BlockState original = world.getBlockAt(x, y, z).getState();
		                    MaterialData newBlock = pattern.next();
		                    if(!curMine.getBlacklist().getBlocks().contains(original.getData())) {
			                    original.setType(newBlock.getItemType());
				                original.setRawData(newBlock.getData());
				                original.update();
		                    }
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
	                    BlockState original = world.getBlockAt(x, y, z).getState();
	                    MaterialData newBlock = pattern.next();
			            Message.debug(original.getBlock().getType() + " was replaced with " + newBlock.getItemType());
		                original.setData(newBlock);
			            original.update();
	                }
	            }
	        }
    	}
	}
}
