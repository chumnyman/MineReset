package com.wolvencraft.MineReset.generation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.Mine;

public class EmptyGenerator {
	public static void reset(Mine curMine) {
		Location one = curMine.getFirstPoint();
		Location two = curMine.getSecondPoint();
		World world = curMine.getWorld();
		MaterialData newBlock = new MaterialData(Material.AIR);
		
    	if(curMine.getBlacklist().getEnabled())
    	{        		
    		if(curMine.getBlacklist().getWhitelist()) {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                	Block original = world.getBlockAt(x, y, z);
		                    if(curMine.getBlacklist().getBlocks().contains(original.getData()))
				                original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
		                }
		            }
		        }
    		}
    		else {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                	Block original = world.getBlockAt(x, y, z);
		                    if(!curMine.getBlacklist().getBlocks().contains(original.getData()))
				                original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
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
	                	Block original = world.getBlockAt(x, y, z);
		                original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
	                }
	            }
	        }
    	}
	}
}
