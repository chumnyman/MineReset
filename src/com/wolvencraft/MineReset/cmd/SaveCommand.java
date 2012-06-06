package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.MineReset.CommandManager;
import com.wolvencraft.MineReset.config.Configuration;
import com.wolvencraft.MineReset.config.Regions;
import com.wolvencraft.MineReset.util.Message;
import com.wolvencraft.MineReset.util.MineUtils;
import com.wolvencraft.MineReset.util.Util;

public class SaveCommand
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("edit"))
		{
			Message.sendDenied(args);
			return;
		}
		
		Message.debug("Intitiating the creation of a new mine");
		if(args.length != 2)
		{
			Message.sendInvalid(args);
			return;
		}

        Message.debug("Argument check passed");
		
		Location[] loc = CommandManager.getLocation();
		if(loc == null || loc[0] == null || loc[1] == null)
		{
			Message.sendError("Make a selection first");
			return;
		}

        Message.debug(loc[0].getWorld().getName() + " ?= " + loc[1].getWorld().getName());
		
		if(!loc[0].getWorld().getName().equalsIgnoreCase(loc[1].getWorld().getName()))
		{
			Message.sendError("Your selection points are in different worlds");
			return;
		}

        Message.debug("Selections checks passed");
		
		String mineName = args[1];
		if(MineUtils.exists(mineName))
		{
			Message.sendError("Mine '" + mineName + "' already exists!");
			return;
		}

        Message.debug("Mine existance check passed");

        Message.debug("Reading default values");
		
		// - Fetching the command sender
		
		Player player = (Player) CommandManager.getSender();
		
		// - Fetching the default values
		// - - Is the mine enabled by default?
		String displayName = Configuration.getString("defaults.display-name");
		boolean silent = Configuration.getBoolean("defaults.silent");
		
		// - - Blacklist defaults
		boolean blacklistEnabled = Configuration.getBoolean("defaults.blacklist.enabled");
		boolean whitelistEnabled = Configuration.getBoolean("defaults.blacklist.whitelist");
		List<String> blacklistedBlocks = Configuration.getList("defaults.blacklist.blocks");
		
		
		// - - Protection defaults
		int protectionPadding = Configuration.getInt("defaults.protection.padding");
		int protectionPaddingTop = Configuration.getInt("defaults.protection.padding-top");
		
		// - - - PVP
		boolean protectionPVPEnabled = Configuration.getBoolean("defaults.protection.pvp");
		
		// - - - Block breaking
		boolean protectionBreakingEnabled = Configuration.getBoolean("defaults.protection.breaking.enabled");
		boolean protectionBreakingBlacklistEnabled = Configuration.getBoolean("defaults.protection.breaking.blacklist.enabled");
		boolean protectionBreakingWhitelistEnabled = Configuration.getBoolean("defaults.protection.breaking.blacklist.whitelist");
		List<String> protectionBreakingBlacklistedBlocks = Configuration.getList("defaults.protection.breaking.blacklist.blocks");
		
		// - - - Block placement
		boolean protectionPlacingEnabled = Configuration.getBoolean("defaults.protection.placing.enabled");
		boolean protectionPlacingBlacklistEnabled = Configuration.getBoolean("defaults.protection.placement.blacklist.enabled");
		boolean protectionPlacingWhitelistEnabled = Configuration.getBoolean("defaults.protection.placement.blacklist.whitelist");
		List<String> protectionPlacingBlacklistedBlocks = Configuration.getList("defaults.protection.placement.blacklist.blocks");
		
		// - - - Coordinates
		
		int[] p1 = new int[3];
		int[] p2 = new int[3];
		
		if((int)loc[0].getX() < (int)loc[1].getX())
		{
			p1[0] = loc[0].getBlockX();
			p2[0] = loc[1].getBlockX();
		}
		else
		{
			p2[0] = loc[0].getBlockX();
			p1[0] = loc[1].getBlockX();
		}
		
		if((int)loc[0].getY() < (int)loc[1].getY())
		{
			p1[1] = loc[0].getBlockY();
			p2[1] = loc[1].getBlockY();
		}
		else
		{
			p2[1] = loc[0].getBlockY();
			p1[1] = loc[1].getBlockY();
		}
		
		if((int)loc[0].getZ() < (int)loc[1].getZ())
		{
			p1[2] = loc[0].getBlockZ();
			p2[2] = loc[1].getBlockZ();
		}
		else
		{
			p2[2] = loc[0].getBlockZ();
			p1[2] = loc[1].getBlockZ();
		}
		
		// - - Materials
		List<String> blockList = Configuration.getList("defaults.materials.blocks");
		List<String> weightList = Configuration.getList("defaults.materials.weights");	
		
		// - - Reset
		
		String generator = Configuration.getString("defaults.reset.generator").toUpperCase();
		
		// - - - Auto
		boolean resetAutoEnabled = Configuration.getBoolean("defaults.reset.auto.reset");
		int resetAutoTime = Configuration.getInt("defaults.reset.auto.reset-every");
		boolean resetAutoWarnEnabled = Configuration.getBoolean("defaults.reset.auto.warn");
		List<String> resetAutoWarnTime = Configuration.getList("defaults.reset.auto.warn-times");




        Message.debug("Finished reading defaults");
		
		// = Setting values to the mine
		String baseNode = "mines." + mineName;
		// = = Basic info
		Regions.setString(baseNode + ".display-name", displayName);
		Regions.setBoolean(baseNode + ".silent", silent);

        Message.debug("Writing blacklist data");
		
		// = = Blacklist
		baseNode = "mines." + mineName + ".blacklist";
		Regions.setBoolean(baseNode + ".enabled", blacklistEnabled);
		Regions.setBoolean(baseNode + ".whitelist", whitelistEnabled);
		Regions.setList(baseNode + ".blocks", blacklistedBlocks);

        Message.debug("Writing protection data");
		
		// = = Protection
		baseNode = "mines." + mineName + ".protection";
		Regions.setInt(baseNode + ".padding", protectionPadding);
		Regions.setInt(baseNode + ".padding-top", protectionPaddingTop);
		
		// = = = PVP
		Regions.setBoolean(baseNode + ".pvp", protectionPVPEnabled);
		
		// = = = Block breaking
		baseNode = "mines." + mineName + ".protection.breaking";
		Regions.setBoolean(baseNode + ".enabled", protectionBreakingEnabled);
		Regions.setBoolean(baseNode + ".blacklist.enabled", protectionBreakingBlacklistEnabled);
		Regions.setBoolean(baseNode + ".blacklist.whitelist", protectionBreakingWhitelistEnabled);
		Regions.setList(baseNode + ".blacklist.blocks", protectionBreakingBlacklistedBlocks);
		
		// = = = Block placement
		baseNode = "mines." + mineName + ".protection.placement";
		Regions.setBoolean(baseNode + ".enabled", protectionPlacingEnabled);
		Regions.setBoolean(baseNode + ".blacklist.enabled", protectionPlacingBlacklistEnabled);
		Regions.setBoolean(baseNode + ".blacklist.whitelist", protectionPlacingWhitelistEnabled);
		Regions.setList(baseNode + ".blacklist.blocks", protectionPlacingBlacklistedBlocks);

        Message.debug("Writing coordinates");
		
		// = = Coordinates
		baseNode = "mines." + mineName + ".coordinates";
		Regions.setString(baseNode + ".world", loc[0].getWorld().getName());
		
		// = = = Position 0
		Regions.setInt(baseNode + ".pos0.x", p1[0]);
		Regions.setInt(baseNode + ".pos0.y", p1[1]);
		Regions.setInt(baseNode + ".pos0.z", p1[2]);
		
		// = = = Position 1
		Regions.setInt(baseNode + ".pos1.x", p2[0]);
		Regions.setInt(baseNode + ".pos1.y", p2[1]);
		Regions.setInt(baseNode + ".pos1.z", p2[2]);
				
		// = = = Position 2 (mine spawn point)
		Regions.setDouble(baseNode + ".pos2.x", player.getLocation().getX());
		Regions.setDouble(baseNode + ".pos2.y", player.getLocation().getY());
		Regions.setDouble(baseNode + ".pos2.z", player.getLocation().getZ());
		Regions.setDouble(baseNode + ".pos2.yaw", player.getLocation().getYaw());
		Regions.setDouble(baseNode + ".pos2.pitch", player.getLocation().getPitch());

        Message.debug("Writing reset data");
		
		// = = Materials
		baseNode = "mines." + mineName + ".materials";
		Regions.setList(baseNode + ".blocks", blockList);
		Regions.setList(baseNode + ".weights", weightList);
		
		// = = Reset
		
		baseNode = "mines." + mineName + ".reset";
		Regions.setString(baseNode + ".generator", generator);
		
		// = = = Automatic
		baseNode = "mines." + mineName + ".reset.auto";
		Regions.setBoolean(baseNode + ".reset", resetAutoEnabled);
		Regions.setInt(baseNode + ".reset-every", resetAutoTime);
		Regions.setBoolean(baseNode + ".warn", resetAutoWarnEnabled);
		Regions.setList(baseNode + ".warn-times", resetAutoWarnTime);
		
		// = = = = Data
		baseNode = "mines." + mineName + ".reset.auto.data";
		Regions.setInt(baseNode + ".next", resetAutoTime);
		
		// = = Adding to the mine list
		List<String> mineList = Regions.getList("data.list-of-mines");
		mineList.add(mineName);
		Regions.setList("data.list-of-mines", mineList);

        Message.debug("Mine creation completed");
		
		CommandManager.setLocation(null, 0);
		CommandManager.setLocation(null, 1);
		
		Regions.saveData();

        Message.debug("Data saved successfully");
		CommandManager.setMine(mineName);
		Message.sendSuccess("Mine '" + mineName + "' created successfully!");
		return;
	}
}
