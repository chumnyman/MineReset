package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.wolvencraft.MineReset.mine.MineBlock;
import org.bukkit.material.MaterialData;


public class RandomBlock {
 
    List<MineBlock> blocks;
    
    public RandomBlock (List<MaterialData> blockList, List<String> weightList)
    {
    	blocks = new ArrayList<MineBlock>();
    	double total = 0;
    	for (String weight : weightList) {
    	    total += Double.parseDouble(weight);
    	}
    	double tally = 0;
    	for (int i = 0; i < blockList.size(); i++) {
    	    tally += Double.parseDouble(weightList.get(i)) / total;
    	    blocks.add(new MineBlock(blockList.get(i), tally));
            Message.debug("Block " + blockList.get(i).getItemTypeId() + " was assigned the tally weight of " + tally);
    	}
    }
    
    public MaterialData next()
    {
    	double r = new Random().nextDouble();
    	for (MineBlock block : blocks) {
    	    if (r <= block.getChance()) {
    	        return block.getBlock();
    	    }
    	}
    	//At this point, we've got a problem folks.
    	return null;
    }

}