package com.wolvencraft.MineReset.generation;

import com.wolvencraft.MineReset.mine.Mine;


public interface BaseGenerator {
	public final String NAME = "DEFAULT";
	
	public boolean run(Mine mine);
	
	public String getName();
	
	public String getDescription();
}
