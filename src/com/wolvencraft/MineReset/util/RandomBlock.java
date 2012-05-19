package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomBlock {
 
    List<MineBlock> blocks;
    
    public RandomBlock (List<String> blockList, List<String> weightList)
    {
    	blocks = new ArrayList<MineBlock>();
    	double total = 0;
    	for (String weight : weightList) {
    	    total += Double.parseDouble(weight);
    	}
    	double tally = 0;
    	for (int i = 0; i < blockList.size(); i++) {
    	    tally += Double.parseDouble(weightList.get(i)) / total;
    	    blocks.add(new MineBlock(Integer.parseInt(blockList.get(i)), tally));
    	    if (Util.debugEnabled()) Message.log("Block " + Integer.parseInt(blockList.get(i)) + " was assigned the tally weight of " + tally);
    	}
    }
    
    public int next()
    {
    	double r = new Random().nextDouble();
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