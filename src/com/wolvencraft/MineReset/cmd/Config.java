package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;

public class Config
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("secret"))
		{
			Util.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			Help.getConfig();
			return;
		}
		else if(args.length != 2)
		{
			Util.sendInvalid(args);
			return;
		}
		
		if(args[1].equalsIgnoreCase("save"))
		{
			CommandManager.getPlugin().saveConfig();
			CommandManager.getPlugin().saveRegionData();
			CommandManager.getPlugin().saveLanguageData();
			CommandManager.getPlugin().saveSignData();
			Util.sendSuccess("Configuration saved to disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("load"))
		{
			CommandManager.getPlugin().getConfig();
			CommandManager.getPlugin().getRegionData();
			CommandManager.getPlugin().getLanguageData();
			CommandManager.getPlugin().getSignData();
			Util.sendSuccess("Configuration loaded from disc");
			return;
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
}
