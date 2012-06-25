package com.wolvencraft.MineReset.generation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import com.wolvencraft.MineReset.mine.Mine;
import com.wolvencraft.MineReset.util.RandomBlock;

public class RandomGenerator implements BaseGenerator {
	public final String NAME;
	public final String DESCRIPTION;
	
	public RandomGenerator() {
		NAME = "RANDOM";
		DESCRIPTION = "Resets the contents of the mine randomly according to the persentage set by the mine configuration";
	}
	
	public boolean run(Mine mine) {
		RandomBlock pattern = new RandomBlock(mine.getBlocks());
		Location one = mine.getFirstPoint();
		Location two = mine.getSecondPoint();
		World world = mine.getWorld();
		
    	if(mine.getBlacklist().getEnabled()) {        		
    		if(mine.getBlacklist().getWhitelist()) {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                	Block original = world.getBlockAt(x, y, z);
		                    MaterialData newBlock = pattern.next();
		                    if(mine.getBlacklist().getBlocks().contains(original.getState().getData()))
				                original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
		                }
		            }
		        }
    			return true;
    		}
    		else {
    			for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
		            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
		                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
		                	Block original = world.getBlockAt(x, y, z);
		                    MaterialData newBlock = pattern.next();
		                    if(!mine.getBlacklist().getBlocks().contains(original.getState().getData()))
				                original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
		                }
		            }
		        }
    			return true;
    		}
    	}
    	else {
	        for (int x = one.getBlockX(); x <= two.getBlockX(); x++) {
	            for (int y = one.getBlockY(); y <= two.getBlockY(); y++) {
	                for (int z = one.getBlockZ(); z <= two.getBlockZ(); z++) {
	                	Block original = world.getBlockAt(x, y, z);
	                    MaterialData newBlock = pattern.next();
		                original.setTypeIdAndData(newBlock.getItemTypeId(), newBlock.getData(), false);
	                }
	            }
	        }
	        return true;
    	}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}