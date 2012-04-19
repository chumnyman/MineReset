package com.wolvencraft.MineReset.cmd;

import java.util.List;

public class MineList
{
	public static void run(String[] args)
	{
		List<String> mineList = Util.getRegionList("data.list-of-mines");
		
		Util.sendMessage("                    -=[ Public Mines ]=-");
		
		for(String s : mineList)
		{
			Util.sendMessage(" - " + s + "");
		}
	}
}