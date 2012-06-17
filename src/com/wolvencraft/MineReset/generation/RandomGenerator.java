package com.wolvencraft.MineReset.generation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.Mine;
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
		                    MaterialData original = new MaterialData(world.getBlockAt(x, y, z).getType());
		                    MaterialData newBlock = pattern.next();
		                    if(curMine.getBlacklist().getBlocks().contains(original))
		                    {
			                    if(original.getItemType() != newBlock.getItemType() && original.getData() != newBlock.getData()) {
				                    world.getBlockAt(x, y, z).setType(newBlock.getItemType());
				                    world.getBlockAt(x, y, z).setData(newBlock.getData());
			                	}
		                    }
		                }
		            }
		        }
    		}
    		else {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                    MaterialData original = new MaterialData(world.getBlockAt(x, y, z).getType());
		                    MaterialData newBlock = pattern.next();
		                    if(!curMine.getBlacklist().getBlocks().contains(original))
		                    {
			                    if(original.getItemType() != newBlock.getItemType() && original.getData() != newBlock.getData()) {
				                    world.getBlockAt(x, y, z).setType(newBlock.getItemType());
				                    world.getBlockAt(x, y, z).setData(newBlock.getData());
			                	}
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
	                	MaterialData original = new MaterialData(world.getBlockAt(x, y, z).getType());
	                    MaterialData newBlock = pattern.next();
	                    if(original.getItemType() != newBlock.getItemType() && original.getData() != newBlock.getData()) {
		                    world.getBlockAt(x, y, z).setType(newBlock.getItemType());
		                    world.getBlockAt(x, y, z).setData(newBlock.getData());
	                	}
	                }
	            }
	        }
    	}
	}
}
