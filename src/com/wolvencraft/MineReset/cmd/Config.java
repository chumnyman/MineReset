package com.wolvencraft.MineReset.cmd;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.Util;

public class Config
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("secret"))
		{
			Message.sendDenied(args);
			return;
		}
		
		if(args.length == 1)
		{
			Help.getConfig();
			return;
		}
		else if(args.length != 2)
		{
			Message.sendInvalid(args);
			return;
		}
		
		if(args[1].equalsIgnoreCase("save"))
		{
			CommandManager.getPlugin().saveRegionData();
			CommandManager.getPlugin().saveSignData();
			Message.sendSuccess("Configuration saved to disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("load"))
		{
			CommandManager.getPlugin().reloadConfig();
			CommandManager.getPlugin().reloadRegionData();
			CommandManager.getPlugin().reloadLanguageData();
			CommandManager.getPlugin().reloadSignData();
			Message.sendSuccess("Configuration loaded from disc");
			return;
		}
		else if(args[1].equalsIgnoreCase("generate"))
		{
			CommandManager.getPlugin().getConfig().options().copyDefaults(false);
			CommandManager.getPlugin().saveDefaultConfig();
			
			CommandManager.getPlugin().getLanguageData().options().copyDefaults(false);
			CommandManager.getPlugin().saveLanguageData();
			Message.sendSuccess("Configuration and language files generated successfully");
			return;
		}
		else
		{
			Message.sendInvalid(args);
			return;
		}
	}
}
