package com.wolvencraft.MineReset.generation;

import com.wolvencraft.MineReset.mine.Mine;


public interface BaseGenerator {
	public boolean run(Mine mine);
	
	public String getName();
	
	public String getDescription();
}
