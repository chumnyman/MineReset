package com.wolvencraft.MineReset.cmd;

import java.util.List;

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
			List<String> mineList = CommandManager.getPlugin().getOldConfig().getStringList("config.debug-mode");
			if(mineList == null)
			{
				Util.sendError("Unable to find config.old.yml. Aborting...");
				return;
			}
			
			for(String mineName : mineList)
			{
				if(!Util.mineExists(mineName))
				{
					Util.sendMessage("Initiating mine creation for name: " + mineName);
					if(Util.debugEnabled()) Util.log("Mine existance check passed");
					
					if(Util.debugEnabled()) Util.log("Reading default values");
					// - Fetching the default values
					// - - Is the mine enabled by default?
					boolean enabled = CommandManager.getPlugin().getOldConfig().getBoolean("regions." + mineName + ".enabled");
					
					
					// - - Blacklist defaults
					boolean blacklistEnabled = Util.getConfigBoolean("defaults.blacklist.enabled");
					boolean whitelistEnabled = Util.getConfigBoolean("defaults.blacklist.whitelist");
					List<String> blacklistedBlocks = Util.getConfigList("defaults.blacklist.blocks");
					
					
					// - - Protection defaults
					int protectionPadding = Util.getConfigInt("defaults.protection.padding");
					int protectionPaddingTop = Util.getConfigInt("defaults.protection.padding-top");
					
					// - - - PVP
					boolean protectionPVPEnabled = Util.getConfigBoolean("defaults.protection.pvp");
					
					// - - - Block breaking
					boolean protectionBreakingEnabled = Util.getConfigBoolean("defaults.protection.breaking.enabled");
					boolean protectionBreakingBlacklistEnabled = Util.getConfigBoolean("defaults.protection.breaking.blacklist.enabled");
					boolean protectionBreakingWhitelistEnabled = Util.getConfigBoolean("defaults.protection.breaking.blacklist.whitelist");
					List<String> protectionBreakingBlacklistedBlocks = Util.getConfigList("defaults.protection.breaking.blacklist.blocks");
					
					// - - - Block placement
					boolean protectionPlacingEnabled = Util.getConfigBoolean("defaults.protection.placing.enabled");
					boolean protectionPlacingBlacklistEnabled = Util.getConfigBoolean("defaults.protection.placing.blacklist.enabled");
					boolean protectionPlacingWhitelistEnabled = Util.getConfigBoolean("defaults.protection.placing.blacklist.whitelist");
					List<String> protectionPlacingBlacklistedBlocks = Util.getConfigList("defaults.protection.placing.blacklist.blocks");
					
					// - - - Coordinates
					
					String world = CommandManager.getPlugin().getOldConfig().getString("regions.coords.world");
					int[] p1 = {
							(int)CommandManager.getPlugin().getOldConfig().getDouble("regions.coords.p1.x"),
							(int)CommandManager.getPlugin().getOldConfig().getDouble("regions.coords.p1.y"),
							(int)CommandManager.getPlugin().getOldConfig().getDouble("regions.coords.p1.z")
					};
					int[] p2 = {
							(int)CommandManager.getPlugin().getOldConfig().getDouble("regions.coords.p2.x"),
							(int)CommandManager.getPlugin().getOldConfig().getDouble("regions.coords.p2.y"),
							(int)CommandManager.getPlugin().getOldConfig().getDouble("regions.coords.p2.z")
					};
					
					// - - Materials
					List<String> blockList = CommandManager.getPlugin().getOldConfig().getStringList("regions.blocks");
					List<String> weightList = CommandManager.getPlugin().getOldConfig().getStringList("regions.weights");	
					
					// - - Reset
					
					// - - - Auto
					boolean resetAutoEnabled = Util.getConfigBoolean("defaults.reset.auto.reset");
					int resetAutoTime = Util.getConfigInt("defaults.reset.auto.reset-time");
					boolean resetAutoWarnEnabled = Util.getConfigBoolean("defaults.reset.auto.warn");
					int resetAutoWarnTime = Util.getConfigInt("defaults.reset.auto.warn-time");
					
					// - - - Manual
					boolean resetManualCooldownEnabled = Util.getConfigBoolean("defaults.reset.manual.cooldown-enabled");
					int resetManualCooldownTime = Util.getConfigInt("defaults.reset.manual.cooldown-time");
					
					
					
					
					if(Util.debugEnabled()) Util.log("Finished reading defaults");
					
					// = Setting values to the mine
					String baseNode = "mines." + mineName;
					// = = Basic info
					Util.setRegionBoolean(baseNode + ".enabled", enabled);
					
					if(Util.debugEnabled()) Util.log("Writing blacklist data");
					
					// = = Blacklist
					baseNode = "mines." + mineName + ".blacklist";
					Util.setRegionBoolean(baseNode + ".enabled", blacklistEnabled);
					Util.setRegionBoolean(baseNode + ".whitelist", whitelistEnabled);
					Util.setRegionList(baseNode + ".blocks", blacklistedBlocks);
					
					if(Util.debugEnabled()) Util.log("Writing protection data");
					
					// = = Protection
					baseNode = "mines." + mineName + ".protection";
					Util.setRegionInt(baseNode + ".padding", protectionPadding);
					Util.setRegionInt(baseNode + ".padding-top", protectionPaddingTop);
					
					// = = = PVP
					Util.setRegionBoolean(baseNode + ".pvp", protectionPVPEnabled);
					
					// = = = Block breaking
					baseNode = "mines." + mineName + ".protection.breaking";
					Util.setRegionBoolean(baseNode + ".enabled", protectionBreakingEnabled);
					Util.setRegionBoolean(baseNode + ".blacklist.enabled", protectionBreakingBlacklistEnabled);
					Util.setRegionBoolean(baseNode + ".blacklist.whitelist", protectionBreakingWhitelistEnabled);
					Util.setRegionList(baseNode + ".blacklist.blocks", protectionBreakingBlacklistedBlocks);
					
					// = = = Block placement
					baseNode = "mines." + mineName + ".protection.placing";
					Util.setRegionBoolean(baseNode + ".enabled", protectionPlacingEnabled);
					Util.setRegionBoolean(baseNode + ".blacklist.enabled", protectionPlacingBlacklistEnabled);
					Util.setRegionBoolean(baseNode + ".blacklist.whitelist", protectionPlacingWhitelistEnabled);
					Util.setRegionList(baseNode + ".blacklist.blocks", protectionPlacingBlacklistedBlocks);
					
					if(Util.debugEnabled()) Util.log("Writing coordinates");
					
					// = = Coordinates
					baseNode = "mines." + mineName + ".coordinates";
					Util.setRegionString(baseNode + ".world", world);
					
					// = = = Position 0
					Util.setRegionInt(baseNode + ".pos0.x", p1[0]);
					Util.setRegionInt(baseNode + ".pos0.y", p1[1]);
					Util.setRegionInt(baseNode + ".pos0.z", p1[2]);
					
					// = = = Position 1
					Util.setRegionInt(baseNode + ".pos1.x", p2[0]);
					Util.setRegionInt(baseNode + ".pos1.y", p2[1]);
					Util.setRegionInt(baseNode + ".pos1.z", p2[2]);
							
					// = = = Position 2 (mine spawn point)
					Util.setRegionInt(baseNode + ".pos2.x", 0);
					Util.setRegionInt(baseNode + ".pos2.y", 0);
					Util.setRegionInt(baseNode + ".pos2.z", 0);
					
					if(Util.debugEnabled()) Util.log("Writing reset data");
					
					// = = Materials
					baseNode = "mines." + mineName + ".materials";
					Util.setRegionList(baseNode + ".blocks", blockList);
					Util.setRegionList(baseNode + ".weights", weightList);
					
					// = = Reset
					// = = = Automatic
					baseNode = "mines." + mineName + ".reset.auto";
					Util.setRegionBoolean(baseNode + ".reset", resetAutoEnabled);
					Util.setRegionInt(baseNode + ".reset-time", resetAutoTime);
					Util.setRegionBoolean(baseNode + ".warn", resetAutoWarnEnabled);
					Util.setRegionInt(baseNode + ".warn-time", resetAutoWarnTime);
					
					// = = = = Data
					baseNode = "mines." + mineName + ".reset.auto.data";
					Util.setRegionInt(baseNode + ".min", resetAutoTime);
					Util.setRegionInt(baseNode + ".sec", 0);
					
					// = = = Manual
					baseNode = "mines." + mineName + ".reset.manual";
					Util.setRegionBoolean(baseNode + ".cooldown-enabled", resetManualCooldownEnabled);
					Util.setRegionInt(baseNode + ".cooldown-time", resetManualCooldownTime);
					
					// = = Adding to the mine list
					List<String> tempMineList = Util.getRegionList("data.list-of-mines");
					tempMineList.add(mineName);
					Util.setRegionList("data.list-of-mines", tempMineList);
					
					if(Util.debugEnabled()) Util.log("Mine creation completed");
					
					Util.saveRegionData();
					
					if(Util.debugEnabled()) Util.log("Data saved successfully");
					Util.sendSuccess("Mine '" + mineName + "' created successfully!");
				}
				else
				{
					Util.sendError("Mine '" + mineName + "' already exists! Skipping...");
					return;
				}
			}
			return;
		}
		else
		{
			Util.sendInvalid(args);
			return;
		}
	}
}
