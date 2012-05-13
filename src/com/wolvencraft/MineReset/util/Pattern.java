package com.wolvencraft.MineReset.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pattern {
 
    List<String> bin;
    
    public Pattern (List<String> blockList, List<String> weightList)
    {
    	bin = new ArrayList<String>(100000);
    	
    	int counter = 0;
    	if(Util.debugEnabled()) Message.log(blockList.size() + " blocks in a queue");
    	for(int i = 0; i < blockList.size(); i++)
    	{
    		for(int j = 0; j < (Double.parseDouble(weightList.get(i)) * 1000); j++)
    		{
    			bin.add(blockList.get(i));
    			counter++;
    		}
			if(Util.debugEnabled()) Message.log("Added " + counter + " "+ blockList.get(i) + " to the bin");
			counter = 0;
    	}
    	
		if(Util.debugEnabled()) Message.log(bin.size() + " blocks in the bin");
    }
    
    public int next()
    {
    	Random seed = new Random();
    	
    	int rand = Integer.parseInt(bin.get(seed.nextInt(bin.size())));
    	return rand;
    }
}