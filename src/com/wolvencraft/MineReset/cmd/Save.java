package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.Location;

import com.wolvencraft.MineReset.CommandManager;

public class Save
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("edit"))
		{
			Util.sendDenied(args);
			return;
		}
		
		if(Util.debugEnabled()) Util.log("Intitiating the creation of a new mine");
		if(args.length > 2)
		{
			Util.sendInvalid(args);
			return;
		}
		
		if(Util.debugEnabled()) Util.log("Argument check passed");
		
		Location[] loc = CommandManager.getLocation();
		if(loc == null || loc[0] == null || loc[1] == null)
		{
			Util.sendError("Make a selection first");
			return;
		}
		
		if(Util.debugEnabled()) Util.log(loc[0].getWorld().getName() + " ?= " + loc[1].getWorld().getName());
		
		if(!loc[0].getWorld().getName().equalsIgnoreCase(loc[1].getWorld().getName()))
		{
			Util.sendError("Your selection points are in different worlds");
			return;
		}
		
		if(Util.debugEnabled()) Util.log("Selections checks passed");
		
		String mineName;
		if(args.length == 1)
		{
			mineName = Util.getConfigString("configuration.default-name");
		}
		else mineName = args[1];
		if(Util.mineExists(mineName))
		{
			Util.sendError("Mine '" + mineName + "' already exists!");
			return;
		}
		if(Util.debugEnabled()) Util.log("Mine existance check passed");
		
		if(Util.debugEnabled()) Util.log("Reading default values");
		// - Fetching the default values
		// - - Is the mine enabled by default?
		String displayName = Util.getConfigString("defaults.display-name");
		
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
		boolean protectionPlacingBlacklistEnabled = Util.getConfigBoolean("defaults.protection.placement.blacklist.enabled");
		boolean protectionPlacingWhitelistEnabled = Util.getConfigBoolean("defaults.protection.placement.blacklist.whitelist");
		List<String> protectionPlacingBlacklistedBlocks = Util.getConfigList("defaults.protection.placement.blacklist.blocks");
		
		// - - - Coordinates
		
		int[] p1 = new int[3];
		int[] p2 = new int[3];
		
		if((int)loc[0].getX() < (int)loc[1].getX())
		{
			p1[0] = (int) Math.round(loc[0].getX());
			p2[0] = (int) Math.round(loc[1].getX());
		}
		else
		{
			p2[0] = (int) Math.round(loc[0].getX());
			p1[0] = (int) Math.round(loc[1].getX());
		}
		
		if((int)loc[0].getY() < (int)loc[1].getY())
		{
			p1[1] = (int) Math.round(loc[0].getY());
			p2[1] = (int) Math.round(loc[1].getY());
		}
		else
		{
			p2[1] = (int) Math.round(loc[0].getY());
			p1[1] = (int) Math.round(loc[1].getY());
		}
		
		if((int)loc[0].getZ() < (int)loc[1].getZ())
		{
			p1[2] = (int) Math.round(loc[0].getZ());
			p2[2] = (int) Math.round(loc[1].getZ());
		}
		else
		{
			p2[2] = (int) Math.round(loc[0].getZ());
			p1[2] = (int) Math.round(loc[1].getZ());
		}
		
		// - - Materials
		List<String> blockList = Util.getConfigList("defaults.materials.blocks");
		List<String> weightList = Util.getConfigList("defaults.materials.weights");	
		
		// - - Reset
		
		// - - - Auto
		boolean resetAutoEnabled = Util.getConfigBoolean("defaults.reset.auto.reset");
		int resetAutoTime = Util.getConfigInt("defaults.reset.auto.reset-time");
		boolean resetAutoWarnEnabled = Util.getConfigBoolean("defaults.reset.auto.warn");
		List<String> resetAutoWarnTime = Util.getConfigList("defaults.reset.auto.warn-times");
		
		// - - - Manual
		boolean resetManualCooldownEnabled = Util.getConfigBoolean("defaults.reset.manual.cooldown-enabled");
		int resetManualCooldownTime = Util.getConfigInt("defaults.reset.manual.cooldown-time");
		
		
		
		
		if(Util.debugEnabled()) Util.log("Finished reading defaults");
		
		// = Setting values to the mine
		String baseNode = "mines." + mineName;
		// = = Basic info
		Util.setRegionString(baseNode + ".display-name", displayName);
		
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
		baseNode = "mines." + mineName + ".protection.placement";
		Util.setRegionBoolean(baseNode + ".enabled", protectionPlacingEnabled);
		Util.setRegionBoolean(baseNode + ".blacklist.enabled", protectionPlacingBlacklistEnabled);
		Util.setRegionBoolean(baseNode + ".blacklist.whitelist", protectionPlacingWhitelistEnabled);
		Util.setRegionList(baseNode + ".blacklist.blocks", protectionPlacingBlacklistedBlocks);
		
		if(Util.debugEnabled()) Util.log("Writing coordinates");
		
		// = = Coordinates
		baseNode = "mines." + mineName + ".coordinates";
		Util.setRegionString(baseNode + ".world", loc[0].getWorld().getName());
		
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
		Util.setRegionList(baseNode + ".warn-times", resetAutoWarnTime);
		
		// = = = = Data
		baseNode = "mines." + mineName + ".reset.auto.data";
		Util.setRegionInt(baseNode + ".min", resetAutoTime);
		Util.setRegionInt(baseNode + ".sec", 0);
		
		// = = = Manual
		baseNode = "mines." + mineName + ".reset.manual";
		Util.setRegionBoolean(baseNode + ".cooldown-enabled", resetManualCooldownEnabled);
		Util.setRegionInt(baseNode + ".cooldown-time", resetManualCooldownTime);
		
		// = = Adding to the mine list
		List<String> mineList = Util.getRegionList("data.list-of-mines");
		mineList.add(mineName);
		Util.setRegionList("data.list-of-mines", mineList);
		
		if(Util.debugEnabled()) Util.log("Mine creation completed");
		
		CommandManager.setLocation(null, 0);
		CommandManager.setLocation(null, 1);
		
		Util.saveRegionData();
		
		if(Util.debugEnabled()) Util.log("Data saved successfully");
		CommandManager.setMine(mineName);
		Util.sendSuccess("Mine '" + mineName + "' created successfully!");
		return;
	}
}
