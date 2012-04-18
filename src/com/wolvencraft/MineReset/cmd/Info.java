package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Info
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("info", true))
		{
			Util.sendDenied(args);
			return;
		}
		if(args.length == 1)
		{
			Help.getInfo();
			return;
		}
		if(args.length != 2)
		{
			Util.sendInvalid(args);
			return;
		}
		
		if(!Util.mineExists(args[1]))
		{
			Util.sendError("Mine '" + args[1] + "' does not exist. Use " + ChatColor.GREEN + "/mine help" + ChatColor.WHITE + " for help");
			return;
		}
		String mineName = args[1];
		
		List<String> itemList = Util.getRegionList("mines." + mineName + ".materials.blocks");
		List<String> weightList = Util.getRegionList("mines." + mineName + ".materials.weights");
		
		Util.sendMessage(ChatColor.DARK_RED + "                    -=[ " + mineName + " ]=-");
		
		boolean autoReset = Util.getRegionBoolean("mines." + mineName + ".reset.auto.reset");
		//TODO: Timer!
		int nextResetMin = 0;//Util.getRegionInt(mineName + ".auto-reset.time.cur-min");
		int nextResetSec = 0;//Util.getRegionInt(mineName + ".auto-reset.time.cur-sec");
		String blockName;
		String blockWeight;
		int blockID;
		if(autoReset)
		{
			Util.sendMessage(" Resets every " + ChatColor.GOLD +  Util.getRegionInt(mineName + ".reset.auto.reset-time") + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetMin + ChatColor.WHITE + " minutes " + ChatColor.GOLD + nextResetSec + ChatColor.WHITE + " seconds");
		}
		else
		{
			Util.sendMessage("The mine has to be reset manually");
		}
		Util.sendMessage(ChatColor.BLUE + " Composition:");
		for(int i = 0; i < itemList.size(); i ++)
		{
			blockID = Integer.parseInt(itemList.get(i));
			blockName = Material.getMaterial(blockID).toString();
			blockWeight = weightList.get(i);
			Util.sendMessage(" - " + blockWeight + "% " + ChatColor.GREEN + blockName.toLowerCase().replace("_", " "));
		}
		List<String> blacklistBlocks =  Util.getRegionList("mines." + mineName + ".blacklist.blocks");
		if(Util.getRegionBoolean(mineName + ".blacklist.enabled") && blacklistBlocks.size() != 0)
		{
			if(Util.getRegionBoolean(mineName + ".blacklist.whitelist"))
			{
				Util.sendMessage(ChatColor.BLUE + " Blacklist:");
			}
			else Util.sendMessage(ChatColor.BLUE + " Whitelist:");
			
			for(int i = 0; i < blacklistBlocks.size(); i++)
			{
				if(Util.debugEnabled()) Util.log("Blacklist: " + blacklistBlocks.get(i));
				Util.sendMessage(" - " + Material.getMaterial(blacklistBlocks.get(i)).toString());
			}
		}
		return;
	}
}
