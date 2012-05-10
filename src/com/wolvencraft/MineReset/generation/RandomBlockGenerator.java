package com.wolvencraft.MineReset.generation;

import java.util.ArrayList;
import java.util.List;

import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class RandomBlockGenerator {
 
    List<MineBlock> blocks;
    
    public RandomBlockGenerator (List<String> blockList, List<String> weightList)
    {
    	blocks = new ArrayList<MineBlock>();
    	double total = 0;
    	for (String weight : weightList) {
    	    total += Double.parseDouble(weight);
    	}
    	double tally = 0;
    	for (int i = 0; i < blockList.size(); i++) {
    	    tally += Double.parseDouble(weightList.get(i)) / total;
    	    blocks.add(new MineBlock(Integer.parseInt(blockList.get(i)), Double.parseDouble(weightList.get(i))));
    	    if (Util.debugEnabled()) Message.log("Block " + Integer.parseInt(blockList.get(i)) + " was assigned the tally weight of " + tally);
    	}
    }
    
    public int next()
    {
    	double r = Math.random();
    	for (MineBlock block : blocks) {
    	    if (r <= block.getChance()) {
    	        return block.getBlockId();
    	    }
    	}
    	//At this point, we've got a problem folks.
    	return -1;
    }
    
    private static class MineBlock {
        private int blockId;
        private double chance;
        public MineBlock(int blockId, double chance) {
            this.blockId = blockId;
            this.chance = chance;
        }
        public int getBlockId() {
            return blockId;
        }
        public double getChance() {
            return chance;
        }
    }
}