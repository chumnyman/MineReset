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
	
	public static void update()
	{	
		CommandManager.getPlugin().getConfig().set("configuration.version", "1.2.2");
		
		Message.debug("Updating the configuration file . . .");
		
		String baseNode = "defaults.reset.auto";
		if(Configuration.exists(baseNode + ".reset-time"))
		{
			CommandManager.getPlugin().getConfig().set(baseNode + ".reset-every", Configuration.getInt(baseNode + ".reset-time") * 60);
			Configuration.remove(baseNode + ".reset-time");
		}
		
		List<String> warnTimes = Configuration.getList("defaults.reset.auto.warn-times");
		for(String time : warnTimes)
		{
			warnTimes.set(warnTimes.indexOf(time), "" + Integer.parseInt(time) * 60);
		}
		
		CommandManager.getPlugin().getConfig().set("defaults.reset.auto.warn-times", warnTimes);
		
		List<String> materialList = Configuration.getList("defaults.materials.blocks");
		for(String block : materialList)
		{
			if(block.indexOf(":") == -1)
			{
				materialList.set(materialList.indexOf(block), block + ":0");
			}
		}
		
		CommandManager.getPlugin().getConfig().set("defaults.materials.blocks", materialList);

		
		
		
		
		Message.debug("Updating the regions file . . .");
		
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
			}
			
			
			List<String> blacklist = Regions.getList("mines." + mineName + ".blacklist.blocks");
			for(String block : blacklist)
			{
				if(block.indexOf(":") == -1)
				{
					blacklist.set(blacklist.indexOf(block), block + ":0");
				}
			}
			Regions.setList("mines." + mineName + ".blacklist.blocks", blacklist);
			
			List<String> protBlacklist = Regions.getList("mines." + mineName + ".protection.breaking.blacklist.blocks");
			for(String block : protBlacklist)
			{
				if(block.indexOf(":") == -1)
				{
					protBlacklist.set(protBlacklist.indexOf(block), block + ":0");
				}
			}
			Regions.setList("mines." + mineName + ".protection.breaking.blacklist.blocks", protBlacklist);
			
			protBlacklist = Regions.getList("mines." + mineName + ".protection.placement.blacklist.blocks");
			for(String block : protBlacklist)
			{
				if(block.indexOf(":") == -1)
				{
					protBlacklist.set(protBlacklist.indexOf(block), block + ":0");
				}
			}
			Regions.setList("mines." + mineName + ".protection.placement.blacklist.blocks", protBlacklist);
			
			warnTimes = Configuration.getList("mines." + mineName + ".reset.auto.warn-times");
			for(String time : warnTimes)
			{
				warnTimes.set(warnTimes.indexOf(time), "" + Integer.parseInt(time) * 60);
			}
			Regions.setList("mines." + mineName + ".reset.auto.warn-times", warnTimes);
			
			materialList = Regions.getList("mines." + mineName + ".materials.blocks");
			for(String block : materialList)
			{
				if(block.indexOf(":") == -1)
				{
					materialList.set(materialList.indexOf(block), block + ":0");
				}
			}
			Regions.setList("mines." + mineName + ".materials.blocks", materialList);
		}
		
		Regions.saveData();
		
		return;
	}
}
