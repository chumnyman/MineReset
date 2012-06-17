package com.wolvencraft.MineReset.generation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.RandomBlock;

public class SurfaceGenerator {
	public static void reset(Mine curMine) {
		RandomBlock pattern = new RandomBlock(curMine.getBlocks());
		Location one = curMine.getFirstPoint();
		Location two = curMine.getSecondPoint();
		int minY = one.getBlockY(), maxY = two.getBlockY();
		World world = curMine.getWorld();
		
    	if(curMine.getBlacklist().getEnabled())
    	{        		
    		if(curMine.getBlacklist().getWhitelist()) {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		            	if(topLevel(y, minY, maxY)) {
		            		for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
			                    BlockState original = world.getBlockAt(x, y, z).getState();
			                    MaterialData newBlock = MineUtils.getMostCommon(curMine).getBlock();
			                    if(curMine.getBlacklist().getBlocks().contains(original.getData())) {
				                    original.setType(newBlock.getItemType());
					                original.setRawData(newBlock.getData());
					                original.update();
			                    }
			                }
		            	}
		            	else {
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
    			return;
    		}
    		else {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		            	if(topLevel(y, minY, maxY)) {
		            		for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
			                    BlockState original = world.getBlockAt(x, y, z).getState();
			                    MaterialData newBlock = MineUtils.getMostCommon(curMine).getBlock();
			                    if(!curMine.getBlacklist().getBlocks().contains(original.getData())) {
				                    original.setType(newBlock.getItemType());
					                original.setRawData(newBlock.getData());
					                original.update();
			                    }
			                }
		            	}
		            	else {
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
    			return;
    		}
    	}
    	else
    	{
	        for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
	            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
	            	if(topLevel(y, minY, maxY)) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                    BlockState original = world.getBlockAt(x, y, z).getState();
		                    MaterialData newBlock = MineUtils.getMostCommon(curMine).getBlock();
			                original.setType(newBlock.getItemType());
				            original.setRawData(newBlock.getData());
				            original.update();
		                }
	            	}
	                else {
	                	for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                    BlockState original = world.getBlockAt(x, y, z).getState();
		                    MaterialData newBlock = pattern.next();
			                original.setType(newBlock.getItemType());
				            original.setRawData(newBlock.getData());
				            original.update();
		                }
	                }
	            }
	        }
	        return;
    	}
	}
	
	private static boolean topLevel(int curY, int minY, int maxY)
	{
		double percent = curY / (maxY - minY);
		if(percent >= 0.95) return true;
		else return false;
	}
}
