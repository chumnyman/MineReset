package com.wolvencraft.MineReset.cmd;

public interface BaseCommand {
	public boolean run(String[] args);
	
	public void getHelp();
}
