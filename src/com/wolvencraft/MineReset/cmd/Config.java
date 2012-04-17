package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;

public class Config
{
	public static void run(String[] args)
	{
		if(args.length == 1)
			Help.getConfig();
		else if(args.length != 2)
		{
			Util.sendInvalid(args);
			return;
		}
		
		if(args[1].equalsIgnoreCase("save"))
		{
			CommandManager.getPlugin().saveConfig();
			CommandManager.getPlugin().saveRegionData();
			Util.sendSuccess("Configuration saved to disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("load"))
		{
			CommandManager.getPlugin().getConfig();
			CommandManager.getPlugin().getRegionData();
			Util.sendSuccess("Configuration loaded from disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("update"))
		{
			//TODO Well... where do we start.
			return;
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
}
