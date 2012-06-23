package com.wolvencraft.MineReset.generation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.RandomBlock;

public class SurfaceGenerator {
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
		                	Block original = world.getBlockAt(x, y, z);
		                	MaterialData newBlock;
		                	if(surface(one, two, x, y, z))
		                		newBlock = MineUtils.getMostCommon(curMine).getBlock();
		                	else
				                newBlock = pattern.next();
			                if(curMine.getBlacklist().getBlocks().contains(original.getData()))
			                	original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
		            	}
		            }
		        }
    			return;
    		}
    		else {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		            	for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                	Block original = world.getBlockAt(x, y, z);
		                	MaterialData newBlock;
		                	if(surface(one, two, x, y, z))
		                		newBlock = MineUtils.getMostCommon(curMine).getBlock();
		                	else
				                newBlock = pattern.next();
			                if(!curMine.getBlacklist().getBlocks().contains(original.getData()))
			                	original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
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
	                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
	                	Block original = world.getBlockAt(x, y, z);
	                	MaterialData newBlock;
	                	if(surface(one, two, x, y, z))
	                		newBlock = MineUtils.getMostCommon(curMine).getBlock();
	                	else
			                newBlock = pattern.next();
			            original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
		            }
	            }
	        }
	        return;
    	}
	}
	
	private static boolean surface(Location pos1, Location pos2, int x, int y, int z)
	{
		int xDist = pos2.getBlockX() - pos1.getBlockX();
		int yDist = pos2.getBlockY() - pos1.getBlockY();
		int zDist = pos2.getBlockZ() - pos1.getBlockZ();
		
		x -= pos1.getBlockX();
		y -= pos1.getBlockY();
		z -= pos1.getBlockZ();
		
		if(x > xDist / 2) x = xDist - x;
		if(y > yDist / 2) y = yDist - y;
		if(z > zDist / 2) y = zDist - z;
		
		if(((x / xDist) < 0.1) ||
		   ((y / yDist) < 0.1) ||
		   ((z / zDist) < 0.1)) return true;
		
		return false;
	}
}
