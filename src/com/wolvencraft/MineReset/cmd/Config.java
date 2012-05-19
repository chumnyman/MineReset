package com.wolvencraft.MineReset.cmd;

import java.util.List;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Regions;
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
	
	public static boolean update()
	{
		boolean updated = false;
		
		String baseNode = "defaults.reset.auto";
		if(Configuration.exists(baseNode + ".reset-time"))
		{
			CommandManager.getPlugin().getConfig().set(baseNode + ".reset-every", Configuration.getInt(baseNode + ".reset-time") * 60);
			Configuration.remove(baseNode + ".reset-time");
			updated = true;
		}
		
		List<String> mineList = Regions.getList("data.list-of-mines");
		
		
		for(String mineName : mineList)
		{
			baseNode = "mines." + mineName + ".reset.auto";
			if(Regions.exists(baseNode + ".reset-time"))
			{
				Regions.setInt(baseNode + ".reset-every", Regions.getInt(baseNode + ".reset-time") * 60);
				Regions.remove(baseNode + ".reset-time");
			}
			if(Regions.exists(baseNode + ".auto.min"))
			{
				int min = Regions.getInt(baseNode + ".auto.min");
				int sec = Regions.getInt(baseNode + ".auto.sec");
				Regions.setInt(baseNode + ".auto.next", min * 60 + sec);
				Regions.remove(baseNode + ".auto.min");
				Regions.remove(baseNode + ".auto.sec");
				updated = true;
			}
		}
		
		return updated;
	}
}
