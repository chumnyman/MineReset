package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.wolvencraft.MineReset.mine.MineBlock;
import org.bukkit.material.MaterialData;


public class RandomBlock {
 
    List<MineBlock> weightedBlocks;

    public RandomBlock(List<MineBlock> blocks) {
    	weightedBlocks = new ArrayList<MineBlock>();
        double tally = 0;
        for (MineBlock block : blocks) {
            tally += block.getChance();
            weightedBlocks.add(new MineBlock(block.getBlock(), tally));
            Message.debug("Block " + block.getBlock().getItemTypeId() + " was assigned the tally weight of " + tally);
        }
    }
    
    public MaterialData next()
    {
    	double r = new Random().nextDouble();
    	for (MineBlock block : weightedBlocks) {
    	    if (r <= block.getChance()) {
    	        return block.getBlock();
    	    }
    	}
    	//At this point, we've got a problem folks.
    	return null;
    }

}