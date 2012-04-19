package com.wolvencraft.MineReset.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.wolvencraft.MineReset.CommandManager;

public class Info
{
	public static void run(String[] args)
	{
		if(!Util.senderHasPermission("info", true))
		{
			Util.sendDenied(args);
			return;
		}
		if(args.length == 1 && CommandManager.getMine() == null)
		{
			Help.getInfo();
			return;
		}
		if(args.length > 2)
		{
			Util.sendInvalid(args);
			return;
		}
		
		if(args.length != 1 && !Util.mineExists(args[1]))
		{
			Util.sendError("Mine '" + args[1] + "' does not exist. Use " + ChatColor.GREEN + "/mine help" + ChatColor.WHITE + " for help");
			return;
		}
		String mineName;
		if(CommandManager.getMine() == null) mineName = args[1];
		else mineName = CommandManager.getMine();
		
		
		
		// Title
		String displayName = Util.getRegionString("mines." + mineName + ".display-name");
		if(displayName.equals("")) displayName = mineName;
		Util.sendMessage(ChatColor.DARK_RED + "                    -=[ " + displayName + " ]=-");
		
		// Reset
		boolean autoReset = Util.getRegionBoolean("mines." + mineName + ".reset.auto.reset");
		int autoResetTime = Util.getRegionInt("mines." + mineName + ".reset.auto.reset-time");
		int nextResetMin = Util.getRegionInt("mines." + mineName + ".reset.auto.data.min");
		int nextResetSec = Util.getRegionInt("mines." + mineName + ".reset.auto.data.sec");
		
		if(autoReset)
		{
			Util.sendMessage(" Resets every " + ChatColor.GOLD +  autoResetTime + ChatColor.WHITE + " minutes. Next reset in " + ChatColor.GOLD + nextResetMin + ChatColor.WHITE + " minutes " + ChatColor.GOLD + nextResetSec + ChatColor.WHITE + " seconds");
		}
		else
		{
			Util.sendMessage("The mine has to be reset manually");
		}
		
		// Protection
		String pvp;
		if(Util.getRegionBoolean("mines" + mineName + ".protection.pvp.enabled"))
			pvp = ChatColor.GREEN + "ON";
		else
			pvp = ChatColor.RED + "OFF";
		
		String breakingProt;
		if(Util.getRegionBoolean("mines." + mineName + ".protection.breaking.enabled"))
		{
			
			if(Util.getRegionBoolean("mines." + mineName + ".protection.breaking.blacklist.enabled"))
			{
				if(Util.getRegionBoolean("mines." + mineName + ".protection.breaking.blacklist.whitelist"))
					breakingProt = ChatColor.GREEN + "ON" + ChatColor.WHITE + " (W)";
				else
					breakingProt = ChatColor.GREEN + "ON" + ChatColor.WHITE + " (B)";
			}
			else
				breakingProt = ChatColor.GREEN + "ON";
		}
		else
		{
			breakingProt = ChatColor.RED + "OFF";
		}

		String placementProt;
		if(Util.getRegionBoolean("mines." + mineName + ".protection.placement.enabled"))
		{
			if(Util.getRegionBoolean("mines." + mineName + ".protection.placement.blacklist.enabled"))
			{
				if(Util.getRegionBoolean("mines." + mineName + ".protection.placement.blacklist.whitelist"))
					placementProt = ChatColor.GREEN + "ON" + ChatColor.WHITE + " (W)";
				else
					placementProt = ChatColor.GREEN + "ON" + ChatColor.WHITE + " (B)";
			}
			else
				placementProt = ChatColor.GREEN + "ON";
		}
		else
		{
			placementProt = ChatColor.RED + "OFF";
		}
		
		Util.sendMessage(ChatColor.BLUE + " Protection:");
		Util.sendMessage(" PVP: " + pvp + ChatColor.WHITE + " | Breaking: " + breakingProt + ChatColor.WHITE + " | Placement: " + placementProt + ChatColor.WHITE + " ");
		
		// Composition
		List<String> itemList = Util.getRegionList("mines." + mineName + ".materials.blocks");
		List<String> weightList = Util.getRegionList("mines." + mineName + ".materials.weights");
		
		String blockName;
		String blockWeight;
		int blockID;
		
		Util.sendMessage(ChatColor.BLUE + " Composition:");
		for(int i = 0; i < itemList.size(); i ++)
		{
			blockID = Integer.parseInt(itemList.get(i));
			blockName = Material.getMaterial(blockID).toString();
			if(Double.parseDouble(weightList.get(i)) < 10)
				blockWeight = " " + weightList.get(i);
			else
				blockWeight = weightList.get(i);
			Util.sendMessage(" - " + blockWeight + "% " + ChatColor.GREEN + blockName.toLowerCase().replace("_", " "));
		}
		
		// Blacklist
		List<String> blacklistBlocks =  Util.getRegionList("mines." + mineName + ".blacklist.blocks");
		if(Util.getRegionBoolean("mines." + mineName + ".blacklist.enabled") && blacklistBlocks.size() != 0)
		{
			if(Util.getRegionBoolean("mines." + mineName + ".blacklist.whitelist"))
			{
				Util.sendMessage(ChatColor.BLUE + " Blacklist:");
			}
			else Util.sendMessage(ChatColor.BLUE + " Whitelist:");
			
			for(String block : blacklistBlocks)
			{
				if(Util.debugEnabled()) Util.log("Blacklisted: " + block);
				Util.sendMessage(" - " + Material.getMaterial(Integer.parseInt(block)).toString());
			}
		}
		return;
	}
}
