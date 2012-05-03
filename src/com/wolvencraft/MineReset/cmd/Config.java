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
			CommandManager.getPlugin().saveRegionData();
			CommandManager.getPlugin().saveSignData();
			Util.sendSuccess("Configuration saved to disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("load"))
		{
			CommandManager.getPlugin().reloadConfig();
			CommandManager.getPlugin().reloadRegionData();
			CommandManager.getPlugin().reloadLanguageData();
			CommandManager.getPlugin().reloadSignData();
			Util.sendSuccess("Configuration loaded from disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("generate"))
		{
			CommandManager.getPlugin().getConfig().options().copyDefaults(true);
			CommandManager.getPlugin().saveDefaultConfig();
			
			CommandManager.getPlugin().getLanguageData().options().copyDefaults(true);
			CommandManager.getPlugin().saveLanguageData();
			Util.sendSuccess("Configuration and language files generated successfully");
			return;
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
}
